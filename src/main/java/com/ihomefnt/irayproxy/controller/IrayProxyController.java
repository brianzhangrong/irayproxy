package com.ihomefnt.irayproxy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.ihomefnt.irayproxy.loadbalance.LoadBalanceConfig;
import com.ihomefnt.irayproxy.loadbalance.UnsupportLoadBalanceException;
import com.ihomefnt.irayproxy.utils.IhomeConfigUtils;

@RestController
public class IrayProxyController {

	@RequestMapping(method = RequestMethod.GET, value = "irayCloud")
	public String hello(HttpServletRequest request) throws UnsupportLoadBalanceException {
		List<String> serverList = Lists.newArrayList();
		IhomeConfigUtils.parseServeList(serverList);
		StringBuilder method = new StringBuilder();
		IhomeConfigUtils.parseLoadBalance(method);
		return LoadBalanceConfig.parseLoadBalanceConfig(method.toString(), request).select(serverList);
	}
}
