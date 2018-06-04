package com.ihomefnt.irayproxy.loadbalance;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

public class LoadBalanceConfig {

	private static AtomicLong index = new AtomicLong(0);
	public final static String LOADBALANCE_RIBBON = "ribbon";
	public final static String LOADBALANCE_RANDOM = "random";
	public final static String LOADBALANCE_HASH = "hash";

	public static LoadBalance parseLoadBalanceConfig(String method, HttpServletRequest request)
			throws UnsupportLoadBalanceException {
		method = method.trim();
		if (LOADBALANCE_RIBBON.equals(method)) {
			return (serverList) -> {
				String server = null;
				long now = index.get();
				if (now >= serverList.size()) {
					index.set(0);
					now = 0;
				}
				server = serverList.get((int) now);
				if (index.compareAndSet(now, now + 1)) {

				}
				return server;
			};
		} else if (LOADBALANCE_RANDOM.equals(method)) {
			return (serverList) -> {
				String server = null;
				Random random = new Random();
				int randomPos = random.nextInt(serverList.size());
				return serverList.get(randomPos);
			};
		} else if (LOADBALANCE_HASH.equals(method)) {
			return (serverList) -> {
				String remoteIp = getRemoteIp(request);
				int hashCode = remoteIp.hashCode();
				int serverListSize = serverList.size();
				int serverPos = hashCode % serverListSize;
				if (serverPos < 0) {
					serverPos = 0 - serverPos;
				}
				return serverList.get(serverPos);
			};
		}
		throw new UnsupportLoadBalanceException("unSupportLoadBalance " + method);
	}

	public static String getRemoteIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		final String[] arr = ip.split(",");
		for (final String str : arr) {
			if (!"unknown".equalsIgnoreCase(str)) {
				ip = str;
				break;
			}
		}
		return ip;
	}

}
