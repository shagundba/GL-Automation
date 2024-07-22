package com.example.GL_Automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GlAutomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlAutomationApplication.class, args);
	}

}
