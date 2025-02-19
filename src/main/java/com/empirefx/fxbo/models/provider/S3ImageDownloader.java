package com.empirefx.fxbo.models.provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Component
public class S3ImageDownloader {

    private static String ACCESS_KEY;
    private static String SECRET_KEY;
    private static String REGION;
    private static String SERVICE;
    private static String SIGNING_ALGORITHM;

    @Value("${aws-config.accessKey}")
    public void setAccessKey(String accessKey){
        ACCESS_KEY = accessKey;
    }

    @Value("${aws-config.secretKey}")
    public void setSecretKey(String secretKey){
        SECRET_KEY = secretKey;
    }

    @Value("${aws-config.region}")
    public void setRegion(String region){
        REGION = region;
    }

    @Value("${aws-config.service}")
    public void setService(String service){
        SERVICE = service;
    }

    @Value("${aws-config.signingAlgorithm}")
    public void setSigningAlgorithm(String signingAlgorithm){
        SIGNING_ALGORITHM = signingAlgorithm;
    }


    public static byte[] downloadImage(String imageUrl) throws Exception {
        // Create the URL connection
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Generate headers
        Map<String, String> headers = generateAWSSignatureHeaders(imageUrl, "GET");

        // Set headers
        headers.forEach(connection::setRequestProperty);

        // Read the response
        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray(); // Return image as byte array
        }
    }

    private static Map<String, String> generateAWSSignatureHeaders(String url, String httpMethod) throws Exception {
        Map<String, String> headers = new HashMap<>();

        // Date formatting for AWS signature
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateTimeFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
        String amzDate = dateTimeFormat.format(new Date());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
        String dateStamp = dateFormat.format(new Date());

        // Generate X-Amz-Content-Sha256 (for an empty payload)
        String payloadHash = hash("");

        // Canonical headers
        String host = new URL(url).getHost();
        String canonicalHeaders = "host:" + host + "\n" + "x-amz-content-sha256:" + payloadHash + "\n" + "x-amz-date:" + amzDate + "\n";
        String signedHeaders = "host;x-amz-content-sha256;x-amz-date";

        // Canonical request
        String canonicalUri = new URL(url).getPath();
        String canonicalRequest = httpMethod + "\n" + canonicalUri + "\n" + "\n" + canonicalHeaders + "\n" + signedHeaders + "\n" + payloadHash;

        // String to sign
        String credentialScope = dateStamp + "/" + REGION + "/" + SERVICE + "/aws4_request";
        String stringToSign = SIGNING_ALGORITHM + "\n" + amzDate + "\n" + credentialScope + "\n" + hash(canonicalRequest);

        // Signature
        byte[] signingKey = getSignatureKey(SECRET_KEY, dateStamp, REGION, SERVICE);
        String signature = bytesToHex(hmacSHA256(signingKey, stringToSign));

        // Authorization header
        String authorizationHeader = SIGNING_ALGORITHM + " Credential=" + ACCESS_KEY + "/" + credentialScope + ", SignedHeaders=" + signedHeaders + ", Signature=" + signature;

        // Set headers
        headers.put("X-Amz-Content-Sha256", payloadHash);
        headers.put("X-Amz-Date", amzDate);
        headers.put("Authorization", authorizationHeader);

        return headers;
    }

    private static String hash(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashedBytes);
    }

    private static byte[] hmacSHA256(byte[] key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
        byte[] kSecret = ("AWS4" + key).getBytes(StandardCharsets.UTF_8);
        byte[] kDate = hmacSHA256(kSecret, dateStamp);
        byte[] kRegion = hmacSHA256(kDate, regionName);
        byte[] kService = hmacSHA256(kRegion, serviceName);
        return hmacSHA256(kService, "aws4_request");
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        try {
            String imageUrl = "https://efx-storage.lon1.digitaloceanspaces.com/docs/kyc/test.jpg";
            byte[] imageBytes = downloadImage(imageUrl);

            System.out.println("Image downloaded successfully, size: " + imageBytes.length + " bytes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
