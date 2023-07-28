package com.footballbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FootballBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballBeApplication.class, args);
	}

}
