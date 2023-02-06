package com.mycervello.file_extractor.utils.datatype.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class provides utilities for collections.
 * 
 * @author Gennadiy Pervukhin
 * @created 08-11-2017
 */
public class CollectionUtils {
	
	//
	//Public static methods
	//
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.size() == 0);
	}
	
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}
	
	public static <T> T getFirstItem(Collection<T> collection) {
		return (collection == null || collection.isEmpty() ? null : collection.iterator().next());
	}
	
	public static <T> T getLastItem(Collection<T> collection) {
		if (collection == null || collection.isEmpty()) {
			return null;
		}
		
		List<T> list = (collection instanceof List ? (List<T>)collection
			: new ArrayList<T>(collection));
		return list.get(list.size() - 1);
	}
	
	/**
	 * The method clones the passed collection saving order of its elements and removes duplicates.
	 */
	public static <T> LinkedHashSet<T> cloneWithoutDuplicates(Collection<T> sourceCollection) {
		LinkedHashSet<T> uniqueCollection = new LinkedHashSet<T>();
		uniqueCollection.addAll(sourceCollection);
		return uniqueCollection;
	}
	
	public static void removeItems(Collection<String> sourceCollection, String[] itemsToRemove,
		boolean ignoreCase)
	{
		if (itemsToRemove == null || itemsToRemove.length == 0) {
			return;
		}
		
		//- prepare the set of items that should be removed from the source collection
		Set<String> setToRemove = (ignoreCase ? SetUtils.newCaseInsensitiveSet()
			: new HashSet<>());
		setToRemove.addAll(Arrays.asList(itemsToRemove));
		
		//- remove the items from the source collection. NOTE: the removeAll() method respects
		//Comparator in the passed collection
		sourceCollection.removeAll(setToRemove);
	}
	//
}