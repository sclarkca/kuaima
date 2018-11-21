package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.kuaima,com.bstek.bdf3.security.decision.manager,com.bstek.bdf3.security.service")
@EnableAutoConfiguration
public class KMApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(KMApplication.class, args);
	}

}