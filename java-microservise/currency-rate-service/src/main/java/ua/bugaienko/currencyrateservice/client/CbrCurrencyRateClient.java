package ua.bugaienko.currencyrateservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ua.bugaienko.currencyrateservice.config.CurrencyClientConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Sergii Bugaienko
 */

@Component
public class CbrCurrencyRateClient implements HttpCurrencyDateRateClient {

    private final CurrencyClientConfig clientConfig;
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Autowired
    public CbrCurrencyRateClient(CurrencyClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public String requestByDate(LocalDate date) {
        var baseUrl = clientConfig.getUrl();
//        var baseUrl = "https://cbr.ru/scripts/XML_daily.asp";
//        ?date_req=09/05/2023
        var client = HttpClient.newHttpClient();
        var url = buildUriRequest(baseUrl, date);
        try {

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private String buildUriRequest(String baseUrl, LocalDate date) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("date_req", DATE_TIME_FORMATTER.format(date))
                .build().toUriString();
    }
}
