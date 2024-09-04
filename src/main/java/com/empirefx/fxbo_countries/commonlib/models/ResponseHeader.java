package com.empirefx.fxbo_countries.commonlib.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseHeader implements Serializable {

	/** Serial version UID */
	@Serial
	private static final long serialVersionUID = -7857624541088572239L;

	protected String messageID;
	protected String conversationID;
	protected String targetSystemID;
	protected String channelCode;
	protected String channelName;
	protected String routeCode;
	protected String routeName;
	protected String serviceCode;
	protected String serviceName;
	protected EhfInfo ehfInfo;

	public ResponseHeader() {
		//Unused constructor
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResponseHeader [messageID=");
		builder.append(messageID);
		builder.append(", conversationID=");
		builder.append(conversationID);
		builder.append(", targetSystemID=");
		builder.append(targetSystemID);
		builder.append(", channelCode=");
		builder.append(channelCode);
		builder.append(", channelName=");
		builder.append(channelName);
		builder.append(", serviceName=");
		builder.append(serviceName);
		builder.append(", routeCode=");
		builder.append(routeCode);
		builder.append(", routeName=");
		builder.append(routeName);
		builder.append(", serviceCode=");
		builder.append(serviceCode);
		builder.append(", ehfInfo=");
		builder.append(ehfInfo);
		builder.append("]");
		return builder.toString();
	}

}
