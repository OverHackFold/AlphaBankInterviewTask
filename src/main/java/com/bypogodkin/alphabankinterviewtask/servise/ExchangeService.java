package com.bypogodkin.alphabankinterviewtask.servise;

import java.util.List;

public interface ExchangeService {

    List<String> getQuotes();

    int checkRates (String moneyCode);

    void refreshRates(long time);

}
