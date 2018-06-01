package com.ihomefnt.irayproxy.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import lombok.Data;


@Configuration
@ConfigurationProperties(prefix="iray")
@Data
public class ServerListProperties {
	List<String> server = Lists.newArrayList();
}
