package com.mycervello.file_extractor.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mycervello.file_extractor.process.FileExtractor.Resources;
import com.mycervello.file_extractor.utils.db.DbSchema;
import com.mycervello.file_extractor.utils.dto.DigitalSizeUtils;
import com.mycervello.file_extractor.utils.dto.FileTable;
import com.mycervello.file_extractor.utils.dto.ParentRecordInfo;
import com.mycervello.file_extractor.utils.dto.SfRecordWithBlob;
import com.mycervello.file_extractor.utils.exception.ExceptionUtils;
import com.mycervello.file_extractor.utils.exception.FileExtractorException;
import com.mycervello.file_extractor.utils.io.FileUtils;

/**
 * This abstract class encapsulates details of a processing extract blobs to files
 *
 * @author Maksim Sysoev
 * @created 12-09-2022
 */
public abstract class AbstractBlobExtractor {

	//
	//Constants
	//
	private static final Logger logger = LogManager.getLogger(AbstractBlobExtractor.class);
	private static final int BLOB_BUFFER_SIZE = DigitalSizeUtils.megabytesToBytes_Int(10);
	
	private static final String T_FILE_PATH = "%s\\%s";
	
	private static final String M_DOWNLOADING_FILE = "       downloading file: %s";
	private static final String M_ERROR_FOR_RECORD = "error occurred for record with id '%s': %s ";
	//

	//
	//Constructors
	//
	public AbstractBlobExtractor(Resources resources, ParentRecordInfo parentRecordInfo,
		String folderPath, FileTable fileTable, String conditionForParent)
	{
		this.resources = resources;
		this.parentRecordInfo = parentRecordInfo;
		this.folderPath = folderPath;
		this.fileTable = fileTable;
		this.conditionForParent = conditionForParent;
	}
	//

	//
	//Variables
	//
	private Resources resources;
	private ParentRecordInfo parentRecordInfo;
	private String folderPath;
	private FileTable fileTable;
	private String conditionForParent;
	//

	//
	//Private methods
	//
	private void extractBlobToFile(SfRecordWithBlob record, String folderPath)
		throws FileExtractorException 
	{
		final String fileName = FileUtils.normalizeFileName(buildFileName(record));
		File file = new File(String.format(T_FILE_PATH, folderPath, fileName));
		
		logger.info(String.format(M_DOWNLOADING_FILE, fileName));
		
		try (OutputStream outputStream = new FileOutputStream(file)) {
			outputStream.flush();
			byte[] buffer;
			int bufferLength;
			long pos = 1;
			Blob blob = record.getDbBlob();
			do {
				buffer = blob.getBytes(pos, BLOB_BUFFER_SIZE);
				bufferLength = buffer.length;
				pos += bufferLength;
				outputStream.write(buffer);
				outputStream.flush();
			} 
			while (bufferLength > 0);
		} 
		catch (Exception error) {
			throw new FileExtractorException(String.format(M_ERROR_FOR_RECORD, record.getSfId(),
				ExceptionUtils.extractMessage(error)));
		}
	}

	private List<SfRecordWithBlob> getSfRecordsWithBlobs() {
		return resources.genericDao.getSfRecordWithBlobByCondition(DbSchema.ARCHIVE, fileTable,
			conditionForParent,	parentRecordInfo.getSfId());
	}
	//

	//
	//Public methods
	//
	protected abstract String buildFileName(SfRecordWithBlob record);
	
	public int extractBlobsToFiles() throws FileExtractorException {
		try {
			//STEP #1: get list sf records with blobs
			List<SfRecordWithBlob> records = getSfRecordsWithBlobs();
			
			//STEP #2: extract blobs to files
			resources.jdbcHelper.executeInTrx_Unsafe("ExtractingBlobsToFiles", () -> {
				for (SfRecordWithBlob record : records) {
					extractBlobToFile(record, folderPath);
				}
			});
			
			return records.size();
		}
		catch (Exception error) {
			throw new FileExtractorException(error);
		}
	}
	//

}
