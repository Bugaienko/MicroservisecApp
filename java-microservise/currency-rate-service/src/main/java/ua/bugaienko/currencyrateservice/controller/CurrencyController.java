package ua.bugaienko.currencyrateservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.bugaienko.currencyrateservice.service.CbrService;

import java.math.BigDecimal;

/**
 * @author Sergii Bugaienko
 */

@RestController
@RequestMapping("currency")
public class CurrencyController {

    private final CbrService currencyService;

    @Autowired
    public CurrencyController(CbrService cbrService) {
        this.currencyService = cbrService;
    }

    @GetMapping("/rate/{code}")
    public BigDecimal currencyRate(@PathVariable("code") String code){
        return currencyService.requestByCurrencyCode(code);
    }

}
