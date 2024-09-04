package com.empirefx.fxbo_countries.commonlib.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Keys implements Serializable {

	private static final long serialVersionUID = 1724083858859234133L;

	private List<StringBuilder> ehfKeys;

	public Keys(List<StringBuilder> ehfKeys) {
		super();
		this.ehfKeys = ehfKeys;
	}

	public Keys() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EhfKeys [ehfKeys=");
		builder.append(ehfKeys);
		builder.append("]");
		return builder.toString();
	}

}
