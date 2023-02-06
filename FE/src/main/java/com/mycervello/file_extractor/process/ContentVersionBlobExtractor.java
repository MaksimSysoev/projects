package com.mycervello.file_extractor.process;

import com.mycervello.file_extractor.process.FileExtractor.Resources;
import com.mycervello.file_extractor.utils.datatype.StringUtils;
import com.mycervello.file_extractor.utils.db.DbSchema;
import com.mycervello.file_extractor.utils.dto.FileTable;
import com.mycervello.file_extractor.utils.dto.ParentRecordInfo;
import com.mycervello.file_extractor.utils.dto.SfRecordWithBlob;

/**
 * This class extends {@link AbstractBlobExtractor} for ContentVersion
 * 
 * @author Maksim Sysoev
 * @created 12-09-2022
 */
public class ContentVersionBlobExtractor extends AbstractBlobExtractor {
	
	//
	//Constants
	//
	private static final String CONDITION_BY_PARENT_ID = String.format(
		"contentdocumentid IN " +
			"(SELECT contentdocumentid FROM %s.contentdocumentlink WHERE linkedentityid = ?)",
		DbSchema.ARCHIVE);
	private static final String T_FILE_NAME = "%s - %s - v%s%s";
	//

	//
	//Constructors
	//
	public ContentVersionBlobExtractor(Resources resources, ParentRecordInfo parentRecordInfo,
		String folderPath)
	{
		super(resources, parentRecordInfo, folderPath, FileTable.CONTENTVERSION, 
			CONDITION_BY_PARENT_ID);
	}
	//
	
	//
	//Public methods
	//
	@Override
	protected String buildFileName(SfRecordWithBlob record) {
		final String fileExtension = StringUtils.isNotEmpty(record.getExtension()) ?
			"." + record.getExtension() : StringUtils.EMPTY;
		
		return String.format(T_FILE_NAME,  record.getVersionContainerIdField(), 
			record.getFileName(), record.getVersionNumber(), fileExtension);
	}
	//
}
