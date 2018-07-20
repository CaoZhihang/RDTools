package com.kevin.rdtools.parse.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ho.yaml.Yaml;

import com.kevin.rdtools.parse.ParseSource;

public class YmlParser implements ParseSource {

	public Map<String, Object> parse(Object source) {
		if (null == source){
			return null;
		}
		
		String fileName = (String) source;
		File dumpFile = new File(fileName);
		System.out.println(source);
		
		Map<String, Object> tmp;
		Map<String ,Object> result = null;
		try {
			tmp = Yaml.loadType(dumpFile, HashMap.class);
			result = new HashMap<String, Object>();
			parseToStringValue(tmp,null,result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
		
		return result;
	}
	
	private void parseToStringValue(Map<String, Object> source, String subKey, Map<String, Object> result){
		if (null == source) {
			return ;
		}
		
		for (Map.Entry<String, Object> entry : source.entrySet()){
			String before = subKey;
			String key = entry.getKey();
			Object value = entry.getValue();
			if (HashMap.class.equals(value.getClass())){
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
			subKey = before;
		}
	}

}
