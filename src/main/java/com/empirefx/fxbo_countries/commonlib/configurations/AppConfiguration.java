package com.empirefx.fxbo_countries.commonlib.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("efx-values")
@Data
public class AppConfiguration {

//    private HeaderValuesConfig headerValues;
    private VascoConfig vasco;


    @Data
    public static class VascoConfig {
        private String cred;
    }
//    @Data
//    public static class HeaderValuesConfig {
//        private String serviceCode;
//        private String serviceName;
//        private List<String> channelCode;
//        private String channelName;
//        private String routeCode;
//        private String routeName;
//        private String serviceMode;
//        private String minorServiceVersion;
//    }

}
