package com.empirefx.fxbo_countries.commonlib.models;

import com.empirefx.fxbo_countries.models.consumer.request.SmsDetails;
import com.empirefx.fxbo_countries.models.provider.ProviderAccountDetails;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
public class RequestPayload implements Serializable {

	/** Serial version */
	@Serial
	private static final long serialVersionUID = 7868310611900741033L;

	private PrimaryData primaryData;
	private ProviderAccountDetails authenticationAccount;
	private SmsDetails sms;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RequestPayload [primaryData=");
		builder.append(primaryData);
		builder.append(", authenticationAccount=");
		builder.append(authenticationAccount);
		builder.append(", sms=");
		builder.append(sms);
		builder.append("]");
		return builder.toString();
	}
}