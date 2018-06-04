package com.ihomefnt.irayproxy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;
import com.ihomefnt.irayproxy.loadbalance.LoadBalanceConfig;
import com.ihomefnt.irayproxy.loadbalance.UnsupportLoadBalanceException;
import com.ihomefnt.irayproxy.utils.IhomeConfigUtils;

@RestController
public class IrayProxyController {

	RateLimiter rateLimiter = RateLimiter.create(1000);

	@RequestMapping(method = RequestMethod.GET, value = "irayCloud")
	public String irayCloudProxy(HttpServletRequest request) throws UnsupportLoadBalanceException {
		List<String> serverList = Lists.newArrayList();
		IhomeConfigUtils.parseServeList(serverList);
		StringBuilder method = new StringBuilder();
		IhomeConfigUtils.parseLoadBalance(method);
		if (rateLimiter.tryAcquire()) {
			return LoadBalanceConfig.parseLoadBalanceConfig(method.toString(), request).select(serverList);
		} else {
			return "overLoad";
		}
	}
}
