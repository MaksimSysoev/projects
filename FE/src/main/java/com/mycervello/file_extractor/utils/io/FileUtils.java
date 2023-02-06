package com.mycervello.file_extractor.utils.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.mycervello.file_extractor.utils.datatype.StringUtils;


public class FileUtils {
	
	//
	//Constants
	//
	public static final String EMPTY = "";
	
	public static final String REGEX_FORBIDDEN_CHARACTERS = "[:\"\"<>*?/[\\|][\\\\]]";
	
	private static final int MAX_FILE_NAME_LENGTH = 255; 
	private static final int MAX_FILE_EXTENSION_LENGTH = 10;
	//
	
	//
	//Private methods
	//
	/**
	 * this method reduces file length to 255 characters
	 */
	private static String trimFileName(String fileName) {	
		int indexOfLastDot = fileName.lastIndexOf(".");
		if (indexOfLastDot == -1) {
			return fileName.substring(0, MAX_FILE_NAME_LENGTH);
		}
		else {
			String extension = fileName.substring(indexOfLastDot + 1, fileName.length());
			//if extension more than 10 characters, then in our case the extension is part of file
			//and file name is truncated.
			if (extension.length() > MAX_FILE_EXTENSION_LENGTH) {
				return fileName.substring(0, MAX_FILE_NAME_LENGTH);
			}
			
			//extract title, where 'FILE_NAME_LENGTH - 1' is the length without the character '.'
			String title = fileName.substring(0, (MAX_FILE_NAME_LENGTH - 1) - extension.length());				
		
			return String.format("%s.%s", title, extension);
		}
	}
	//
	
	//
	//Public static methods
	//
	public static boolean exists(String fileName) {
		return (StringUtils.isNotEmpty(fileName) && (new File(fileName)).exists());
	}
	
	public static void delete(String fileName) {

		if (StringUtils.isNotEmpty(fileName)) {
			File file = new File(fileName);
			//this method does not throw an exception if the file does not exist
			file.delete();
		}
	}
	
	public static File generateOutputFile(String outputPath, String folderName, String fileName) {
		String filePath = outputPath + File.separator + folderName + File.separator + fileName;
		
		return new File(filePath);
	}
	
	public static boolean createNewFile(File file) throws IOException {
		file.getParentFile().mkdirs();
		return file.createNewFile();
	}
	
	public static void writeToFile(File file, byte[] bytes) throws IOException {
		createNewFile(file);
		Files.write(file.toPath(), bytes);
	}
	
	public static String normalizeFileName(String fileName) {
		String newFileName = fileName.trim();
		
		newFileName = newFileName.replaceAll(REGEX_FORBIDDEN_CHARACTERS, EMPTY);
		
		if (newFileName.length() > 255) {
			newFileName = trimFileName(newFileName);
		}
		
		return newFileName;
	}
	//
}
