package com.cowin.VaccineSchedular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class VaccineSchedularApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccineSchedularApplication.class, args);
	}

}
