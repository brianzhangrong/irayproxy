package com.ihomefnt.irayproxy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Joiner;
import com.ihomefnt.irayproxy.config.ServerListProperties;

@RestController
public class IrayProxyController {
	
	@Autowired
	ServerListProperties  properties;
	
	@RequestMapping(method=RequestMethod.GET,value="hello")
	public String hello(){
		return Joiner.on(",").join(properties.getServer());
	}
}
