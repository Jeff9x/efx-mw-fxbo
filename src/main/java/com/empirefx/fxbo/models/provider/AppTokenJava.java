package com.empirefx.fxbo.models.provider;

import com.empirefx.fxbo.processors.ImageUploadRequestProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;


//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Base64;

@Component
public class AppTokenJava {

    String encodeFront;
    String encodeBack;
    int config;
    int user;
    String status;
    String expiresAt;
    String documentNumber;
    String type;
    String countryOfIssue;
    String frontFile;
    String frontName;
    String backFile;
    String backName;
    boolean uploadedByClient;


//    // Helper method to encode a file to Base64 string
    public static String encodeFileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] fileBytes = new byte[(int) file.length()];
            fileInputStream.read(fileBytes);
            return Base64.getEncoder().encodeToString(fileBytes);  // Return Base64 encoded string
        }
    }

    public static String convertImageToBase64(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }
    public static String toString (String config,String user,String status,String expiresAt, String documentNumber, String type, String countryOfIssue,
                                   String frontFile, String frontName,String backFile ,String backName, String uploadedByClient)throws IOException {
        // Method to construct the payload
            // Create a StringBuilder to build the payload
            StringBuilder builder = new StringBuilder();

            // Start building the JSON payload
            builder.append("{\n");
            builder.append("    \"config\": ").append(config).append(",\n");
            builder.append("    \"user\": ").append(user).append(",\n");
            builder.append("    \"status\": \"").append(status).append("\",\n");
            builder.append("    \"data\": {\n");
            builder.append("        \"expires_at\": \"").append(expiresAt).append("\",\n");
            builder.append("        \"document_number\": \"").append(documentNumber).append("\",\n");
            builder.append("        \"type\": \"").append(type).append("\",\n");
            builder.append("        \"country_of_issue\": \"").append(countryOfIssue).append("\",\n");

            // Append front_side array
            builder.append("        \"front_side\": [\n");
            builder.append("            {\n");
            builder.append("                \"file\": \"").append(frontFile).append("\",\n");
            builder.append("                \"name\": \"").append(frontName).append("\"\n");
            builder.append("            }\n");
            builder.append("        ],\n");

            // Append back_side array
            builder.append("        \"back_side\": [\n");
            builder.append("            {\n");
            builder.append("                \"file\": \"").append(backFile).append("\",\n");
            builder.append("                \"name\": \"").append(backName).append("\"\n");
            builder.append("            }\n");
            builder.append("        ]\n");
            builder.append("    },\n");

            // Append uploadedByClient field
            builder.append("    \"uploadedByClient\": ").append(uploadedByClient).append("\n");
            builder.append("}");
//            System.out.println("String backend"+builder);

            return builder.toString();

        }

    }
