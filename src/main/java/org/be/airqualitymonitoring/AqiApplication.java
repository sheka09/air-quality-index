package org.be.airqualitymonitoring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AqiApplication {

	private static final Logger LOGGER = LogManager.getLogger(AqiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AqiApplication.class, args);
	}
	
}
