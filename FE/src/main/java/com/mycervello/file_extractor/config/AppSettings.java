package com.mycervello.file_extractor.config;

import java.util.HashMap;
import java.util.Map;

import com.mycervello.file_extractor.utils.datatype.StringUtils;



/**
 * This class provides utilities for application settings.
 * 
 * @author Gennadiy Pervukhin
 * @created 20-06-2018
 */
public class AppSettings {
	
	/*********************************************************
	* SETTINGS FROM the SYSTEM VARIABLES *
	**********************************************************/
	//
	//Variables
	//
	private static final String SV_DATABASE_URL = "ADB_DATABASE_URL";
	
	private static final Map<String, String> DEFAULT_SYSTEM_VARS = new HashMap<String, String>();
	//
	
	//
	//Private static methods
	//
	private static String getSystemVar(String name) {
		String value = System.getenv(name);
		return (StringUtils.isNotEmpty(value) ? value : DEFAULT_SYSTEM_VARS.get(name));
	}
	//
	
	//
	//Public static methods
	//
	public static String getDatabaseUrl() {
		return getSystemVar(SV_DATABASE_URL);
	}
	//


	//
	//Public static common methods
	//
	public static boolean validateAllSettings() {
		return (StringUtils.isNotEmpty(getDatabaseUrl()));
	}
	//
}