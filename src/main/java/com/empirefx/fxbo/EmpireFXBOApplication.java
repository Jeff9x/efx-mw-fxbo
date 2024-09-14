package com.empirefx.fxbo;

import com.empirefx.fxbo.commonlib.configurations.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.empirefx")
@EnableConfigurationProperties(value = AppConfiguration.class)

public class EmpireFXBOApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpireFXBOApplication.class, args);
	}


}
