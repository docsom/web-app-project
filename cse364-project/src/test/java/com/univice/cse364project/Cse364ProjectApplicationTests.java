package com.univice.cse364project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootTest
class Cse364ProjectApplicationTests extends SpringBootServletInitializer {

	@Test
	void contextLoads() {
	}
	public static void main(String[] args) {
		SpringApplication.run(Cse364ProjectApplication.class, args);
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(new Class[]{Cse364ProjectApplication.class});
	}
}
