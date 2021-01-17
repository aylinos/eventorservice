package com.eventor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(exclude = {DataSourceAutoConfiguration.class})
public class EventorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventorApplication.class, args);
		System.out.println("Hello Maven");
	}

}
