package com.bypogodkin.alphabankinterviewtask.client;

import com.bypogodkin.alphabankinterviewtask.model.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "exchange-rates-client",url = "${openexchangerates.url.general}")
public interface FeignExchangeRatesClient extends ExchangeRatesClient{
    @Override
    @GetMapping("/latestRates.json")
    ExchangeRate getLatestRates(
            @RequestParam("app_id") String appId
    );

    @Override
    @GetMapping("/historical/{date}.json")
    ExchangeRate getHistoricalRates(
            @PathVariable String date,
            @RequestParam("app_id") String appId
    );
}
