package com.univice.cse364project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
@SpringBootApplication
public class Cse364ProjectApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(Cse364ProjectApplication.class, args);
	}

	// 이 부분 추가
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Cse364ProjectApplication.class);
	}

}
