package com.empirefx.fxbo_countries.commonlib.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EhfRecord implements Serializable {

    private static final long serialVersionUID = -1317559232336740643L;
    private String statusCode;
    private String statusDescription;
    private String businessDescription;
    private String ehfRef;
    private String ehfDesc;
}
