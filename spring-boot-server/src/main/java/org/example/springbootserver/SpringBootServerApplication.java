package org.example.springbootserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringBootServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootServerApplication.class, args);
	}

}
