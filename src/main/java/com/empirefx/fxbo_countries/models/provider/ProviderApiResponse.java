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

    private String code;
    private Integer name;


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
