package com.empirefx.fxbo.models.provider;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponse {
    private String login;
    private int serverId;
    private int userId;
    private String createdAt;
    private String groupName;
    private String currency;
    private int isEnabled;
    private int leverage;
    private double balance;
    private double credit;
    private double equity;
    private double margin;
    private Object customFields;  // This can be an object or null
    private Object managerId;     // This can be an object or null
    private Object accountTypeId; // This can be an object or null
    private String tradingStatus;
    private Object companyId;     // This can be an object or null
    private Object tags;          // This can be an object or null


}