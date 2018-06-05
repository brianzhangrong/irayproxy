package com.ihomefnt.irayproxy.utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.base.Splitter;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class IhomeConfigUtils {
	/**
	 * 配置地址
	 * 
	 */
	public final static String CONFIG_URL = "http://127.0.0.1:8888/master/ihomeConfig-prd.properties";
	/**
	 * 环境地址
	 */
	public final static String ENV_URL = "http://127.0.0.1:8888/env";
	/**
	 * 账号
	 */
	public final static String USERNAME = "admin";
	/**
	 * 密码
	 */
	public final static String PASSWORD = "admin";
	public final static String PROPERTY_NAME = "iray.server";
	public final static String PROPERTY_LOADBALANCE_METHOD = "loadbalance.method";
	public final static String PROPERTY_SPLIT_SYMBOL = ":";
	public final static String IP_SPLIT_SYMBOL = ",";
	public final static String CR = "\n";
	public final static Integer HTTP_OK = 200;
	/**
	 * http client 基础配置
	 */
	static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS).retryOnConnectionFailure(true).writeTimeout(1500, TimeUnit.MILLISECONDS)
			.build();
	/**
	 * basic 认证
	 */
	static String credential = Credentials.basic(USERNAME, PASSWORD);
	/**
	 * request 认证头
	 */
	static Request configRequest = new Request.Builder().url(CONFIG_URL).header("Authorization", credential).get()
			.build();

	static String configStr;
	public static Timer timer = new Timer();
	{
		try {
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					configStr = null;
					commonParse(null);
					log.warn("configStr:{}", configStr);
				}

			}, 0, 1000 * 10);
		} catch (Exception e) {
			log.error("loop time error:{}", ExceptionUtils.getStackTrace(e));
		}
	}

	public static void parseServeList(List<String> list) {
		commonParse(extractedIrayServer(list));
	}

	public static void parseLoadBalance(StringBuilder method) {
		commonParse(extractedLoadBalance(method));
	}

	private static ParseConfigCallBack extractedLoadBalance(StringBuilder method) {
		return (propertyValueStr) -> {
			if (propertyValueStr.contains(PROPERTY_LOADBALANCE_METHOD)) {
				List<String> propertyValuePairList = Splitter.on(PROPERTY_SPLIT_SYMBOL).splitToList(propertyValueStr);
				String property = propertyValuePairList.get(0);
				String value = propertyValuePairList.get(1);
				if (StringUtils.isNotBlank(value)) {
					method.append(value);
				}
			}
		};
	}

	public static void commonParse(ParseConfigCallBack parseConfigCallBack) {
		try {
			if (StringUtils.isBlank(configStr)) {
				synchronized (configRequest) {
					Response response = client.newCall(configRequest).execute();
					if (response.isSuccessful()) {
						if (response.code() == HTTP_OK) {
							configStr = response.body().string();
							response.close();
						} else {
							log.info("get config code:{},", response.code());
						}
					}
				}
			}
			if (StringUtils.isNotBlank(configStr)) {
				List<String> propertyValueList = Splitter.on(CR).splitToList(configStr);
				propertyValueList.forEach(propertyValueStr -> {
					if (parseConfigCallBack != null && StringUtils.isNotBlank(propertyValueStr)) {
						parseConfigCallBack.doParse(propertyValueStr);
					}
				});
			}
		} catch (Exception e) {
			log.error("query server config error:{}", ExceptionUtils.getStackTrace(e));
		} finally {
		}
	}

	private static ParseConfigCallBack extractedIrayServer(List<String> list) {
		return (propertyValueStr) -> {
			if (propertyValueStr.contains(PROPERTY_NAME)) {
				List<String> propertyValuePairList = Splitter.on(PROPERTY_SPLIT_SYMBOL).splitToList(propertyValueStr);
				String property = propertyValuePairList.get(0);
				String value = propertyValuePairList.get(1);
				if (StringUtils.isNotBlank(value)) {
					list.addAll(Splitter.on(IP_SPLIT_SYMBOL).splitToList(value));
				}
			}
		};
	}

	@FunctionalInterface
	public interface ParseConfigCallBack {
		void doParse(String t);
	}

}
