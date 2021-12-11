package com.untilled.roadcapture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RoadcaptureApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoadcaptureApplication.class, args);
	}

}
