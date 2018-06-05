package com.ihomefnt.irayproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
// @EnableAtlas
public class IrayProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrayProxyApplication.class, args);
	}
}
