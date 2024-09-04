package com.empirefx.fxbo_countries.processors;

import com.empirefx.fxbo_countries.commonlib.models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.empirefx.fxbo_countries.commonlib.exceptions.UnsuccessfullException;
import com.empirefx.fxbo_countries.models.consumer.response.StatusDetails;
import com.empirefx.fxbo_countries.models.provider.ProviderApiResponse;
import jakarta.annotation.PostConstruct;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;



import static com.empirefx.fxbo_countries.commonlib.enums.ErrorCodesEnum.EHF1000;
import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.*;
@Component
public class SuccessResponseGeneratorProcessor implements Processor {

    private Gson gson;

    @PostConstruct
    public void postConstruct() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        String jsonRedisResponse = exchange.getIn().getBody(String.class);

        boolean successfulResponse = jsonRedisResponse.contains("\"errorCode\": 0") || jsonRedisResponse.contains("\"errorCode\":0");

        if (!successfulResponse)
            throw new UnsuccessfullException();

        ProviderApiResponse providerApiResponse = gson.fromJson(jsonRedisResponse, ProviderApiResponse.class);

        StatusDetails statusDetails = StatusDetails.builder()
                .statusCode(providerApiResponse.getStatus())
                .errorCode(providerApiResponse.getErrorCode())
                .errorMessage(providerApiResponse.getErrorMessage())
                .ticketId(providerApiResponse.getTicketId()).build();

        RequestWrapper originalRequestWrapper = exchange.getProperty(ORIGINAL_REQUEST, RequestWrapper.class);
        ResponsePayload responsePayload = ResponsePayload.builder()
                .primaryData(originalRequestWrapper.getRequestPayload().getPrimaryData())
                .status(statusDetails).build();


        Item entry = new Item();
        List<Item> listOfItems = new ArrayList<>();
        EhfInfo ehfInfo = new EhfInfo();

        entry.setEhfRef(EHF1000.getCode());
        entry.setEhfDesc(EHF1000.getMessage());
        listOfItems.add(entry);
        ehfInfo.setItem(listOfItems);
        buildResponse(exchange, originalRequestWrapper, responsePayload, ehfInfo);
    }

    private static void buildResponse(Exchange exchange, RequestWrapper originalRequestWrapper,
                                      ResponsePayload responsePayload, EhfInfo ehfInfo) {
        Integer httpStatusCode = exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);
        ResponseHeader header = new ResponseHeader();
        header.setEhfInfo(ehfInfo);
        header.setMessageID(originalRequestWrapper.getHeader().getMessageID());
        header.setConversationID(exchange.getIn().getHeader(CONVERSATION_ID_HEADER, String.class));
        header.setTargetSystemID("NotAvailable");
        header.setChannelCode(originalRequestWrapper.getHeader().getChannelCode());
        header.setChannelName(originalRequestWrapper.getHeader().getChannelName());
        header.setRouteCode(originalRequestWrapper.getHeader().getRouteCode());
        header.setRouteName(originalRequestWrapper.getHeader().getRouteName());
        header.setServiceCode(originalRequestWrapper.getHeader().getServiceCode());
        header.setServiceName(originalRequestWrapper.getHeader().getServiceName());
        ResponseWrapper response = ResponseWrapper.builder().header(header).responsePayload(responsePayload).build();
        exchange.getIn().setBody(response, ResponseWrapper.class);
        if (httpStatusCode != null) {
            if (httpStatusCode == 200) {
                exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
            } else if (httpStatusCode == 201) {
                exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.CREATED.value());
            }
        } else {
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        }
        exchange.setProperty(APP_REQUEST_TYPE, "Outgoing Response Data -- (To Caller): {}");
        exchange.setProperty(APP_REQUEST, AppLogger.builder()
                .headerData(response.getHeader())
                .payloadData(response.getResponsePayload())
                .build());
    }
}