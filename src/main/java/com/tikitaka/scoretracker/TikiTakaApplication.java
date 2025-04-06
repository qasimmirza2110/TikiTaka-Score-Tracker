package com.tikitaka.scoretracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TikiTakaApplication {
	public static void main(String[] args) {
		SpringApplication.run(TikiTakaApplication.class, args);
	}
}