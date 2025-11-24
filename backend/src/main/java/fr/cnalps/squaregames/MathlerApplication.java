package fr.cnalps.squaregames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class MathlerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MathlerApplication.class, args);
    }
}