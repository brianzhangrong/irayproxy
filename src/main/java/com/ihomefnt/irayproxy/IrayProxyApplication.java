package com.ihomefnt.irayproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.stream.config.BinderFactoryConfiguration;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.cloud.stream.config.ChannelBindingAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigServer
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.ihomefnt.irayproxy"})
public class IrayProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrayProxyApplication.class, args);
	}
}
