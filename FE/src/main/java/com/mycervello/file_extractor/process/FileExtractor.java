package com.mycervello.file_extractor.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.mycervello.file_extractor.ProcessStarter;
import com.mycervello.file_extractor.jdbc.dao.GenericDao;
import com.mycervello.file_extractor.utils.datatype.StringUtils;
import com.mycervello.file_extractor.utils.db.DbSchema;
import com.mycervello.file_extractor.utils.db.DbUtils;
import com.mycervello.file_extractor.utils.db.JdbcHelper;
import com.mycervello.file_extractor.utils.db.SqlQueryBuilder;
import com.mycervello.file_extractor.utils.dto.FileExtractorParams;
import com.mycervello.file_extractor.utils.dto.ParentRecordInfo;
import com.mycervello.file_extractor.utils.exception.ExceptionUtils;

/**
 * This class extracts parent records and uses them for further processing.
 * 
 * @author Maksim Sysoev
 * @created 12-09-2022
 */
public class FileExtractor {
	
	//
	//Constants
	//
	private static final Logger logger = LogManager.getLogger(ProcessStarter.class);
	
	private static final String M_PROCESS_COMPLETED = "***** PROCESS COMPLETED: "
		+ "Total count of downloaded files: %s.";
	
	private static final String M_PROCESS_FAILED = 
		"The process was stopped because of this error: %s";
	//

	//
	//Constructors
	//
	public FileExtractor(Resources resources, FileExtractorParams params) {
		this.resources = resources;
		this.params = params;
	}
	//

	//
	//Variables
	//
	private Resources resources;
	private FileExtractorParams params;

	private List<ParentRecordInfo> parentRecordsInfo;
	//

	//
	//Private methods
	//
	private void getAllParentRecords() {
		SqlQueryBuilder queryBuilder = new SqlQueryBuilder(DbSchema.ARCHIVE,
			params.getParentTable());
		
		queryBuilder.addFields(DbUtils.FIELD_SF_ID, params.getParentNameField());
		
		queryBuilder.addCondition(params.getParentCondition());

		final String query = queryBuilder.build();
		
		ResultSetExtractor<List<ParentRecordInfo>> resultExtractor = new RecordsExtractor(params);
		
		parentRecordsInfo = resources.jdbc.query(query, resultExtractor);
	}
	//

	//
	//Public methods
	//
	public void run() {
		try {
			logger.info("***** PROCESS STARTED");
			
			//STEP #1: get all parent records
			this.getAllParentRecords();
			
			int totalCountOfDownloadedFiles = 0;
			//STEP #2: process file tables for each parent record
			for (ParentRecordInfo parentRecord : parentRecordsInfo) {
				final RecordFilesExtractor recordFilesExtractor = new RecordFilesExtractor(
					this.resources, this.params, parentRecord);
				totalCountOfDownloadedFiles += recordFilesExtractor.processFileTableRecords();
			}
			
			logger.info(String.format(M_PROCESS_COMPLETED, totalCountOfDownloadedFiles));
		}
		catch (Exception error) {	
			logger.error(String.format(M_PROCESS_FAILED, ExceptionUtils.extractMessage(error)));
		}
	}
	//

	//
	//Data types
	//
	private static class RecordsExtractor implements ResultSetExtractor<List<ParentRecordInfo>> {

		//
		//Constructors
		//
		public RecordsExtractor(FileExtractorParams params) {
			this.params = params;
		}		
		//
		
		//
		//Variables
		//
		private FileExtractorParams params;
		//
		
		//
		//Public methods
		//
		@Override
		public List<ParentRecordInfo> extractData(ResultSet rs) throws SQLException,
			DataAccessException 
		{
			List<ParentRecordInfo> sfRecords = new ArrayList<>();
			
			while (rs.next()) {
				ParentRecordInfo record = new ParentRecordInfo();
				record.setSfId(rs.getString(DbUtils.FIELD_SF_ID));
				
				if (StringUtils.isNotEmpty(params.getParentNameField())) {
					record.setName(rs.getString(params.getParentNameField()));
				}
				
				sfRecords.add(record);
			}
			
			return sfRecords;
		};
		//
	}
		
	
	@Component
	public static class Resources {
		@Autowired
		public JdbcTemplate jdbc;

		@Autowired
		public JdbcHelper jdbcHelper;

		@Autowired
		public GenericDao genericDao;
	}
	//
}