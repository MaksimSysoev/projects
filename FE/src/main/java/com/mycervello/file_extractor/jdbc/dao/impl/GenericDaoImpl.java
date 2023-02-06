package com.mycervello.file_extractor.jdbc.dao.impl;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.mycervello.file_extractor.jdbc.dao.GenericDao;
import com.mycervello.file_extractor.utils.datatype.StringUtils;
import com.mycervello.file_extractor.utils.db.DbUtils;
import com.mycervello.file_extractor.utils.db.SqlQueryBuilder;
import com.mycervello.file_extractor.utils.dto.FileTable;
import com.mycervello.file_extractor.utils.dto.SfRecordWithBlob;
import com.mycervello.file_extractor.utils.dto.SfRecordWithBlobInfo;

@Repository("genericDao")
public class GenericDaoImpl extends BaseDaoImpl implements GenericDao {
	
	//
	//Constants
	//
	private static final String SELECT_RECORDS_WITH_BLOBS = 
		"SELECT %s, lo.oid \"lo.oid\" FROM %s.%s t " +
		"LEFT JOIN pg_largeobject_metadata lo ON t.%s = lo.oid %s";
	//

	//
	//Private methods
	//
	/**
	 * The method extracts a Blob from the current row in the passed {@link ResultSet}. The row must
	 * contain a record with blob field that is joined with a corresponding record from the Large
	 * Object (pg_largeobject_metadata). Example of a query for this scenario see in the
	 * {@link #SELECT_RECORD_WITH_BLOB_BY_PARENT_ID}.
	 */
	private static Blob extractBlobFromRecordJoinedWithLO(ResultSet resultSet, String blobField)
		throws SQLException 
	{
		// check whether the large object exists
		final Long oid = resultSet.getObject(blobField, Long.class);
		final boolean blobExists = (oid != null && resultSet.getObject("lo.oid") != null);
		return (blobExists ? resultSet.getBlob(blobField) : null);
	}

	private static void fillSfRecordWithBlobInfo(SfRecordWithBlobInfo sfRecord, ResultSet resultSet,
		String blobSizeField) throws SQLException
	{
		sfRecord.setId(resultSet.getInt(DbUtils.FIELD_ID));
		sfRecord.setSfId(resultSet.getString(DbUtils.FIELD_SF_ID));
		sfRecord.setBlobSize(resultSet.getInt(blobSizeField));
	}
	//

	//
	//Public methods
	//
	@Override
	public List<SfRecordWithBlob> getSfRecordWithBlobByCondition(String schemaName, 
		FileTable fileTable, String condition, Object... conditionParams)
	{
		//prepare a list of fields
		final List<String> fields = new ArrayList<>(Arrays.asList(DbUtils.FIELD_ID,
			DbUtils.FIELD_SF_ID, fileTable.getBlobField(), fileTable.getBlobSizeField(),
			fileTable.getNameField(), fileTable.getExtensionField(), 
			fileTable.getVersionNumberField(), fileTable.getVersionContainerIdField()));		
		fields.removeAll(Arrays.asList(null, StringUtils.EMPTY));

		//build a query
		final String query = String.format(SELECT_RECORDS_WITH_BLOBS, 
			SqlQueryBuilder.joinFields(fields), schemaName, fileTable.getName(),
			fileTable.getBlobField(), 
			StringUtils.isEmpty(condition) ? StringUtils.EMPTY : "WHERE " + condition);
		
		//initialize a result set extractor
		final ResultSetExtractor<List<SfRecordWithBlob>> recordExtractor = 
			new BlobExtractor(fileTable);

		//execute the query and extract the result
		return jdbcTemplate.query(query, recordExtractor, conditionParams);
	}
	//

	//
	// Data types
	//
	private static class BlobExtractor 
		implements ResultSetExtractor<List<SfRecordWithBlob>>
	{
		//
		//Constructors
		//
		public BlobExtractor(FileTable fileTable)
		{
			this.fileTable = fileTable;
		}
		//
		
		//
		//Variables
		//
		private FileTable fileTable;
		//
		
		//
		//Public methods
		//
		@Override
		public List<SfRecordWithBlob> extractData(ResultSet rs) throws SQLException, 
			DataAccessException 
		{
			List<SfRecordWithBlob> listSfRecord = new ArrayList<>();
			while (rs.next()) {
				SfRecordWithBlob sfRecord = new SfRecordWithBlob();
				fillSfRecordWithBlobInfo(sfRecord, rs, fileTable.getBlobSizeField());

				//set file name
				sfRecord.setFileName(rs.getString(fileTable.getNameField()));				
				
				//set content document id
				if (StringUtils.isNotEmpty(fileTable.getVersionContainerIdField())) {
					sfRecord.setVersionContainerIdField(
						rs.getString(fileTable.getVersionContainerIdField()));				
				}
				
				//set version number
				if (StringUtils.isNotEmpty(fileTable.getVersionNumberField())) {
					sfRecord.setVersionNumber(rs.getString(fileTable.getVersionNumberField()));
				}
				
				//set file extension
				if (StringUtils.isNotEmpty(fileTable.getExtensionField())) {
					sfRecord.setExtension(rs.getString(fileTable.getExtensionField()));
				}

				//set blob
				sfRecord.setDbBlob(extractBlobFromRecordJoinedWithLO(rs, fileTable.getBlobField()));

				listSfRecord.add(sfRecord);
			}

			return listSfRecord;
		}
		//
	}
	//
}