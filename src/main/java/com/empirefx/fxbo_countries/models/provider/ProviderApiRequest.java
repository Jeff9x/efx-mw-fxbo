package com.empirefx.fxbo_countries.models.provider;

import com.google.gson.Gson;
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
public class ProviderApiRequest implements Serializable {

    private static final long serialVersionUID = 2784026292020503432L;

    @SerializedName("account")
    private ProviderAccountDetails account;
    @SerializedName("sms")
    private ProviderSmsDetails sms;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this, ProviderApiRequest.class);
    }

}
