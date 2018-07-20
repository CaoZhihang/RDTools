package com.kevin.rdtools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.ho.yaml.Yaml;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kevin.rdtools.parse.factory.ParserFactory;
import com.kevin.rdtools.parse.impl.Configuration;

@SpringBootApplication
public class RDTools {

	private static Map<String, Object> values;

	public static void loadConfig() throws FileNotFoundException {
		String fileName = RDTools.class.getClassLoader().getResource("application.yml").getPath();

		File dumpFile = new File(fileName);
		
		values = ParserFactory.autoParse(fileName);

		Map father = Yaml.loadType(dumpFile, HashMap.class);
		for (Object key : father.keySet()) {
			System.out.println(father.get(key).getClass());
			System.out.println(key + ":\t" + father.get(key).toString());
		}
	}
	
	public static void loadConfig(Object source){
		values = ParserFactory.autoParse(source);
	}

	public static Object get(String key){
		if (null != values){
			return values.get(key);
		} else{
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		try {
			loadConfig();
			System.out.println(get("redis.maxActive"));
			if (null != values){
				for (Map.Entry<String, Object> entry : values.entrySet()){
					System.out.println(entry.getKey()+" : "+entry.getValue());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
//		Configuration config = new Configuration();
//		System.out.println(Configuration.class.equals(config.getClass()));
		
	}

}
