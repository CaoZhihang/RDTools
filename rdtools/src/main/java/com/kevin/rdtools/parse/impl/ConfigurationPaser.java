package com.kevin.rdtools.parse.impl;

import java.util.Map;

import com.kevin.rdtools.parse.ParseSource;

public class ConfigurationPaser implements ParseSource {

	public Map<String, Object> parse(Object source) {
		if (null == source){			
			return null;
		}
		
		Configuration config = (Configuration)source;
		Map<String, Object> result = config.toMap();
		return result;
	}

}
