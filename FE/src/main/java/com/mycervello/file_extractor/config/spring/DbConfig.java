package com.mycervello.file_extractor.config.spring;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mycervello.file_extractor.config.AppSettings;
import com.zaxxer.hikari.HikariDataSource;

/**
 * This class is used to configure connection to DB in Spring app.
 * 
 * @author Gennadiy Pervukhin
 * @created 20-11-2018
 */
@Configuration
public class DbConfig {
	
	//
	//Variables
	//
	/**
	 * This variable helps to configure maximum pool size of a data source. It works if you set it
	 * before initialization of a context.
	 */
	public static int maxPoolSize = -1;
	//
	
	//
	//Public methods
	//
	@Bean
	public DataSource dataSource() throws URISyntaxException {
		
		//STEP #1: create a new data source
		URI dbUri = new URI(AppSettings.getDatabaseUrl());

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort()
			+ dbUri.getPath() + "?sslmode=require";

		DataSource dataSource = DataSourceBuilder.create()
			.url(dbUrl).username(username).password(password)
			.build();
		
		//STEP #2: specific configuration of the data source
		if (dataSource != null && dataSource instanceof HikariDataSource && maxPoolSize > 0) {
			((HikariDataSource)dataSource).setMaximumPoolSize(DbConfig.maxPoolSize);
		}
		
		return dataSource;
	}
	//
}