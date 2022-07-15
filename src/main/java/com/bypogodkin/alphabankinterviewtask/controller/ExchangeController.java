package com.bypogodkin.alphabankinterviewtask.controller;

import com.bypogodkin.alphabankinterviewtask.servise.ExchangeService;
import com.bypogodkin.alphabankinterviewtask.servise.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rates")
public class ExchangeController {
    private final ExchangeService exchangeService;
    private final GifService gifService;
    @Value("${giphy.rich}")
    private String richTag;
    @Value("${giphy.broke}")
    private String brokeTag;
    @Value("${giphy.zero}")
    private String whatTag;
    @Value("${giphy.error}")
    private String errorTag;

    @Autowired
    public ExchangeController(
            final ExchangeService exchangeRatesService,
            final GifService gifService) {
        this.exchangeService = exchangeRatesService;
        this.gifService = gifService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getcurrency")
    public List<String> getCurrencyQuotes() {
        return exchangeService.getQuotes();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getgif/{code}")
    public ResponseEntity<Map> getGif(@PathVariable String code) {
        ResponseEntity<Map> result = null;
        int gifKey = -101;
        String gifTag = this.errorTag;
        if (code != null) {
            gifKey = exchangeService.checkRates(code);
        }
        switch (gifKey) {
            case 1:
                gifTag = this.richTag;
                break;
            case -1:
                gifTag = this.brokeTag;
                break;
            case 0:
                gifTag = this.whatTag;
                break;
        }
        result = gifService.getGif(gifTag);
        return result;
    }


}


