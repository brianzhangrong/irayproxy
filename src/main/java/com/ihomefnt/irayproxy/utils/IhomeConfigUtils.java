package com.ihomefnt.irayproxy.utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.base.Joiner;
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
	/**
	 * http client 基础配置
	 */
	private static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS).retryOnConnectionFailure(true).writeTimeout(1500, TimeUnit.MILLISECONDS)
			.build();
	/**
	 * basic 认证
	 */
	private static String credential = Credentials.basic(USERNAME, PASSWORD);
	/**
	 * request 认证头
	 */
	private static Request configRequest = new Request.Builder().url(CONFIG_URL).header("Authorization", credential)
			.get().build();
	private static Request envRequest = new Request.Builder().url(ENV_URL).header("Authorization", credential).get()
			.build();
	// static {
	// try {
	// Response response = client.newCall(envRequest).execute();
	// response.close();
	// } catch (IOException e) {
	// log.error("init error:{}",ExceptionUtils.getStackTrace(e));
	// }
	// }
	public final static String PROPERTY_NAME = "iray.server";
	public final static String PROPERTY_LOADBALANCE_METHOD = "loadbalance.method";
	public final static String PROPERTY_SPLIT_SYMBOL = ":";
	public final static String IP_SPLIT_SYMBOL = ",";
	public final static String CR = "\n";
	public final static Integer HTTP_OK = 200;

	static Timer timer = new Timer();
	static String configStr;
	static {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				configStr = null;
				commonParse(null);
				log.info("configStr:{}", configStr);
			}

		}, 10 * 1000, 1000 * 30 * 60);
	}

	public static void parseServeList(List<String> list) {
		commonParse(extractedIrayServer(list));
	}

	public static void parseLoadBalance(StringBuilder method) {
		commonParse(extractedLoadBalance(method));
	}

	private static ParseConfigCallBack extractedLoadBalance(StringBuilder method) {
		log.info("parse loadbalance:{}", method.toString());
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
			List<String> propertyValueList = Splitter.on(CR).splitToList(configStr);
			propertyValueList.forEach(propertyValueStr -> {
				if (parseConfigCallBack != null) {
					parseConfigCallBack.doParse(propertyValueStr);
				}
			});

		} catch (Exception e) {
			log.error("query server config error:{}", ExceptionUtils.getStackTrace(e));
		} finally {
		}
	}

	private static ParseConfigCallBack extractedIrayServer(List<String> list) {
		log.info("parse serverList:{}", Joiner.on(",").join(list));
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
