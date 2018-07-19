package com.kevin.rdtools.parse.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.kevin.rdtools.parse.ParseSource;

public class YmlParser implements ParseSource {

	public Map<String, Object> parse(Object source) {
		if (null == source){
			return null;
		}
		
		Map<String ,Object> tmp = (Map<String, Object>) source;
		Map<String ,Object> result = new HashMap<String, Object>();
		parseToStringValue(tmp,null,result);
		
		return result;
	}
	
	private void parseToStringValue(Map<String, Object> source, String subKey, Map<String, Object> result){
		if (null == source) {
			return ;
		}
		
		for (Map.Entry<String, Object> entry : source.entrySet()){
			String key = entry.getKey();
			Object value = entry.getValue();
			if ("class java.util.HashMap".equals(value.getClass())){
				if (null == subKey){
					subKey = key;
				} else{
					subKey += "."+key;
				}
				
				parseToStringValue((Map<String, Object>)value, subKey, result);
			} else{
				if (null == subKey){					
					result.put(key, value);
				} else{
					result.put(subKey+"."+key,value);
				}
			}
		}
	}

}
