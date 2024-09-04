package com.empirefx.fxbo_countries.models.consumer.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 6792584434564972167L;

    @SerializedName("errorCode")
    private Integer errorCode;
    @SerializedName("errorMessage")
    private String errorMessage;
    @SerializedName("statusCode")
    private String statusCode;
    @SerializedName("ticketId")
    private String ticketId;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("errorCode=");
        builder.append(errorCode);
        builder.append(", errorMessage=");
        builder.append(errorMessage);
        builder.append(", statusCode=");
        builder.append(statusCode);
        builder.append(", ticketId=");
        builder.append(ticketId);
        return builder.toString();
    }
}
