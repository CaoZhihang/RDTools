package com.kevin.rdtools.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.ho.yaml.Yaml;
import org.springframework.stereotype.Component;

@Component
public class YmlUtils {

	public static Map loadFile(String fileName) throws FileNotFoundException{
		File file =  new File(fileName);
		Map<String, Object> result = Yaml.loadType(file, HashMap.class);
		return result;
	}
	
}
