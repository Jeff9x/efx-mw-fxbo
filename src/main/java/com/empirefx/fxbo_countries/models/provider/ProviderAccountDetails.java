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
public class ProviderAccountDetails implements Serializable {

    private static final long serialVersionUID = 4172418819271887756L;

    @SerializedName("password")
    private String password;
    @SerializedName("systemId")
    private String systemId;


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("password=");
        builder.append(password);
        builder.append(", systemId=");
        builder.append(systemId);
        return builder.toString();
    }
}
