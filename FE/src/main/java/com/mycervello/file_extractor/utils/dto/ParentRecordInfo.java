package com.mycervello.file_extractor.utils.dto;

/**
 * This class encapsulates details about sf parent record.
 * 
 * @author Maksim Sysoev
 * @created 12-09-2022
 */
public class ParentRecordInfo {
	
	//
	//Variables
	//
	private String sfid;
	private String name;
	//

	
	//
	//Properties
	//
	public String getSfId() { return sfid; }
	public void setSfId(String sfId) { this.sfid = sfId;}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }	
	//
}
