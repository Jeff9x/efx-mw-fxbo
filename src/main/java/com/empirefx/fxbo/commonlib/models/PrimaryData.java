package com.empirefx.fxbo.commonlib.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrimaryData implements Serializable {

	/** Serial version UID */
	private static final long serialVersionUID = -8478984558316046120L;

	private String businessKey;
	private String businessKeyType;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PrimaryData [businessKey=");
		builder.append(businessKey);
		builder.append(", businessKeyType=");
		builder.append(businessKeyType);
		builder.append("]");
		return builder.toString();
	}

}