package ua.bugaienko.currencyrateservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Sergii Bugaienko
 */


@Data
@ConfigurationProperties(prefix = "currency.client")
public class CurrencyClientConfig {
    private String url;
}
