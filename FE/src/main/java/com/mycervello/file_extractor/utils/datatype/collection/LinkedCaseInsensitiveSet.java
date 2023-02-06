package com.mycervello.file_extractor.utils.datatype.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class represents a case-insensitive set of strings with predictable iteration order.
 * 
 * @author Gennadiy Pervukhin
 * @created 24-12-2020
 */
public class LinkedCaseInsensitiveSet extends LinkedHashSet<String> {
	
	//
	//Constructors
	//
	public LinkedCaseInsensitiveSet() { }
	
	public LinkedCaseInsensitiveSet(Collection<? extends String> items) {
		super();
		this.addAll(items);
	}
	//

	//
	//Variables
	//
	private static final long serialVersionUID = 7917761226297944898L;
	//
	
	//
	//Private methods
	//
	/**
	 * The method tries to find items matching the passed ones and remove the existing items in 2
	 * ways:
	 * - REMOVE MODE: if removeIfMatchingFound = TRUE, remove existing items of the set, if they
	 * are in the passed collection;
	 * - RETAIN MODE: if removeIfMatchingFound = FALSE, remove existing items of the set, if they
	 * are in NOT the passed collection.
	 */
	private boolean removeItems(Collection<?> itemsForAnalysis, boolean removeIfMatchingFound) {
		
		if (itemsForAnalysis == null) {
			return false;
		}
		
		//STEP #1: prepare a case-insensitive set of items for analysis
		Set<String> setForAnalysis = SetUtils.newCaseInsensitiveSet();
		for (Object item : itemsForAnalysis) {
			if (item == null || item instanceof String) {
				setForAnalysis.add((String)item);
			}
		}
		
		//STEP #2: go through all existing items. If an item exists in the set for analysis, make a
		//decision to remove it or leave it according to the passed flag
		boolean isSetChanged = false;
		Iterator<String> iterator = this.iterator();
		while (iterator.hasNext()) {
			String existingItem = iterator.next();
			if (setForAnalysis.contains(existingItem) == removeIfMatchingFound) {
				iterator.remove();
				isSetChanged = true;
			}
		}
		return isSetChanged;
	}
	//
	
	//
	//Public methods
	//
	@Override
	public boolean add(String newItem) {
		if (!this.contains(newItem)) {
			return super.add(newItem);
		}
		return false;
	}
	
	@Override
	public boolean addAll(Collection<? extends String> newItems) {
		boolean isSetChanged = false;
		for (String newItem : newItems) {
			//try to add a single item, if it's possible
			boolean isItemAdded = this.add(newItem);
			if (isItemAdded) {
				isSetChanged = true;
			}
		}
		return isSetChanged;
	}
	
	/**
	 * Custom non-standard method for sets (it accepts String instead of Object).
	 */
	public boolean contains(String item) {
		
		if (item == null) {
			return this.contains(null);
		}
		
		for (String existingItem : this) {
			if (item.equalsIgnoreCase(existingItem)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean contains(Object item) {
		if (item != null && !(item instanceof String)) {
			return false;
		}
		return this.contains((String)item);
	}
	
	@Override
	public boolean containsAll(Collection<?> items) {
		for (Object item : items) {
			if (!this.contains(item)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean remove(Object itemToRemove) {
		
		if (itemToRemove == null) {
			return this.remove(null);
		}
		else if (!(itemToRemove instanceof String)) {
			return false;
		}
		
		final String stringItemToRemove = (String)itemToRemove;
		
		for (Iterator<String> iterator = this.iterator(); iterator.hasNext();) {
			String existingItem = iterator.next();
			if (stringItemToRemove.equalsIgnoreCase(existingItem)) {
				iterator.remove();
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean removeAll(Collection<?> itemsToRemove) {
		
		if (CollectionUtils.isEmpty(itemsToRemove)) {
			return false;
		}
		
		return this.removeItems(itemsToRemove, true);
	}
	
	@Override
	public boolean retainAll(Collection<?> itemsToRetain) {
		
		if (CollectionUtils.isEmpty(itemsToRetain)) {
			int sizeBeforeClear = this.size();
			this.clear();
			return (sizeBeforeClear > 0);
		}
		
		return this.removeItems(itemsToRetain, false);
	}
	//
}