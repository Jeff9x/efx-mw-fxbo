package com.empirefx.fxbo.commonlib.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class RequestWrapper implements Serializable {
	/** Serial version UID */
	@Serial
	private static final long serialVersionUID = 6464903968440189419L;

	@JsonIgnore
	private RequestHeader header;
	private RequestPayload requestPayload;


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RequestWrapper [header=");
		builder.append(header);
		builder.append(", requestPayload=");
		builder.append(requestPayload);
		builder.append("]");
		return builder.toString();
	}

}