package edu.otib.lab_known_vuln;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class LabMisconfigurationApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabMisconfigurationApplication.class, args);
	}
}
