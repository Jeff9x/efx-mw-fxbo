package com.empirefx.fxbo_countries.models.consumer.request;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsDetails implements Serializable {

    private static final long serialVersionUID = 8873090595539789807L;

    @SerializedName("sender")
    private String sender;
    @SerializedName("smsText")
    @Size(max = 160)
    private String smsText;
    @SerializedName("unicode")
    private Boolean unicode;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("sender=");
        builder.append(sender);
        builder.append(", smsText=");
        builder.append(smsText);
        builder.append(", unicode=");
        builder.append(unicode);
        return builder.toString();
    }
}
