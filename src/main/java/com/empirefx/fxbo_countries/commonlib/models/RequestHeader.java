package com.empirefx.fxbo_countries.commonlib.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestHeader implements Serializable {
    private static final long serialVersionUID = 7656371320496443722L;
    @JsonProperty(required = true)
    protected String messageID;
    @JsonProperty(required = true)
    protected String serviceCode;
    @JsonProperty(required = true)
    protected String serviceName;
    @JsonProperty(required = true)
    protected String channelCode;
    @JsonProperty(required = true)
    protected String channelName;
    @JsonProperty(required = true)
    protected String routeCode;
    @JsonProperty(required = true)
    protected String routeName;
    @JsonProperty(required = true)
    protected String timeStamp;
    @JsonProperty(required = true)
    protected String serviceMode;
    protected String minorServiceVersion;
    @JsonProperty(required = true)
    protected String callBackURL;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RequestHeader [messageID=");
        builder.append(messageID);
        builder.append(", serviceCode=");
        builder.append(serviceCode);
        builder.append(", serviceName=");
        builder.append(serviceName);
        builder.append(", channelCode=");
        builder.append(channelCode);
        builder.append(", channelName=");
        builder.append(channelName);
        builder.append(", routeCode=");
        builder.append(routeCode);
        builder.append(", routeName=");
        builder.append(routeName);
        builder.append(", timeStamp=");
        builder.append(timeStamp);
        builder.append(", serviceMode=");
        builder.append(serviceMode);
        builder.append(", minorServiceVersion=");
        builder.append(minorServiceVersion);
        builder.append(", callBackURL=");
        builder.append(callBackURL);
        builder.append("]");
        return builder.toString();
    }
}
