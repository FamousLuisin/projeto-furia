package com.furia.challenge.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		System.setProperty("PANDASCORE_TOKEN", dotenv.get("PANDASCORE_TOKEN"));

		SpringApplication.run(ApiApplication.class, args);
	}

}
