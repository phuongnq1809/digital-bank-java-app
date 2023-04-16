package vn.funix.fx18559.java.asm04.dao;

import vn.funix.fx18559.java.asm04.model.Account;
import vn.funix.fx18559.java.asm04.service.BinaryFileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class AccountDao {
	private final static String FILE_PATH = "src/vn/funix/fx18559/java/asm04/file/accounts.dat";

	public static void save(List<Account> accounts) {
		BinaryFileService.writeFile(FILE_PATH, accounts);
	}

	public static void saveUpdate(List<Account> accounts) {
		BinaryFileService.writeFile(FILE_PATH, accounts, true);
	}

	public static List<Account> list() {
		return BinaryFileService.readFile(FILE_PATH);
	}

	public static void update(Account editAccount) throws IOException {

		List<Account> accounts = list(); // Lay ve danh sach tai khoan da co trong file accounts.dat

		List<Account> updatedAccounts = new ArrayList<>();

		ReentrantLock bufferlock = new ReentrantLock();

		int MAX_THREAD;
		if (accounts.size() == 1) {
			MAX_THREAD = 1;
		} else {
			MAX_THREAD = accounts.size() - 1;
		}

		ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD); // Threads pool

		int i = 0;
		while (i < accounts.size()) {
			ThreadValidateAndAddAccount threadValidateAndAddAccount = new ThreadValidateAndAddAccount(
					updatedAccounts,
					accounts.get(i),
					editAccount,
					bufferlock
			);
			executorService.execute(threadValidateAndAddAccount);
			i++;
		}

		Future<List<Account>> future = executorService.submit(() -> updatedAccounts);

		try {
			saveUpdate(future.get());
		} catch (ExecutionException e) {
			System.out.println("Co loi trong qua trinh thuc thi!");
		} catch (InterruptedException e) {
			System.out.println("Thread bi tam ngat!");
		}

		executorService.shutdown();

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

class ThreadValidateAndAddAccount implements Runnable {

	private List<Account> bufferAccList;
	private Account account;
	private Account editAccount;
	private ReentrantLock bufferLock;

	public ThreadValidateAndAddAccount(List<Account> bufferAccList, Account account, Account editAccount, ReentrantLock bufferLock) {
		this.bufferAccList = bufferAccList;
		this.account = account;
		this.editAccount = editAccount;
		this.bufferLock = bufferLock;
	}

	@Override
	public void run() {
		Random random = new Random();
		try {
			bufferLock.lock();
			try {
				if (account.getAccountNumber().equals(editAccount.getAccountNumber())) {
					bufferAccList.add(editAccount);
				} else {
					bufferAccList.add(account);
				}
			} finally {
				bufferLock.unlock();
			}
			Thread.sleep(random.nextInt(500));
		} catch (InterruptedException e) {
			System.out.println("Thread was interrupted.");
		}
	}
}

//  public static void update(Account editAccount){
//  Code tuan tu, khong dung multiThread (luu lai de tham khao):
//
//		List<Account> accounts = list();
//		boolean hasExist = accounts.stream().anyMatch(account -> account.getAccountNumber().equals(editAccount.getAccountNumber()));
//
//		List<Account> updatedAccounts;
//			if (!hasExist) {
//					updatedAccounts = new ArrayList<>(accounts);
//			updatedAccounts.add(editAccount);
//			} else {
//			updatedAccounts = new ArrayList<>();
//
//
//			for (Account account : accounts) {
//			if (account.getAccountNumber().equals(editAccount.getAccountNumber())) {
//			updatedAccounts.add(editAccount);
//			} else {
//			updatedAccounts.add(account);
//			}
//			}
//			}
//
//			saveUpdate(updatedAccounts);
//	}