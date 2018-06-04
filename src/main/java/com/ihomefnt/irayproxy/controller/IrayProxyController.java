package com.ihomefnt.irayproxy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.ihomefnt.irayproxy.config.ServerListProperties;
import com.ihomefnt.irayproxy.utils.IhomeConfigUtils;

@RestController
public class IrayProxyController {

	@Autowired
	ServerListProperties properties;

	@RequestMapping(method = RequestMethod.GET, value = "hello")
	public String hello() {
		List<String> serverList = Lists.newArrayList();
		IhomeConfigUtils.parseServeList(serverList);
		return Joiner.on(",").join(serverList);
	}
}
