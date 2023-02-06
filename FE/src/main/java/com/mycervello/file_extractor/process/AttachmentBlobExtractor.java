package com.mycervello.file_extractor.process;

import com.mycervello.file_extractor.process.FileExtractor.Resources;
import com.mycervello.file_extractor.utils.dto.FileTable;
import com.mycervello.file_extractor.utils.dto.ParentRecordInfo;
import com.mycervello.file_extractor.utils.dto.SfRecordWithBlob;

/**
 * This class extends {@link AbstractBlobExtractor} for Attachment
 * @author Maksim Sysoev
 * @created 12-09-2022
 */
public class AttachmentBlobExtractor extends AbstractBlobExtractor {

	//
	//Constants
	//
	private static final String CONDITION_BY_PARENT_ID = "parentid = ?";
	private static final String T_FILE_NAME = "%s - %s";
	//

	//
	//Constructors
	//
	public AttachmentBlobExtractor(Resources resources, ParentRecordInfo parentRecordInfo,
		String folderPath) {
		super(resources, parentRecordInfo, folderPath, FileTable.ATTACHMENT,
			CONDITION_BY_PARENT_ID);
	}
	//

	//
	//Public methods
	//
	@Override
	protected String buildFileName(SfRecordWithBlob record) {
		return String.format(T_FILE_NAME, record.getSfId(), record.getFileName());
	}
	//
}
