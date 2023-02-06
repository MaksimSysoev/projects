package com.mycervello.file_extractor.utils.datatype.collection;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
	
	//
	//Public static methods
	//
	public static <T> T getLastItem(List<T> list) {
		return CollectionUtils.getLastItem(list);
	}
	
	public static boolean isEmpty(List<?> list) {
		return (list == null || list.size() == 0);
	}
	
	public static <T> List<T> subList(List<T> list, int maxLength) {
		return subList(list, 0, Math.min(list.size(), maxLength));
	}
	
	/**
	 * The standard subList method returns a sub-list that depends on a source list. But this
	 * method clones the sub-list and returns an independent copy.
	 */
	public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
		if (list == null) { return null; }
		
		return (new ArrayList<T>(list.subList(fromIndex, toIndex)));
	}
	
	public static <T> List<T> cloneWithoutDuplicates(List<T> values) {
		if (values == null) {
			return (new ArrayList<T>());
		}
		
		List<T> uniqueItems = new ArrayList<>();
		for (T value : values) {
			if (!uniqueItems.contains(value)) {
				uniqueItems.add(value);
			}
		}
		return uniqueItems;
	}
	//
}
