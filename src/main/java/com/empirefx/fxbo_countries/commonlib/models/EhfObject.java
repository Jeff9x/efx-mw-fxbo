package com.empirefx.fxbo_countries.commonlib.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EhfObject implements Serializable {

	private static final long serialVersionUID = -8551895066257437538L;
	private String key;
	private EhfRecord ehfRecord;

}
