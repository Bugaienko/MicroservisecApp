package ua.bugaienko.currencyrateservice.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.bugaienko.currencyrateservice.client.HttpCurrencyDateRateClient;
import ua.bugaienko.currencyrateservice.schema.ValCurs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Sergii Bugaienko
 */

@Service
public class CbrService {

    private final Cache<LocalDate, Map<String, BigDecimal>> cache;

    private final HttpCurrencyDateRateClient client;

    @Autowired
    public CbrService(HttpCurrencyDateRateClient client) {
        this.client = client;
        this.cache = CacheBuilder.newBuilder().build();
    }

    public BigDecimal requestByCurrencyCode(String code) {
        try {
            return cache.get(LocalDate.now(), this::callAllByCurrentDate).get(code);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, BigDecimal> callAllByCurrentDate() {
        var xml = client.requestByDate(LocalDate.now());
        ValCurs response = unmarshal(xml);

        return response.getValute().stream().collect(Collectors.toMap(ValCurs.Valute::getCharCode,
                item -> parseWithLocale(item.getValue())));
    }

    private BigDecimal parseWithLocale(String currency) {
        try {
            double v = NumberFormat.getNumberInstance(Locale.getDefault()).parse(currency).doubleValue();
            return BigDecimal.valueOf(v);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private ValCurs unmarshal(String xml) {
        try (StringReader reader = new StringReader(xml)) {
            JAXBContext context = JAXBContext.newInstance(ValCurs.class);
            return (ValCurs) context.createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
