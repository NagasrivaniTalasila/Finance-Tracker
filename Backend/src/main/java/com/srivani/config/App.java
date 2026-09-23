package com.srivani.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(App.class);
		Map<String, Object> config = new HashMap<>();
		config.put("server.port", 8000);
		application.setDefaultProperties(config);
		application.run(args);
	}
}
