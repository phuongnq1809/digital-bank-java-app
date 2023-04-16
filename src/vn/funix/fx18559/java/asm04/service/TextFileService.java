package vn.funix.fx18559.java.asm04.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileService {
	private static final String COMMA_DELIMITER = ",";

	public static List<List<String>> readFile(String fileName) {
		List<List<String>> customersList = new ArrayList<>();

		try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
			String input;
			while ((input = file.readLine()) != null) {
				String[] data = input.split(COMMA_DELIMITER);
				List<String> customer = new ArrayList<>();
				customer.add(data[0]); // customer ID
				customer.add(data[1]); // customer name

				customersList.add(customer);
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
		return customersList;
	}

	// Kiem tra xem file co ton tai hay khong
	public static boolean isFileExisted(String fileName) {
		File f = new File(fileName);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
