package com.empirefx.fxbo.models.provider;
import java.util.List;
import java.util.Map;

public class DocumentResponse {
    private int id;
    private int userId;
    private String createdAt;
    private String processedAt;
    private String expiresAt;
    private String type;
    private String comment;
    private String idNumber;
    private String status;
    private String declineReason;
    private boolean uploadedByClient;
    private String description;
    private int configId;
    private Data data;

    // Getters and setters

    public static class Data {
        private String expires_at;
        private String document_number;
        private String type;
        private String country_of_issue;
        private List<Map<String, String>> front_side;  // Should be a list
        private List<Map<String, String>> back_side;   // Should be a list

        // Getters and setters
    }

    @Override
    public String toString() {
        return "DocumentResponse{" +
                "id=" + id +
                ", userId=" + userId +
                ", createdAt='" + createdAt + '\'' +
                ", processedAt='" + processedAt + '\'' +
                ", expiresAt='" + expiresAt + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", status='" + status + '\'' +
                ", declineReason='" + declineReason + '\'' +
                ", uploadedByClient=" + uploadedByClient +
                ", description='" + description + '\'' +
                ", configId=" + configId +
                ", data=" + data +
                '}';
    }
}