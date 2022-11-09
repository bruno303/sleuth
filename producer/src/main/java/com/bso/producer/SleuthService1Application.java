package com.bso.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SleuthService1Application {
	public static void main(String[] args) {
		SpringApplication.run(SleuthService1Application.class, args);
	}
}
