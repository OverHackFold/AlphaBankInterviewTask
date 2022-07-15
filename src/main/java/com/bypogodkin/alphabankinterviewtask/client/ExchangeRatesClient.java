package com.bypogodkin.alphabankinterviewtask.client;

import com.bypogodkin.alphabankinterviewtask.model.ExchangeRate;

public interface ExchangeRatesClient {
        ExchangeRate getLatestRates(String appId);
        ExchangeRate getHistoricalRates(String date, String appId);
}
