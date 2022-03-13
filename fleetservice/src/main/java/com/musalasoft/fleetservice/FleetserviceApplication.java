package com.musalasoft.fleetservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FleetserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FleetserviceApplication.class, args);
	}

}
