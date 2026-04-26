package com.conduit.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.conduit")
public class ConduitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConduitApplication.class, args);
	}

}
