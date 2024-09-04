package com.empirefx.fxbo_countries.models.provider;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProviderSmsDetails implements Serializable {

    private static final long serialVersionUID = -1201884338314200440L;

    @SerializedName("dest")
    private String dest;
    @SerializedName("src")
    private String src;
    @SerializedName("text")
    private String text;
    @SerializedName("unicode")
    private Boolean unicode;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("dest=");
        builder.append(dest);
        builder.append(", src=");
        builder.append(src);
        builder.append(", text=");
        builder.append(text);
        builder.append(", unicode=");
        builder.append(unicode);
        return builder.toString();
    }
}
