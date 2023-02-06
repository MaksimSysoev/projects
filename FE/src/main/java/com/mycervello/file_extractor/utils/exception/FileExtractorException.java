package com.mycervello.file_extractor.utils.exception;

public class FileExtractorException extends Exception {
	
	//
	//Constructors
	//
	public FileExtractorException(String errorMesage) {
		super(errorMesage);
	}
	
	public FileExtractorException(Throwable error) {
		super(ExceptionUtils.extractMessage(error), error);
	}
	//
	
	//
	//Variables
	//
	private static final long serialVersionUID = -3892786508911504812L;
	//
}
