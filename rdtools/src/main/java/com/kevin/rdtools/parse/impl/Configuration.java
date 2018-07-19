package com.kevin.rdtools.parse.impl;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

	private Map<String,Object> config;
	
	public Configuration(){
		config = new HashMap<String, Object>();
	}
	
	public void set(String key,Object value){
		if (null != key){
			config.put(key, value);
		}
	}
	
	Map<String,Object> toMap(){
		return config;
	}
}
