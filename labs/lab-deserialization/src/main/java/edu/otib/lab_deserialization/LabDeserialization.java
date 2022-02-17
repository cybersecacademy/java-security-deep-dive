package edu.otib.lab_deserialization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LabDeserialization {

    public static void main(String[] args) {
        System.getProperties().setProperty("org.apache.commons.collections.enableUnsafeSerialization", "true");
        SpringApplication.run(LabDeserialization.class, args);
    }
}
