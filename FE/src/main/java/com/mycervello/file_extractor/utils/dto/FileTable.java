package com.mycervello.file_extractor.utils.dto;

/**
 * This class contains information about fields of file tables
 * 
 * @author Maksim Sysoev
 * @created 12-09-2022
 */
public enum FileTable {
	
	//
	//Enum values
	//
	ATTACHMENT("attachment", "body", "bodylength", "name", null, null, null),
	CONTENTVERSION("contentversion", "versiondata", "contentsize", "title", "fileextension",
		"versionnumber", "contentdocumentid");
	//
	
	//
	//Constructors
	//
	private FileTable(String name, String blobField, String blobSizeField, String nameField, 
		String extensionField, String versionNumberField, String versionContainerIdField)
	{
		this.name = name;
		this.blobField = blobField;
		this.blobSizeField = blobSizeField;
		this.nameField = nameField;
		this.extensionField = extensionField;
		this.versionNumberField = versionNumberField;
		
		//Field name that stores the common id for all versions of document, which groups different
		//versions of the same document
		this.versionContainerIdField = versionContainerIdField;
	}
	//
	
	//
	//Variables
	//
	private final String name;
	private final String blobField;
	private final String blobSizeField;
	private final String nameField;
	private final String extensionField;
	private final String versionNumberField;
	private final String versionContainerIdField;
	//

	//
	//Properties
	//
	public String getName() { return name; }

	public String getBlobSizeField() { return blobSizeField; }

	public String getNameField() { return nameField; }

	public String getExtensionField() { return extensionField; }

	public String getBlobField() { return blobField; }

	public String getVersionNumberField() { return versionNumberField; }
	
	public String getVersionContainerIdField() { return versionContainerIdField; }
	//
	
	
}
