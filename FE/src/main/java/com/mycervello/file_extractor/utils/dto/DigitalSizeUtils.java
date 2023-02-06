package com.mycervello.file_extractor.utils.dto;

public class DigitalSizeUtils {
	//
	//Constants
	//
	private static final int BYTES_IN_MB = (1024 * 1024);
	//
	
	//
	//Public static methods
	//
	/**
	 * This method can be used to convert small number of megabytes, up to 2047 inclusive. If a
	 * larger value is passed, it simply returns maximum integer value.
	 */
	public static int megabytesToBytes_Int(int megabytes) {
		return (Math.abs(megabytes) < 2047 ? (megabytes * BYTES_IN_MB) : Integer.MAX_VALUE);
	}
	
	public static long megabytesToBytes(int megabytes) {
		return (megabytes * (long)BYTES_IN_MB);
	}
	
	public static String prettyFormat(long sizeInBytes) {
		final String[] UNITS = new String[] { "bytes", "KB", "MB", "GB" };
		final double UNITS_STEP_FACTOR = 1024;
		
		int indexOfUnit = 0;
		double sizeInCurrentUnits = sizeInBytes;
		for (; indexOfUnit < UNITS.length; indexOfUnit++) {
			if (sizeInCurrentUnits < UNITS_STEP_FACTOR) {
				break;
			}
			
			sizeInCurrentUnits /= UNITS_STEP_FACTOR;
		}
		
		return String.format("%.1f %s", sizeInCurrentUnits, UNITS[indexOfUnit]);
	}
	//
}
