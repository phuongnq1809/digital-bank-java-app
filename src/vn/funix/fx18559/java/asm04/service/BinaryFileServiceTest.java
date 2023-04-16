package vn.funix.fx18559.java.asm04.service;

import org.junit.*;
import vn.funix.fx18559.java.asm04.model.Customer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BinaryFileServiceTest {

	@Test
	public void readFile_filePathTrue() {
		String fileName = "src/vn/funix/fx18559/java/asm04/file/customers.dat";
		assertNotNull(BinaryFileService.readFile(fileName));
		List<Customer> list = new ArrayList<>();
		assertNotSame(list, BinaryFileService.readFile(fileName));
	}

	@Test //throws IOException
	public void readFile_filePathFalse() {
		String fileName = "test";
		assertNotNull(BinaryFileService.readFile(fileName));
	}

	@Test
	public void isFileExisted() {
		assertFalse(BinaryFileService.isFileExisted("test"));
	}
}