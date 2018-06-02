package com.ihomefnt.irayproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigServer
public class IrayProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrayProxyApplication.class, args);
	}
}
