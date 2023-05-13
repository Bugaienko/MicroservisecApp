package ua.bugaienko.currencyrateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import ua.bugaienko.currencyrateservice.config.CurrencyClientConfig;

@SpringBootApplication
@EnableConfigurationProperties(CurrencyClientConfig.class)
@EnableEurekaClient
public class CurrencyRateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyRateServiceApplication.class, args);
    }

}
