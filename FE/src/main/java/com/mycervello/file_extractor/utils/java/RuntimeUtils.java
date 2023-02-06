package com.mycervello.file_extractor.utils.java;

/**
 * This class provides utilities for the Runtime environment.
 * 
 * @author Gennadiy Pervukhin
 * @created 22-12-2020
 */
public class RuntimeUtils {
	
	//
	//Private static methods
	//
	private static String bytesToMBs(long bytes) {
		return (Math.round(bytes / 1024.0 / 1024 * 100) / 100) + " MB";
	}
	//
	
	//
	//Public static methods
	//
	public static void printMemoryUsage(String label) {
		Runtime runtime = Runtime.getRuntime();
		//runtime.gc();
		String usedMemory = bytesToMBs(runtime.totalMemory() - runtime.freeMemory());
		System.out.println("***** " + label + ": " + usedMemory + " of "
			+ bytesToMBs(runtime.totalMemory())
			+ " (max = " + bytesToMBs(runtime.maxMemory()) + ")");
	}
	//
}