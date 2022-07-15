package com.bypogodkin.alphabankinterviewtask.servise.impl;


import com.bypogodkin.alphabankinterviewtask.client.ExchangeRatesClient;
import com.bypogodkin.alphabankinterviewtask.model.ExchangeRate;
import com.bypogodkin.alphabankinterviewtask.servise.ExchangeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Service
public class ExchangeServiceImpl implements ExchangeService {

    private  ExchangeRate prevRates;
    private  ExchangeRate currentRates;

    private final ExchangeRatesClient exchangeRatesClient;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    @Autowired
    public ExchangeServiceImpl(

            @Qualifier("date_bean") SimpleDateFormat dateFormat,
            @Qualifier("time_bean") SimpleDateFormat timeFormat,
            ExchangeRate prevRates, ExchangeRate currentRates, ExchangeRatesClient exchangeRatesClient) {
        this.prevRates = prevRates;
        this.currentRates = currentRates;
        this.exchangeRatesClient = exchangeRatesClient;
        this.dateFormat=dateFormat;
        this.timeFormat=timeFormat;

    }

    @Override
    public List<String> getQuotes() {
        List<String> rates = new ArrayList<>();
        if (this.currentRates.getRates() != null) {
            rates = (List<String>) this.currentRates.getRates().keySet();
        }
        return rates;

    }

    @Override
    public int checkRates(String moneyCode) {
        Double prevCoefficient = this.getQuotes(this.prevRates, moneyCode);
        Double currentCoefficient = this.getQuotes(this.currentRates, moneyCode);
        return prevCoefficient != null && currentCoefficient != null
                ? Double.compare(currentCoefficient, prevCoefficient)
                : -101;
    }

    @Override
    public void refreshRates (long time) {
        long currentTime = System.currentTimeMillis();
        this.refreshCurrentRates(currentTime);
        this.refreshPrevRates(currentTime);
    }

    private void refreshCurrentRates(long time) {
        if (
                this.currentRates == null ||
                        !timeFormat.format(Long.valueOf(this.currentRates.getTimestamp()) * 1000)
                                .equals(timeFormat.format(time))
        ) {
            this.currentRates = exchangeRatesClient.getLatestRates(this.appId);
        }
    }

    private void refreshPrevRates(long time) {
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTimeInMillis(time);
        String currentDate = dateFormat.format(prevCalendar.getTime());
        prevCalendar.add(Calendar.DAY_OF_YEAR, -1);
        String newPrevDate = dateFormat.format(prevCalendar.getTime());
        if (
                this.prevRates == null
                        || (
                        !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(newPrevDate)
                                && !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(currentDate)
                )
        ) {
            this.prevRates = exchangeRatesClient.getHistoricalRates(newPrevDate, appId);
        }
    }
}
