package com.ihomefnt.irayproxy.utils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

@Slf4j
public class IhomeConfigUtils {
	/**
	 * 配置地址
	 */
	public final static String CONFIG_URL="http://127.0.0.1:8888/master/ihomeConfig-prd.properties";
	/**
	 * 环境地址
	 */
	public final static String ENV_URL="http://127.0.0.1:8888/env";
	/**
	 * 账号
	 */
	public final static String USERNAME="admin";
	/**
	 * 密码
	 */
	public final static String PASSWORD="admin";
	/**
	 * http client 基础配置
	 */
	private static OkHttpClient client = new OkHttpClient.Builder() .connectTimeout(30, TimeUnit.SECONDS)
	        .readTimeout(30, TimeUnit.SECONDS).retryOnConnectionFailure(true).writeTimeout(1500, TimeUnit.MILLISECONDS)
		    .build();
	/**
	 * basic 认证
	 */
	private static  String credential = Credentials.basic(USERNAME, PASSWORD);
	/**
	 * request 认证头
	 */
	private static Request configRequest = new Request.Builder()
             .url(CONFIG_URL).header("Authorization",credential ).get()
             .build();
	private static Request envRequest = new Request.Builder()
            .url(ENV_URL).header("Authorization",credential ).get()
            .build();
//	static {
//		 try {
//			Response response = client.newCall(envRequest).execute();
//			response.close();
//		} catch (IOException e) {
//			log.error("init error:{}",ExceptionUtils.getStackTrace(e));
//		}
//	}
	public final static String PROPERTY_NAME="iray.server";
	public final static String PROPERTY_SPLIT_SYMBOL=":";
	public final static String IP_SPLIT_SYMBOL=",";
	public final static String CR="\r\n";
	public static List<String> getServerList(){
		List<String> list = Lists.newArrayList();
		 try {
             Response response = client.newCall(configRequest).execute();
             if (response.isSuccessful()) {
            	 if(response.code()==200) {
            		 
		             String configStr = response.body().string();
		             List<String> propertyValueList = Splitter.on(CR).splitToList(configStr);
		             propertyValueList.forEach(propertyValueStr->{
		            	 if(propertyValueStr.contains(PROPERTY_NAME)) {
		            		 List<String> propertyValuePairList = Splitter.on(PROPERTY_SPLIT_SYMBOL).splitToList(propertyValueStr);
		            		 String property =propertyValuePairList.get(0);
		            		 String value =propertyValuePairList.get(1);
		            		 if(StringUtils.isNotBlank(value)) {
		            			 list .addAll(Splitter.on(IP_SPLIT_SYMBOL).splitToList(value));
		            		 }
		            	 }
	             });
            	 }else {
            		 log.info("get config code:{},",response.code());
            	 }
	             response.close();
             }
             
         } catch (Exception e) {
        	 log.error("query server config error:{}",ExceptionUtils.getStackTrace(e));
         }finally{
         }
		 return list;
	}
	public static void main(String[] args) {
		List<String> serverList = IhomeConfigUtils.getServerList();
		System.out.println(Joiner.on(",").join(serverList));
	}
	
}
