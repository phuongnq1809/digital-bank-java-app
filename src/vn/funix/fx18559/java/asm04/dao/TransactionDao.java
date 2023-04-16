package vn.funix.fx18559.java.asm04.dao;

import vn.funix.fx18559.java.asm04.model.Transaction;
import vn.funix.fx18559.java.asm04.service.BinaryFileService;

import java.io.File;
import java.util.List;

public class TransactionDao {
	private final static String FILE_PATH = "src/vn/funix/fx18559/java/asm04/file/transactions.dat";

	public static void save(List<Transaction> transactions) {
		BinaryFileService.writeFile(FILE_PATH, transactions);
	}

	public static List<Transaction> list() {
		return BinaryFileService.readFile(FILE_PATH);
	}

	// Kiem tra xem file da ton tai chua
	public static boolean isFileExisted() {
		File f = new File(FILE_PATH);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
