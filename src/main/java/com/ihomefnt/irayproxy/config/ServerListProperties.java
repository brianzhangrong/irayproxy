package com.ihomefnt.irayproxy.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import lombok.Data;


@Configuration
@RefreshScope
@Data
public class ServerListProperties {
	@Value("${iray.server}")
	String  server;
	@Value("${user}")
	String user;
}
