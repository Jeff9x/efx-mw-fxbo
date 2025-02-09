package com.empirefx.fxbo.models.provider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.net.URL;

public class DownloadGcpImage {
    private static final String projectId = "prj-empirefx-eng-stg-be";

    public static byte[] downloadImageFromGoogleUrl(String urlString) {
        try {
            // Parse the URL to extract bucket name and object name
            URL url = new URL(urlString);
            String[] pathParts = url.getPath().split("/");

            if (pathParts.length < 3) {
                System.err.println("Invalid URL format: " + urlString);
                return null;
            }

            String bucketName = pathParts[1]; // Extract the bucket name
            String objectName = url.getPath().substring(bucketName.length() + 2); // Extract the object name

            // Initialize the Cloud Storage client with explicit credentials
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(
                            new FileInputStream("/Users/emmanuelmuchiri/Documents/DocumentsZangu/key.json")
                    ))
                    .setProjectId(projectId)
                    .build()
                    .getService();

            // Get the object (image) from the bucket
            Blob blob = storage.get(BlobId.of(bucketName, objectName));

            if (blob == null || !blob.exists()) {
                System.err.println("Error: Object " + objectName + " not found in bucket " + bucketName);
                return null;
            }

            // Return the image as a byte array
            byte[] imageData = blob.getContent();
            System.out.println("Successfully retrieved image: " + objectName);

            return imageData;
        } catch (Exception e) {
            System.err.println("Error downloading image: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String urlString = "https://storage.googleapis.com/bkt-empirefx-eng-stg/ec9cd296-b59e-4553-b2b6-787d55cdb231_gcp_image?X-Goog-Algorithm=GOOG4-RSA-SHA256&X-Goog-Credential=storage-bkt%40prj-empirefx-eng-stg-be.iam.gserviceaccount.com%2F20250123%2Fauto%2Fstorage%2Fgoog4_request&X-Goog-Date=20250123T115940Z&X-Goog-Expires=604800&X-Goog-SignedHeaders=host&X-Goog-Signature=9b2f98178653e7ffb5b3a7efb808b6d265e8d07af06fd46f257504f6d9f89c2d144cdf8467622733dc5295a901c490cc45727fb694715c0f2f5ab852daed20b132c7af4e8e5fdfb97645e72d5e8c0f443ac7d66cee949701c8b268f3e5ebc3264a9b13b443b08b29fbf43109364fb505db91b462b0563766c086bbaaaf188664f9f63f6a5582c45566ecdc2a7802f3c3ae26cc614be0d4911a8222a60182ccbdb20b74c9ea1226d2bb494e3275056514d80db022ba67a1a829a43dbc8908c1f6b585d35815cd6bc44f41f7134adbae0c04ff9ddfaf6d8e6c91f0bc8f331e37232015b97e67734f21a4580b9e92786b0afe9bb36e42e23b2f07f5328c1fd9fbe8";

        // Call the method to download the image as bytes
        byte[] imageBytes = downloadImageFromGoogleUrl(urlString);

        if (imageBytes != null) {
            System.out.println("Image downloaded successfully. Size: " + imageBytes.length + " bytes");
        } else {
            System.out.println("Failed to download image.");
        }
    }
}