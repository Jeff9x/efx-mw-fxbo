package com.empirefx.fxbo.processors;
import com.empirefx.fxbo.models.provider.AppTokenJava;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.empirefx.fxbo.models.provider.DownloadGcpImage.downloadImageFromGoogleUrl;
import static com.empirefx.fxbo.models.provider.S3ImageDownloader.downloadImage;


@Component
public class ImageUploadRequestProcessor implements Processor {

    private final ObjectMapper objectMapper;

    public ImageUploadRequestProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    String encodeFront;
    String encodeBack;
    @Value("${adaptive.POIConfig}")
    private Integer config;
    String user;
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

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void process(Exchange exchange) throws Exception {


        // Extract the incoming request body as a Map
        @SuppressWarnings("unchecked")
        Map<String, Object> requestBody = exchange.getIn().getBody(Map.class);

        System.out.println("Incoming Request: " + requestBody);



        // Parse the JSON payload
        JSONObject jsonObject = new JSONObject(requestBody);
        System.out.println("Incoming JSON: " + jsonObject);
        user = String.valueOf(Integer.parseInt(String.valueOf(Integer.valueOf(requestBody.get("user").toString()))));
        System.out.println("Incoming User: " + user);
        uploadedByClient = Boolean.parseBoolean(requestBody.get("uploadedByClient").toString());
        status = requestBody.get("status").toString();
        System.out.println("Incoming status: " + status);
        //
        JSONObject dataObject = jsonObject.getJSONObject("data");
        System.out.println("Incoming Data: " + dataObject);

        expiresAt = String.valueOf(dataObject.get("expires_at"));
        System.out.println("Incoming expiresAt: " + expiresAt);
        countryOfIssue = String.valueOf(dataObject.get("country_of_issue"));
        System.out.println("Incoming countryOfIssue: " + countryOfIssue);
        documentNumber = String.valueOf(dataObject.get("document_number"));
        System.out.println("Incoming documentNumber: " + documentNumber);
        type = String.valueOf(dataObject.get("type"));
        System.out.println("Incoming type: " + type);

        JSONArray frontSideArray = dataObject.getJSONArray("front_side");
        System.out.println("Incoming Front Side: " + frontSideArray);
        JSONArray backSideArray = dataObject.getJSONArray("back_side");
        System.out.println("Incoming Back Side: " + backSideArray);

        for (int i = 0; i < frontSideArray.length(); i++)
        {
            JSONObject fileObject = frontSideArray.getJSONObject(i);



            String filePath = fileObject.getString("file");
            String fileName = fileObject.getString("name");


//            byte[] imageBytes = downloadImageFromGoogleUrl(filePath);

            byte[] imageBytes = downloadImage(filePath);

            File file = null;
            if (Objects.equals(type, "National ID") && Objects.equals(fileName, "FRONT_SIDE")) {
                // File object pointing to the output file
                file = new File("front_side.png");

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    // Writing byte data to the file
                    fos.write(imageBytes);
                    System.out.println("Front File has been created: " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            assert file != null;
            frontFile = AppTokenJava.encodeFileToBase64(file.getAbsolutePath());
            frontName = fileName;

        }
//        System.out.println("Incoming Front Side Encoded: " + encodeFront);

        for (int i = 0; i < backSideArray.length(); i++)
        {
            JSONObject fileObjectBack = backSideArray.getJSONObject(i);

            String filePathBack = fileObjectBack.getString("file");
            String fileNameBack = fileObjectBack.getString("name");


//            Resource urlResourceBack = resourceLoader.getResource(filePathBack);

//            System.out.println("Front urlImage: " + urlResourceBack);

//            byte[] imageBytesBack = toByteArray(urlResourceBack.getInputStream());

            byte[] imageBytesBack = downloadImage(filePathBack);

//            byte[] imageBytesBack = downloadImageFromGoogleUrl(filePathBack);

            File fileback = null;
            if (Objects.equals(type, "National ID") && Objects.equals(fileNameBack, "BACK_SIDE")) {
                // File object pointing to the output file
                fileback = new File("back_side.png");

                try (FileOutputStream fos = new FileOutputStream(fileback)) {
                    // Writing byte data to the file
                    fos.write(imageBytesBack);
                    System.out.println("Back Side File has been created: " + fileback.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            assert fileback != null;
            backFile = AppTokenJava.encodeFileToBase64(fileback.getAbsolutePath());
            backName = fileNameBack;
        }

//        System.out.println("Incoming Back Side Encoded: " + encodeBack);

        // Optionally: log the updated request
//        System.out.println("Processed Request: " + requestBody);

        String finalPayload = AppTokenJava.toString(config,user,status,expiresAt,documentNumber,type, countryOfIssue,
                 frontFile,  frontName, backFile , backName, String.valueOf(uploadedByClient));

        System.out.println("finalpayload"+finalPayload);

        // Convert the Map to JSON using ObjectMapper
//        String jsonRequestBody = objectMapper.writeValueAsString(finalPayload);

//        System.out.println("Processed JSON Request: " + jsonRequestBody);
        // Set the JSON string back to the exchange body for further processing
        exchange.getIn().setBody(finalPayload);
    }

}
