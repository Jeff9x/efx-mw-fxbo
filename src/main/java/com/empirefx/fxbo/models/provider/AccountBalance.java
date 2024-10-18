package com.empirefx.fxbo.models.provider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountBalance {
    private String login;
    private String currency;
    private String balance;
    @JsonIgnore
    private String serverId;

}