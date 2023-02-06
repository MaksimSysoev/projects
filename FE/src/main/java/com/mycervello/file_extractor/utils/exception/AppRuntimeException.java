package com.mycervello.file_extractor.utils.exception;

/**
 * This exception occurs in runtime and must not be declared in the "throws" statements.
 * 
 * @author Gennadiy Pervukhin
 */
public class AppRuntimeException extends RuntimeException {

	//
	//Constructors
	//
	public AppRuntimeException(String errorMessage) {
		super(errorMessage);
	}
	
	public AppRuntimeException(Throwable error) {
		super(ExceptionUtils.extractMessage(error), error);
	}
	//

	//
	//Variables
	//
	private static final long serialVersionUID = 1179741200621950842L;
	//
}