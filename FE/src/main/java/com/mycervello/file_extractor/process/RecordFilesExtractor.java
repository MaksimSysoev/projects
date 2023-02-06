package com.mycervello.file_extractor.process;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycervello.file_extractor.process.FileExtractor.Resources;
import com.mycervello.file_extractor.utils.datatype.StringUtils;
import com.mycervello.file_extractor.utils.dto.FileExtractorParams;
import com.mycervello.file_extractor.utils.dto.FileTable;
import com.mycervello.file_extractor.utils.dto.ParentRecordInfo;
import com.mycervello.file_extractor.utils.exception.AppRuntimeException;
import com.mycervello.file_extractor.utils.exception.FileExtractorException;
import com.mycervello.file_extractor.utils.io.FileUtils;

/**
 * This class creates a new folder for parent record and extracts blobs.
 * 
 * @author Maksim Sysoev
 * @created 12-09-2022
 */
public class RecordFilesExtractor {

	//
	// Constants
	//
	private static final Logger logger = LogManager.getLogger(RecordFilesExtractor.class);
	
	private static final String M_DOWNLOADING_FILES = 
		"----- downloading files for the parent record: %s";
	
	private static final String EMPTY_NAME = "no name";
	private static final String T_FOLDER_NAME = "%s - %s";
	private static final String T_FOLDER_PATH = "%s\\%s";
	//

	//
	// Constructors
	//
	public RecordFilesExtractor(Resources resources, FileExtractorParams params,
		ParentRecordInfo parentRecord) {
		this.resources = resources;
		this.params = params;
		this.parentRecord = parentRecord;
	}
	//

	//
	// Variables
	//
	private Resources resources;
	private FileExtractorParams params;
	private ParentRecordInfo parentRecord;
	//

	//
	// Private methods
	//
	private String buildFolderName() {
		final String recordName = this.parentRecord.getName();
		final String folderName = String.format(T_FOLDER_NAME, this.parentRecord.getSfId(),
			(StringUtils.isNotEmpty(recordName) ? recordName : EMPTY_NAME));

		return FileUtils.normalizeFileName(folderName);
	}

	private String createFolder() throws IOException {
		String folderPath = String.format(T_FOLDER_PATH, params.getOutputFolderPath(),
			buildFolderName());
		
		new File(folderPath).mkdirs();
		
		return folderPath;
	}

	private AbstractBlobExtractor buildBlobExtractor(String tableName, String folderPath)
		throws FileExtractorException
	{
		if (tableName.equalsIgnoreCase(FileTable.ATTACHMENT.getName())) {
			return new AttachmentBlobExtractor(this.resources, this.parentRecord, folderPath);
		}
		else if (tableName.equalsIgnoreCase(FileTable.CONTENTVERSION.getName())) {
			return new ContentVersionBlobExtractor(this.resources, this.parentRecord, folderPath);
		}
		else {
			throw new AppRuntimeException(String.format("Table %s is not supported", tableName));
		}
	}
	//

	//
	//Public methods
	//
	public int processFileTableRecords() throws FileExtractorException, IOException {
		logger.info(String.format(M_DOWNLOADING_FILES, parentRecord.getName()));
		
		//STEP #1: create a new folder for parent. Folder name will be 'sfid' - 'parent's name'
		String folderPath = this.createFolder();

		//STEP #2: process file tables for the parent record
		//number of processed file records
		int numberRecords = 0;
		
		for (String fileTable : params.getFileTables()) {
			AbstractBlobExtractor blobExtractor = this.buildBlobExtractor(fileTable, folderPath);
			numberRecords += blobExtractor.extractBlobsToFiles();
		}
		
		//if there are no files, the empty folder will be deleted
		if (numberRecords == 0) {
			FileUtils.delete(folderPath);
		}
		
		return numberRecords;
	}
	//
}
