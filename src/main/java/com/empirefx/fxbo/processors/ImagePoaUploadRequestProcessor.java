package com.empirefx.fxbo.processors;

import com.empirefx.fxbo.models.provider.AppTokenJava;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.empirefx.fxbo.models.provider.DownloadGcpImage.downloadImageFromGoogleUrl;


@Component
public class ImagePoaUploadRequestProcessor implements Processor {

    private final ObjectMapper objectMappers;

    private ImagePoaUploadRequestProcessor(ObjectMapper objectMapper) {
        this.objectMappers = objectMapper;
    }
    String encodeFront;
    String encodeBack;
    @Value("${adaptive.POAConfig}")
//    private Integer config;
    String config;
    String user;
    String status;
    String type;
    String address;
    String postal_code;
    String country;
    String city;
    String utilityFile;
    String utilityName;
    boolean uploadedByClient;
//    Data datas;

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
        config = String.valueOf(Integer.parseInt(String.valueOf(Integer.valueOf(requestBody.get("config").toString()))));
        user = String.valueOf(Integer.parseInt(String.valueOf(Integer.valueOf(requestBody.get("user").toString()))));
        System.out.println("Incoming User: " + user);
        uploadedByClient = Boolean.parseBoolean(requestBody.get("uploadedByClient").toString());
        status = requestBody.get("status").toString();
        System.out.println("Incoming status: " + status);
        //
        JSONObject dataObject = jsonObject.getJSONObject("data");
        System.out.println("Incoming Data: " + dataObject);

        address = String.valueOf(dataObject.get("address"));
        System.out.println("Incoming address: " + address);
        postal_code = String.valueOf(dataObject.get("postal_code"));
        System.out.println("Incoming postal_code: " + postal_code);
        country = String.valueOf(dataObject.get("country"));
        System.out.println("Incoming country: " + country);
        city = String.valueOf(dataObject.get("city"));
        System.out.println("Incoming city: " + city);
        type = String.valueOf(dataObject.get("type"));
        System.out.println("Incoming type: " + type);

        JSONArray backSideArray = dataObject.getJSONArray("file");
        System.out.println("Incoming Back Side: " + backSideArray);


        for (int i = 0; i < backSideArray.length(); i++)
        {
            JSONObject fileObjectBack = backSideArray.getJSONObject(i);

            String filePath = fileObjectBack.getString("file");
            String fileName = fileObjectBack.getString("name");

//            Resource urlResource = resourceLoader.getResource(filePath);
//
//            System.out.println("Front urlImage: " + urlResource);

//            byte[] imageBytes = toByteArray(urlResource.getInputStream());

            byte[] imageBytes = downloadImageFromGoogleUrl(filePath);

            File file = null;
            if (Objects.equals(type, "Utility Bill")) {
                // File object pointing to the output file
                file = new File("utility_bill.png");

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    // Writing byte data to the file
                    fos.write(imageBytes);
                    System.out.println("Front File has been created: " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            assert file != null;
            utilityFile = AppTokenJava.encodeFileToBase64(file.getAbsolutePath());
            utilityName = fileName;
        }

//        System.out.println("Incoming Back Side Encoded: " + encodeBack);

        // Optionally: log the updated request
//        System.out.println("Processed Request: " + requestBody);

        String finalPayload = AppTokenJava.toStringPoa(config,user,status,type, address,
                postal_code,  country, city, utilityFile , utilityName, String.valueOf(uploadedByClient));

        System.out.println("finalpayload"+finalPayload);

        // Convert the Map to JSON using ObjectMapper
//        String jsonRequestBody = objectMapper.writeValueAsString(finalPayload);

//        System.out.println("Processed JSON Request: " + jsonRequestBody);
        // Set the JSON string back to the exchange body for further processing
        exchange.getIn().setBody(finalPayload);
    }

}
