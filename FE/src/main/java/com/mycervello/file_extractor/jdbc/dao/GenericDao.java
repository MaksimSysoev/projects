package com.mycervello.file_extractor.jdbc.dao;

import java.util.List;

import com.mycervello.file_extractor.utils.dto.FileTable;
import com.mycervello.file_extractor.utils.dto.SfRecordWithBlob;

public interface GenericDao {
	List<SfRecordWithBlob> getSfRecordWithBlobByCondition( String schemaName,
		FileTable fileTable, String condition, Object... conditionParams);

}