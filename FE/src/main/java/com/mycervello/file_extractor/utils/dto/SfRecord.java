package com.mycervello.file_extractor.utils.dto;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mycervello.file_extractor.utils.db.DbUtils;

public class SfRecord {
	
	//
	//Constructors
	//
	public SfRecord() {}
	
	public SfRecord(Integer id, String sfid) {
		this.id = id;
		this.sfid = sfid;
	}
	
	public SfRecord(SfRecord sfRecord) {
		this(sfRecord.getId(), sfRecord.getSfId());
	}
	
	public SfRecord(Map<String, Object> sfRecord) {
		this((Integer)sfRecord.get(DbUtils.FIELD_ID), (String)sfRecord.get(DbUtils.FIELD_SF_ID));
	}
	//

	//
	//Variables
	//
	protected Integer id;
	protected String sfid;
	//
	
	//
	//Properties
	//
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getSfId() {
		return sfid;
	}
	public void setSfId(String sfid) {
		this.sfid = sfid;
	}
	//
	


	//
	//Public methods
	//
	@Override
	public String toString() {
		return "[id=" + id + ", sfid=" + sfid + "]";
	}
	//
	
	//
	//Public static methods
	//
	/*public static List<String> extractSfIds(Collection<SfObject> sfObjects) {
		List<String> sfIds = new ArrayList<String>();
		if (sfObjects != null) {
			for (SfObject sfObject : sfObjects) {
				sfIds.add(sfObject.getSfId());
			}
		}
		
		return sfIds;
	}*/
	
	public static <ItemType extends SfRecord> Map<Integer, String> convertListToMapOfIds(
		Collection<ItemType> sfRecords)
	{
		Map<Integer, String> mapOfIds = new LinkedHashMap<Integer, String>();
		if (sfRecords != null) {
			for (SfRecord sfRecord : sfRecords) {
				mapOfIds.put(sfRecord.id, sfRecord.sfid);
			}
		}
		return mapOfIds;
	}
	
	public static <ItemType extends SfRecord> Map<String, SfRecord> createMapBySfId(
		Collection<ItemType> sfRecords)
	{
		Map<String, SfRecord> mapOfRecords = new LinkedHashMap<String, SfRecord>();
		if (sfRecords != null) {
			for (SfRecord sfRecord : sfRecords) {
				mapOfRecords.put(sfRecord.sfid, sfRecord);
			}
		}
		return mapOfRecords;
	}
	//
}