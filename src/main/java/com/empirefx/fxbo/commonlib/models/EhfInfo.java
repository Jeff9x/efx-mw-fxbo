package com.empirefx.fxbo.commonlib.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EhfInfo implements Serializable {
	/** Serial version UID */
	private static final long serialVersionUID = -8647643527147121819L;
	private List<Item> item;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EhfInfo [item=");
		builder.append(item);
		builder.append("]");
		return builder.toString();
	}

}