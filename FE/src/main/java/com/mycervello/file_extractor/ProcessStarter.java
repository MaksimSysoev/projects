package com.mycervello.file_extractor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mycervello.file_extractor.config.AppSettings;
import com.mycervello.file_extractor.process.FileExtractor;
import com.mycervello.file_extractor.utils.dto.FileExtractorParams;

/**
 * This class checks input arguments and starts the file extraction process
 * 
 * @author Maksim Sysoev
 * @created 12-09-2022
 */
@Component
public class ProcessStarter implements CommandLineRunner {
	
	//
	//Constants
	//
	private static final Logger logger = LogManager.getLogger(ProcessStarter.class);
	//
	
	//
	//Variables
	//
	@Autowired
	private FileExtractor.Resources resources;
	//

	//
	//Public methods
	//
	@Override
	public void run(String... args) {
		//STEP #1: validate application settings
		if (!AppSettings.validateAllSettings()) {
			logger.error("***** PLEASE CONFIGURE SYSTEM SETTINGS BEFORE RUN");
			return;
		}
		
		//STEP #2: parse and validate input arguments
		FileExtractorParams params = new FileExtractorParams(args);
		if (!params.validateAllParams()) {
			return;
		}
		
		//STEP #3: run the process
		new FileExtractor(resources, params).run();
	}
	//
}