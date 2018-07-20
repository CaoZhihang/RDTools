package com.kevin.rdtools.parse.factory;

import java.util.Map;

import com.kevin.rdtools.parse.ParseSource;
import com.kevin.rdtools.parse.impl.Configuration;
import com.kevin.rdtools.parse.impl.ConfigurationPaser;
import com.kevin.rdtools.parse.impl.YmlParser;

/**
 * 解析配置文件工厂
 * @author zhcao1
 *
 */
public class ParserFactory {

	/**
	 * 根据数据源类型，选择合适的解析类解析
	 * @param source 数据源
	 * @return
	 */
	public static Map<String,Object> autoParse(Object source){
		Map<String,Object> result = null;
		if (null == source){
			return result;
		}
		
		ParseSource parseSource = null;
		if (source.getClass().equals(String.class)){
			parseSource = new YmlParser();
		} else if(source.getClass().equals(Configuration.class)){
			parseSource = new ConfigurationPaser();
		}
		
		if (null != parseSource){
			result = parseSource.parse(source);
		}
		
		return result;
	}
	
}
