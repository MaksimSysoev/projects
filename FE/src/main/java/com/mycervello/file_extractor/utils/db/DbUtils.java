package com.mycervello.file_extractor.utils.db;

import java.sql.Blob;
import java.sql.SQLException;

/**
 * Utilities to work with DB.
 * 
 * @author Gennadiy Pervukhin
 * @created 28-12-2020
 */
public class DbUtils {
	
	//
	//Constants
	//
	public static final String FIELD_ID = "id";
	public static final String FIELD_SF_ID = "sfid";
	//
	
	//
	//Public static methods
	//
	public static byte[] getBlobBytes(Blob blob) throws SQLException {
		return blob.getBytes(1, (int)blob.length());
	}
	//
}