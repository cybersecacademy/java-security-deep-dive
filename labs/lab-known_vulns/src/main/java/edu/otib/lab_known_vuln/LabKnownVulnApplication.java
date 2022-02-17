package edu.otib.lab_known_vuln;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class LabKnownVulnApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabKnownVulnApplication.class, args);
	}
}
