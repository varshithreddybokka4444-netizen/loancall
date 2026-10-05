package com.varshith.LoanCall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoanCallApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanCallApplication.class, args);
	}
}