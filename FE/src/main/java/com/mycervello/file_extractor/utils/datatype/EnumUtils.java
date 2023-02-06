package com.mycervello.file_extractor.utils.datatype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class provides utilities for enumerations.
 * 
 * @author Gennadiy Pervukhin
 * @created 23-10-2019
 */
public class EnumUtils {
	
	//
	//Public static methods
	//
	/**
	 * The method tries to parse the string value into enumeration and returns the passed default
	 * value, if parsing failed.
	 * 
	 * @param enumInString
	 * @param defaultValue - must NOT be null
	 * @return
	 */
	public static <T extends Enum<T>> T parseOrDefault(String enumInString, T defaultValue) {
		try {
			if (StringUtils.isNotEmpty(enumInString) && defaultValue != null) {
				return Enum.valueOf(defaultValue.getDeclaringClass(), enumInString);
			}
		}
		catch (Exception error) { /*do nothing*/ }
		
		return defaultValue;
	}
	
	public static <T extends Enum<T>> String[] extractNames(Collection<T> enumValues) {
		List<String> names = new ArrayList<>();
		for (T enumValue : enumValues) {
			names.add(enumValue.name());
		}
		return names.toArray(new String[0]);
	}
	
	public static <T extends Enum<T>> String[] extractNames(Class<T> enumClass) {
		//- get all Enum constants
		T[] enumValues = enumClass.getEnumConstants();
		//- extract names of Enum constants
		return extractNames(Arrays.asList(enumValues));
	}
	
	public static String formatEnumLabel(String enumName) {
		//replace underscores by white spaces
		return enumName.replace("_", " ");
	}
	//
}