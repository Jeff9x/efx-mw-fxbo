package com.empirefx.fxbo.models.provider;
import java.util.Map;

public class UserRequest {
    private int user;
    private String password;
    private String sid;
    private String groupName;
    private int leverage;
    private double initialBalance;
    private boolean notifyDisable;
    private boolean readOnly;
//    private Map<String, String> customFields;

    // Getters and Setters
    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getLeverage() {
        return leverage;
    }

    public void setLeverage(int leverage) {
        this.leverage = leverage;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public boolean isNotifyDisable() {
        return notifyDisable;
    }

    public void setNotifyDisable(boolean notifyDisable) {
        this.notifyDisable = notifyDisable;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

//    public Map<String, String> getCustomFields() {
//        return customFields;
//    }
//
//    public void setCustomFields(Map<String, String> customFields) {
//        this.customFields = customFields;
//    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "user=" + user +
                ", password='" + password + '\'' +
                ", sid='" + sid + '\'' +
                ", groupName='" + groupName + '\'' +
                ", leverage=" + leverage +
                ", initialBalance=" + initialBalance +
                ", notifyDisable=" + notifyDisable +
                ", readOnly=" + readOnly +
//                ", customFields=" + customFields +
                '}';
    }
}