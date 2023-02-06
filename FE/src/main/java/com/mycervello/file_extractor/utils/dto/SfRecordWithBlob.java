package com.mycervello.file_extractor.utils.dto;

import java.sql.Blob;

public class SfRecordWithBlob extends SfRecordWithBlobInfo {
	
	//
	//Constructors
	//
	public SfRecordWithBlob() { }
	
	public SfRecordWithBlob(Integer id, String sfId, Integer blobSize)
	{
		super(id, sfId, blobSize);
	}
	//

	//
	//Variables
	//
	private String fileName;
	private String extension;
	private Blob dbBlob;
	private String versionContainerIdField;
	private String versionNumber;
	//
	
	//
	//Properties
	//
	public String getFileName() { return fileName; }
	public void setFileName(String fullFileName) { this.fileName = fullFileName; }
	
	public String getExtension() { return extension; }
	public void setExtension(String extension) { this.extension = extension;}
	
	public Blob getDbBlob() { return dbBlob; }
	public void setDbBlob(Blob dbBlob) { this.dbBlob = dbBlob; }
	
	public String getVersionContainerIdField() { return versionContainerIdField; }
	public void setVersionContainerIdField(String contentDocumentId) {
		this.versionContainerIdField = contentDocumentId;
	}
	
	public String getVersionNumber() { return versionNumber; }
	public void setVersionNumber(String versionNumber) { this.versionNumber = versionNumber; }
	//
}
