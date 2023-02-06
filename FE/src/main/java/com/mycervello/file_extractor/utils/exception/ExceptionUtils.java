package com.mycervello.file_extractor.utils.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

import org.postgresql.util.PSQLException;

import com.mycervello.file_extractor.utils.datatype.StringUtils;

public class ExceptionUtils {
	
	//
	//Constants
	//
	private static final int DEFAULT_MAX_ERROR_LENGTH = 5000;
	//
	
	//
	//Private static methods
	//
	private static String getMessage(Throwable error) {
		String message = error.getMessage();
		
		if (error instanceof InvocationTargetException) {
			Throwable targetError = ((InvocationTargetException)error).getTargetException();
			if (targetError != null) {
				message = targetError.getMessage();
			}
		}
		else if (error.getCause() != null && (error.getCause() instanceof PSQLException
			|| error instanceof ExecutionException))
		{
			//1. A nested PSQLException does not add a source SQL-code to the message
			//2. ExecutionException usually wraps a business exception that should be taken. It's
			//used by the async CompletableFuture
			message = error.getCause().getMessage();
		}
		
		if (message != null) { message = message.trim(); }
		return message;
	}
	//
	
	//
	//Public static methods
	//
	/*This method helps to extract error message from an exception respecting a limit.
	For example, it helps us to avoid problems with SQL exceptions that may have very long messages
	because of SQL-queries.*/
	public static String extractMessage(Throwable error, int maxLength) {
		
		//STEP #1: if a message does not exceed the limit, then we return it
		String errorMessage = getMessage(error);
		if (errorMessage == null || errorMessage.length() <= maxLength) {
			return errorMessage;
		}
		
		//STEP #2: if a message is too large, then we try to extract some details from
		//the nested exceptions and truncate the current message
		String customErrorMessage = "";
		//- extract error message from the nested exceptions
		if (error.getCause() != error) {
			customErrorMessage = extractMessage(error.getCause(), maxLength)
				+ "     ----->     More: ";
		}
		//- truncate and add the current message
		int remainingLength = (maxLength - customErrorMessage.length());
		customErrorMessage += StringUtils.truncate(errorMessage, remainingLength);
		
		return customErrorMessage;
	}
	
	public static String extractMessage(Throwable error) {
		return extractMessage(error, DEFAULT_MAX_ERROR_LENGTH);
	}
	//
}
