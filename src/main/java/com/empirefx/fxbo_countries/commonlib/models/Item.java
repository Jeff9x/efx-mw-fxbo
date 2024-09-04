package com.empirefx.fxbo_countries.commonlib.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item implements Serializable {

	private static final long serialVersionUID = 7273566685172729424L;

	private String ehfRef;
	private String ehfDesc;

	public Item() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Item [ehfRef=");
		builder.append(ehfRef);
		builder.append(", ehfDesc=");
		builder.append(ehfDesc);
		builder.append("]");
		return builder.toString();
	}

	public Item(String ehfRef, String ehfDesc) {
		super();
		this.ehfRef = ehfRef;
		this.ehfDesc = ehfDesc;
	}

}