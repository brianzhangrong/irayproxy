package com.ihomefnt.irayproxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@RefreshScope
@Data
public class ServerListProperties {
	@Value("${iray.server}")
	String server;
	@Value("${loadbalance.method}")
	String method;
}
