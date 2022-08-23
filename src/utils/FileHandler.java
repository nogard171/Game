package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class FileHandler {
	public static void writeFile(String fileName, String value, boolean append) {
		try {
			// create file with the fileName variable
			File file = new File(fileName);
			// create file if it does not exist
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
			}
			// open the file to writing
			FileWriter myWriter = new FileWriter(fileName, append);
			// write to the file
			myWriter.write(value);
			// close the file when done
			myWriter.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
