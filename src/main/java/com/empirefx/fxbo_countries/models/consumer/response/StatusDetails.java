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

    @SerializedName("code")
    private String code;
    @SerializedName("name")
    private String name;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("code=");
        builder.append(code);
        builder.append(", name=");
        builder.append(name);
        return builder.toString();
    }
}
