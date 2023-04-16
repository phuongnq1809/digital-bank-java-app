package vn.funix.fx18559.java.asm04.service;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TextFileServiceTest {

	@Test
	public void readFile_filePathTrue() {
		String fileName = "src/vn/funix/fx18559/java/asm04/file/customers.txt";
		assertNotNull(TextFileService.readFile(fileName));
		List<List<String>> list = new ArrayList<>();
		assertNotSame(list, TextFileService.readFile(fileName));
	}

	@Test // throw FileNotFoundException
	public void readFile_filePathFalse() {
		String fileName = "test";
		assertNotSame(null, TextFileService.readFile(fileName));
	}

	@Test
	public void isFileExisted() {
		assertFalse(TextFileService.isFileExisted("test"));
	}
}