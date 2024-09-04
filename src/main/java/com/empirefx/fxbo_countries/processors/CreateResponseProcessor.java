package com.empirefx.fxbo_countries.processors;

import com.empirefx.fxbo_countries.commonlib.models.AppLogger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.APP_REQUEST;
import static com.empirefx.fxbo_countries.commonlib.constants.ConstantsCommons.APP_REQUEST_TYPE;


@Component
public class CreateResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.setProperty(APP_REQUEST_TYPE, "Provider Response Data -------(From Backend): {}");
        exchange.setProperty(APP_REQUEST, AppLogger.builder()
                .headerData(exchange.getIn().getHeaders())
                        .payloadData(exchange.getIn().getBody())
                .build());
    }
}