package com.empirefx.fxbo_countries.commonlib.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.empirefx.fxbo_countries.models.consumer.response.StatusDetails;
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
public class ResponsePayload implements Serializable {

    private static final long serialVersionUID = 353652223581858774L;

    private PrimaryData primaryData;
    private StatusDetails status;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResponsePayload [ primaryData=");
        builder.append(primaryData);
        builder.append(", [status=");
        builder.append(status);
        builder.append("]");
        return builder.toString();
    }

}