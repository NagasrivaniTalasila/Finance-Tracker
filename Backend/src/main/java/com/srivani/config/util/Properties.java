package com.srivani.config.util;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@PropertySource({ "application.properties", "procedures.properties" })
@RequiredArgsConstructor
@Component
public class Properties {

	private final Environment env;

	public String getProperty(String input) {
		return env.getProperty(input);
	}
}
