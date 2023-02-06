package com.mycervello.file_extractor.utils.datatype.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.mycervello.file_extractor.utils.datatype.StringUtils;

/**
 * This class provides utilities for sets.
 * 
 * @author Gennadiy Pervukhin
 * @created 14-12-2017
 */
public class SetUtils {
	
	//
	//Public static methods
	//
	public static Set<String> newCaseInsensitiveSet() {
		return (new TreeSet<String>(String.CASE_INSENSITIVE_ORDER));
	}
	
	public static Set<String> newCaseInsensitiveSet(Collection<String> values) {
		Set<String> set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		set.addAll(values);
		return set;
	}
	
	public static <T> Set<T> newFromSourceAndRemove(Collection<T> source,
		Collection<T> itemsToRemove)
	{
		source = (source != null ? source : new ArrayList<>());
		itemsToRemove = (itemsToRemove != null ? itemsToRemove : new ArrayList<>());
		
		Set<T> resultSet = new HashSet<>(source);
		resultSet.removeAll(itemsToRemove);
		return resultSet;
	}
	
	public static void removeBlankValues(Set<String> set) {
		if (set != null) {
			set.remove(null);
			set.remove(StringUtils.EMPTY);
		}
	}
	
	public static <T> boolean containsAny(Set<T> set, Collection<T> itemsToCheck) {
		if (set == null || itemsToCheck == null) {
			return false;
		}
		
		for (T item : itemsToCheck) {
			if (set.contains(item)) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean containsOthers(Set<T> set, Collection<T> itemsToCheck) {
		if (set == null) { return false; }
		else if (itemsToCheck == null) { return (set.size() > 0); }
		
		Set<T> setDifference = new HashSet<>(set);
		setDifference.removeAll(itemsToCheck);
		return (setDifference.size() > 0);
	}
	
	public static boolean containsIgnoreCase(Set<String> set, String stringToCheck) {
		if (set == null || stringToCheck == null) {
			return false;
		}

		for (String string : set) {
			if (string.equalsIgnoreCase(stringToCheck)) { return true; }
		}
		return false;
	}
	
	@SafeVarargs
	public static <T> Set<T> joinSetsIntoNew(Set<T>... sets) {
		Set<T> resultSet = new HashSet<T>();
		for (Set<T> set : sets) {
			resultSet.addAll(set);
		}
		return resultSet;
	}
	//
}