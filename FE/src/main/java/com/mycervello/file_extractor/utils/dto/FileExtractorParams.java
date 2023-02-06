package com.mycervello.file_extractor.utils.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycervello.file_extractor.utils.datatype.EnumUtils;
import com.mycervello.file_extractor.utils.datatype.StringUtils;
import com.mycervello.file_extractor.utils.datatype.collection.CollectionUtils;

/**
 * This class parses and validates input arguments.
 * 
 * @author Maksim Sysoev
 * @created 12-09-2022
 */
public class FileExtractorParams {

	//
	//Constants
	//
	private static final Logger logger = LogManager.getLogger(FileExtractorParams.class);

	private static final String PARAM_OUTPUT_FOLDER_PATH = "outputFolderPath";
	private static final String PARAM_PARENT_TABLE = "parentTable";
	private static final String PARAM_PARENT_CONDITION = "parentCondition";
	private static final String PARAM_PARENT_NAME_FIELD = "parentNameField";
	private static final String PARAM_FILE_TABLES = "fileTables";
	
	private static final String FILE_TABLES_SEPARATOR = ",";
	//

	//
	//Constructors
	//
	public FileExtractorParams(String... args) {
		parseCmdArgs(args);

		this.outputFolderPath = mapOfArgs.get(PARAM_OUTPUT_FOLDER_PATH);
		this.parentTable = mapOfArgs.get(PARAM_PARENT_TABLE);
		this.parentCondition = mapOfArgs.get(PARAM_PARENT_CONDITION);
		this.parentNameField = mapOfArgs.get(PARAM_PARENT_NAME_FIELD);
		this.fileTables = convertStringToCollectionValues(mapOfArgs.get(PARAM_FILE_TABLES), 
			FILE_TABLES_SEPARATOR);
	}
	//

	//
	//Variables
	//
	private Map<String, String> mapOfArgs = new HashMap<>();

	private String outputFolderPath;
	private String parentTable;
	private String parentCondition;
	private String parentNameField;
	private List<String> fileTables;
	//

	//
	//Properties
	//
	public String getOutputFolderPath() { return outputFolderPath; }
	public void setOutputFolderPath(String outputFolderPath) {
		this.outputFolderPath = outputFolderPath;
	}

	public String getParentTable() { return parentTable;}
	public void setParentTable(String parentTable) { this.parentTable = parentTable; }

	public String getParentCondition() { return parentCondition; }
	public void setParentCondition(String parentCondition) {
		this.parentCondition = parentCondition;
	}

	public String getParentNameField() { return parentNameField; }
	public void setParentNameField(String parentNameField) {
		this.parentNameField = parentNameField;
	}

	public List<String> getFileTables() { return fileTables; }
	public void setFileTables(List<String> fileTables) {
		this.fileTables = fileTables;
	}
	//

	//
	//Private methods
	//
	private Map<String, String> parseCmdArgs(String... args) {
		for (String arg : args) {
			final int indexOfEqualSign = arg.indexOf("=");
			if (indexOfEqualSign != -1) {
				final String argName = arg.substring(0, indexOfEqualSign);
				final String argValue = arg.substring(indexOfEqualSign + 1);
				mapOfArgs.put(argName, argValue);
			}
		}

		return mapOfArgs;
	}

	private List<String> convertStringToCollectionValues(String sourceString, String separator) {
		if (StringUtils.isNotEmpty(sourceString)) {
			//delete all whitespace characters
			sourceString = sourceString.replaceAll("\\s+", "");
			
			return Arrays.asList(StringUtils.split(sourceString, separator));
		}

		return null;
	}

	private boolean checkFileTableNames() {
		final List<String> fileTableNames = Arrays.asList(EnumUtils.extractNames(FileTable.class));
		
		for (String table : this.fileTables) {
			if (!fileTableNames.contains(table.toUpperCase())) {
				return false;
			}
		}
		
		return true;
	}
	//

	
	//
	//Public methods
	//
	public boolean validateAllParams() {
		if (StringUtils.isEmpty(outputFolderPath) || StringUtils.isEmpty(parentTable) 
			|| StringUtils.isEmpty(parentCondition)	|| CollectionUtils.isEmpty(fileTables))
		{
			logger.error("***** PLEASE PASS 4 REQUIRED PARAMS:"
				+ " outputFolderPath, parentTable, parentCondition, fileTables");

			return false;
		}
		
		if (!this.checkFileTableNames()) {
			logger.error(String.format("***** THE APP SUPPORTS ONLY THESE FILE TABLES: %s",  
				StringUtils.join(EnumUtils.extractNames(FileTable.class), ", ")));
			return false;
		}

		return true;
	}
	//

}
