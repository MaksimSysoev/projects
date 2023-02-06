package com.mycervello.file_extractor.jdbc.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mycervello.file_extractor.utils.db.JdbcHelper;

public abstract class BaseDaoImpl {
	
	//
	//Variables
	//
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	protected  JdbcHelper jdbcHelper;
	//

}