package com.y.javachat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class JavaChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaChatApplication.class, args);
	}

}
