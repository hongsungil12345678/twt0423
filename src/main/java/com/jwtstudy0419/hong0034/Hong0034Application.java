package com.jwtstudy0419.hong0034;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Hong0034Application {

	public static void main(String[] args) {
		SpringApplication.run(Hong0034Application.class, args);
	}

}
