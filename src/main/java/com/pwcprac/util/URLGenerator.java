package com.pwcprac.util;


import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.pwcprac.entity.RegisteredApp;
@Component
@Scope("prototype")
@PropertySource("classpath:/application.yml")
public class URLGenerator {
	//id
	private int loginUrlCode;
	private String loginUrl;
	private RegisteredApp app;
	private String port;
	private String host;
	private String appVIP;
	private String callbackURL;
	
	@Autowired
	public URLGenerator(@Value("${server.port}")String port,@Value("${server.host}")String host,@Value("${spring.application.name}")String appVIP){
		this.port=port;
		this.host=host;
		this.appVIP=appVIP;
	}
	
	//private Map<String, String>
	private String authURI="/oauth2/login";
	private int generatedCode;

	public String getDefaultRealURL() {
		return host+":"+port+authURI;
	}
	
	public String getDefaultVIPURL(){
		return appVIP+":"+port+authURI;
	}
	
	public String generateURL(){
		loginUrlCode = app.hashCode()+(int)Calendar.getInstance().getTimeInMillis();
		loginUrl = getDefaultRealURL()+"?urlcode="+loginUrlCode;
		return loginUrl;
	}

	public String getCallbackURL() {
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}

	public RegisteredApp getApp() {
		return app;
	}

	public void setApp(RegisteredApp app) {
		this.app = app;
	}
	//TODO generate session related code
	public int generateCode(){
		generatedCode = callbackURL.hashCode()+(int)Calendar.getInstance().getTimeInMillis();
		return generatedCode;
	}
	public int getCode(){
		return generatedCode;
	}

	public int getLoginUrlCode() {
		return loginUrlCode;
	}
	
	
}
