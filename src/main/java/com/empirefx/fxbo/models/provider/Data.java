package com.empirefx.fxbo.models.provider;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Data {
    private String expiresAt;
    private String documentNumber;
    private String type;
    private String countryOfIssue;
    private List<DocumentSide> frontSide;
    private List<DocumentSide> backSide;
}