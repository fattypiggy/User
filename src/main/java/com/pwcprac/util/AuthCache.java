package com.pwcprac.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Component;

@Component
public class AuthCache {
	//key : LoginUrlCode
	private Map<Integer, URLGenerator> map;
	Timer timer = new Timer();
	long timeout = 20000; // milliseconds
	public void addEntryWithTimeOuts(Integer loginUrlCode,URLGenerator urlGenerator){
		map.put(loginUrlCode, urlGenerator);
		timer.schedule(new TimerTask() {
	        @Override
	        public void run() {
	            actionAfterTimeout(loginUrlCode);
	        }
	    }, timeout);
	}
	public AuthCache(){
		map = new HashMap<Integer, URLGenerator>();
	}
	
	public Map<Integer, URLGenerator> getAuthCache() {
		return map;
	}
	
	void actionAfterTimeout(Integer key) {
	    //do something
	    map.remove(key);
	}

}
