package com.mycervello.file_extractor.utils.datatype.collection;

import java.util.Map;
import java.util.TreeMap;

public class MapUtils
{
	//
	//Public static methods
	//
	public static <V> TreeMap<String, V> newCaseInsensitiveMap() {
		return (new TreeMap<>(String.CASE_INSENSITIVE_ORDER));
	}
	
	public static <V> TreeMap<String, V> newCaseInsensitiveMap(Map<String, V> sourceMap) {
		TreeMap<String, V> map = newCaseInsensitiveMap();
		if (sourceMap != null) {
			map.putAll(sourceMap);
		}
		return map;
	}
	
	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.size() == 0);
	}
	
	public static boolean isNotEmpty(Map<?, ?> map) {
		return (!isEmpty(map));
	}
	//
}
