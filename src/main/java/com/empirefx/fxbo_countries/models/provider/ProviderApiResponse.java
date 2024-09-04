package com.empirefx.fxbo_countries.models.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderApiResponse implements Serializable {

    private static final long serialVersionUID = -7437896624723346380L;

    private String destination;
    private Integer errorCode;
    private String errorMessage;
    private String status;
    private String ticketId;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("destination=");
        builder.append(destination);
        builder.append(", errorCode=");
        builder.append(errorCode);
        builder.append(", errorMessage=");
        builder.append(errorMessage);
        builder.append(", status=");
        builder.append(status);
        builder.append(", ticketId=");
        builder.append(ticketId);
        return builder.toString();
    }
}
