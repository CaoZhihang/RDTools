package com.kevin.rdtools.parse;

import java.util.HashMap;
import java.util.Map;

public interface ParseSource {

	/**
	 * 解析数据源信息
	 * @param source 数据源
	 * @return 解析后的结果集
	 */
	public Map<String,Object> parse(Object source);
	
}
