package com.empirefx.fxbo.models.provider;

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

    // Getters and setters

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getLeverage() {
        return leverage;
    }

    public void setLeverage(int leverage) {
        this.leverage = leverage;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getEquity() {
        return equity;
    }

    public void setEquity(double equity) {
        this.equity = equity;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public Object getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Object customFields) {
        this.customFields = customFields;
    }

    public Object getManagerId() {
        return managerId;
    }

    public void setManagerId(Object managerId) {
        this.managerId = managerId;
    }

    public Object getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Object accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getTradingStatus() {
        return tradingStatus;
    }

    public void setTradingStatus(String tradingStatus) {
        this.tradingStatus = tradingStatus;
    }

    public Object getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Object companyId) {
        this.companyId = companyId;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "login='" + login + '\'' +
                ", serverId=" + serverId +
                ", userId=" + userId +
                ", createdAt='" + createdAt + '\'' +
                ", groupName='" + groupName + '\'' +
                ", currency='" + currency + '\'' +
                ", isEnabled=" + isEnabled +
                ", leverage=" + leverage +
                ", balance=" + balance +
                ", credit=" + credit +
                ", equity=" + equity +
                ", margin=" + margin +
                ", customFields=" + customFields +
                ", managerId=" + managerId +
                ", accountTypeId=" + accountTypeId +
                ", tradingStatus='" + tradingStatus + '\'' +
                ", companyId=" + companyId +
                ", tags=" + tags +
                '}';
    }
}