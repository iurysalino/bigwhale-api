package com.whale.web.documents.compactconverter.model;

import org.springframework.stereotype.Component;

@Component
public class CompactConverterModel {

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}