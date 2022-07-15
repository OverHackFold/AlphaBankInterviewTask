package com.bypogodkin.alphabankinterviewtask.servise.impl;


import com.bypogodkin.alphabankinterviewtask.servise.GifService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class GifServiceImpl implements GifService {

    @Override
    public ResponseEntity<Map> getGif(String tag) {
        return null;
    }
}
