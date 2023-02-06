package com.mycervello.file_extractor.utils.datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StringUtils {
	
	//
	//Constants
	//
	public static final String EMPTY = "";
	
	public static final String NEW_LINE = "\r\n";
	public static final String DOUBLE_LINE = NEW_LINE + NEW_LINE;
	//
	
	//
	//Public static methods
	//
	public static boolean isEmpty(String sourceString) {
		return (sourceString == null || sourceString.isEmpty());
	}
	
	public static boolean isNotEmpty(String sourceString) {
		return (!isEmpty(sourceString));
	}
	
	public static String emptyIfNull(String value) {
		return (value != null ? value : EMPTY);
	}
	
	public static String toLowerCase(String sourceString) {
		return (sourceString != null ? sourceString.toLowerCase() : null);
	}
	
	public static List<String> toLowerCase(Collection<String> sourceStrings) {
		List<String> formattedStrings = new ArrayList<>();
		for (String sourceString : sourceStrings) {
			formattedStrings.add(toLowerCase(sourceString));
		}
		return formattedStrings;
	}
	
	public static String truncate(String sourceString, int limit) {
		if (sourceString != null && limit > -1 && sourceString.length() > limit) {
			return sourceString.substring(0, limit);
		}
		return sourceString;
	}
	
	public static void removeBlankValues(Collection<String> collection) {
		if (collection != null) {
			collection.removeAll(Arrays.asList(null, EMPTY));
		}
	}
	
	public static String[] split(String sourceString, String separator) {
		return (sourceString != null ? sourceString.split(separator) : new String[0]);
	}
	
	public static String join(String[] strings, String separator) {
		separator = emptyIfNull(separator);
		return (strings != null ? String.join(separator, strings) : EMPTY);
	}
	
	public static String join(Collection<String> strings, String separator) {
		separator = emptyIfNull(separator);
		return (strings != null ? String.join(separator, strings) : EMPTY);
	}
	
	public static boolean equal(String value1, String value2, boolean isCaseSensitive) {
		if (value1 == null || value2 == null) {
			return (value1 == value2);
		}
		return (isCaseSensitive ? value1.equals(value2) : value1.equalsIgnoreCase(value2));
	}
	
	public static boolean matchOneIgnoreCase(String sourceString, String... values) {
		
		if (sourceString == null) { return false; }
		
		for (String value : values) {
			if (sourceString.equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean startsWithAny(String sourceString, String... startValues) {
		
		if (sourceString == null) { return false; }
		
		for (String startValue : startValues) {
			if (sourceString.startsWith(startValue)) {
				return true;
			}
		}
		return false;
	}
	
	public static String[] toArray(Collection<String> collection) {
		return (collection != null ? collection.toArray(new String[0]) : new String[0]);
	}
	
	/*public static String decodeBase64(String base64String) {
		return (base64String != null ? new String(java.util.Base64.getDecoder().decode(base64String)) : null);
	}*/
	//
}
