package com.empirefx.fxbo.models.provider;
import java.util.List;
import java.util.Map;

public class UserAccountResponse {
    private int id;
    private int managerId;
    private String country;
    private String city;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String secondaryEmail;
    private String referrer;
    private String phone;
    private String secondaryPhone;
    private boolean lead;
    private String registrationDate;
    private String updatedAt;
    private String promotedToClientAt;
    private String lastLoginDate;
    private String referralLink;
    private String status;
    private String secondaryStatus;
    private String commpeakStatus;
    private String cellxpertCxd;
    private String title;
    private boolean isIb;
    private boolean isTrader;
    private boolean enabled;
    private String vat;
    private String companyRegNumber;
    private String companyJurisdiction;
    private String nationality;
    private String address;
    private String state;
    private String zipCode;
    private String clientType;
    private String phoneCode;
    private String lei;
    private String taxResidency;
    private String countryOfBirth;
    private String cityOfBirth;
    private String birthDate;
    private String language;
    private List<String> tags;
    private Map<String, String> customFields;
    private boolean emailVerified;
    private boolean phoneVerified;
    private List<String> notificationPreferences;
    private boolean testProfile;
    private String mifidCategorization;
    private String tin;
    private String noTinReason;
    private String registerSocialProvider;
    private boolean pep;
    private String riskLevel;
    private String riskCategorization;
    private String riskComment;
    private int cid;
    private boolean verified;
    private String tradingStatus;
    private String clientIp;
    private String source;
    private String firstDepositId;
    private String firstDepositDate;
    private String lastDepositId;
    private String lastDepositDate;
    private int maxLeverage;
    private String lastTradedAt;
    private String ibCampaignId;
    private boolean canRequestIb;
    private boolean canCreateIbLinks;
    private String masterPartnerId;
    private String entryPoint;
    private Integer ibTreeDepth;
    private MarketingDetails marketingDetails;

    // Getters and setters

    public static class MarketingDetails {
        private String marketingLinkId;
        private String marketingLink;
        private String referrer;
        private String cellxpertCxd;
        private UTM utm;

        public static class UTM {
            private String campaign;
            private String content;
            private String medium;
            private String source;
            private String term;

            // Getters and setters
        }

        // Getters and setters
    }

    @Override
    public String toString() {
        return "ClientResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", tags=" + tags +
                ", customFields=" + customFields +
                ", notificationPreferences=" + notificationPreferences +
                ", riskCategorization='" + riskCategorization + '\'' +
                '}';
    }
}