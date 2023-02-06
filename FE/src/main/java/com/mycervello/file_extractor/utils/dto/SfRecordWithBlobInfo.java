package com.mycervello.file_extractor.utils.dto;

public class SfRecordWithBlobInfo extends SfRecord {
	
	//
	//Constructors
	//
	public SfRecordWithBlobInfo() { }
	
	public SfRecordWithBlobInfo(Integer id, String sfId, Integer blobSize)
	{
		super(id, sfId);
		this.blobSize = blobSize;
	}
	//

	//
	//Variables
	//
	private Integer blobSize;
	//
	
	//
	//Properties
	//
	public Integer getBlobSize() { return blobSize; }
	public void setBlobSize(Integer blobSize) { this.blobSize = blobSize; }
	//
	
	//
	//Public methods
	//
	@Override
	public String toString() {
		return "SfRecordWithBlobInfo [id=" + id + ", sfid=" + sfid + ", blobSize=" + blobSize + "]";
	}
	//
}