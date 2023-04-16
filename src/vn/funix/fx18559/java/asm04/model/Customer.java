package vn.funix.fx18559.java.asm04.model;

import vn.funix.fx18559.java.asm04.common.Utilities;
import vn.funix.fx18559.java.asm04.common.CustomerIDValidator;
import vn.funix.fx18559.java.asm04.dao.AccountDao;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	private String customerId;
	private String name;

	public Customer(String customerId, String name) {
		if (CustomerIDValidator.validateCustomerId(customerId)) {
			this.customerId = customerId;
		}
		this.name = name;
	}

	public Customer(List<String> values) {
		this(values.get(0), values.get(1));
	}

	// Kiem tra xem khach hang co phai la Premium hay khong
	// La Premium khi co it nhat 1 tai khoan la Premium
	public boolean isCustomerPremium() {
		if (getAccounts() != null) {
			for (Account account : getAccounts()) {
				if (account.isAccountPremium()) {
					return true;
				}
			}
		}
		return false;
	}

	// Ham them tai khoan
	public void addSavingsAccount(SavingsAccount newAccount) {
		List<Account> accountList = new ArrayList<>();
		if (AccountDao.isFileExisted()) { // Neu da co tai khoan trong bank, kiem tra dieu kien truoc khi them
			List<Account> accountsDatFile = AccountDao.list();
			if (isAccountExisted(accountsDatFile, newAccount)) {
				System.out.println("Tai khoan " + newAccount.getAccountNumber() + " da ton tai trong ngan hang!, them khong thanh cong!");
			} else {
				accountList.add(newAccount);
				System.out.println("Tai khoan " + newAccount.getAccountNumber() + " voi so du " +
						Utilities.formatCurrencyVND(newAccount.getBalance()) + "d duoc them thanh cong!");
				newAccount.input(newAccount.getBalance()); //khoi tao giao dich DEPOSIT cho account khi them moi lan dau
			}
		} else { // them moi lan dau tien
			accountList.add(newAccount);
			System.out.println("Tai khoan " + newAccount.getAccountNumber() + " voi so du " +
					Utilities.formatCurrencyVND(newAccount.getBalance()) + "d duoc them thanh cong!");
			newAccount.input(newAccount.getBalance());
		}

		if (!accountList.isEmpty()) {
			// Luu danh sach tai khoan vao file accounts.dat
			AccountDao.save(accountList);
		} else {
			System.out.println("Khong co tai khoan moi duoc them vao danh sach!");
		}
	}

	// Kiem tra xem 1 Account da ton tai hay chua
	// Su dung bieu thuc lambda voi Predicate interface
	public boolean isAccountExisted(List<Account> accounts, Account newAccount) {
		Predicate<Account> accountCondition = account -> account.getAccountNumber().equals(newAccount.getAccountNumber());
		for (Account account : accounts) {
			if (accountCondition.test(account)) {
				return true;
			}
		}
		return false;
	}

	// Ham lay ve danh sach cac tai khoan tuong ung voi customer hien tai
	// su dung stream va bieu thuc lambda de get ve cac tai khoan ung voi so ID cua khach hang hien tai
	public List<Account> getAccounts() {
		if (AccountDao.isFileExisted()) {
			List<Account> accounts = AccountDao.list();
			return accounts.stream()
					.filter(account -> account.getCustomerId().equals(this.customerId))
					.collect(Collectors.toList());
		}
		return null;
	}

	// Ham lay ra Account tu trong danh sach
	public Account getAccountByAccountNumber(List<Account> accounts, String accountNumber) {
		for (Account account : accounts) {
			if (account.getAccountNumber().equals(accountNumber)) {
				return account;
			}
		}
		return null;
	}

	// Ham chuyen tien
	public void transfers(Scanner sc, SavingsAccount sendSavingsAccount, SavingsAccount receiveSavingsAccount) {

		System.out.print("Nhap so tien muon chuyen: ");
		String amount = Utilities.getKeyStringNumber(sc.nextLine(), sc);
		// Kiem tra dieu kien chuyen tien cua tai khoan gui, neu khong hop le yeu cau nhap lai
		while (!sendSavingsAccount.isAccepted(Double.parseDouble(amount))) {
			System.out.println("So tien chuyen khong hop le!");
			System.out.print("Vui long nhap lai: ");
			amount = Utilities.getKeyStringNumber(sc.nextLine(), sc);
		}

		if (sendSavingsAccount.transfers(receiveSavingsAccount, Double.parseDouble(amount))) {
			try {
				// cap nhat cac tai khoan trong file accounts.dat
				AccountDao.update(sendSavingsAccount);
				AccountDao.update(receiveSavingsAccount);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Chuyen tien thanh cong, bien lai giao dich:");
			sendSavingsAccount.log(Double.parseDouble(amount),
					TransactionType.TRANSFERS,
					receiveSavingsAccount.getAccountNumber());
		} else {
			System.out.println("Chuyen tien khong thanh cong!");
		}

	}

	// Ham rut tien
	public void withdraw(Scanner sc) {
		System.out.print("Nhap so tai khoan muon rut tien: ");
		String accountNumber = Utilities.getKeyStringNumber(sc.nextLine(), sc);
		accountNumber = Utilities.getKeyStringNumberNcharacters(6, accountNumber, sc);

		Account account = getAccountByAccountNumber(getAccounts(), accountNumber);

		while (account == null) {
			System.out.println("So tai khoan khong ton tai trong ngan hang!");
			System.out.print("Vui long nhap lai: ");
			accountNumber = Utilities.getKeyStringNumber(sc.nextLine(), sc);
			accountNumber = Utilities.getKeyStringNumberNcharacters(6, accountNumber, sc);
			account = getAccountByAccountNumber(getAccounts(), accountNumber);
		}

		System.out.println("Ban dang rut tien tu so tai khoan: " + account.getAccountNumber() +
				", loai tai khoan: " + account.getAccountType());

		SavingsAccount savingsAccount = (SavingsAccount) account;
		System.out.print("Moi ban nhap so tien: ");
		String amount = Utilities.getKeyStringNumber(sc.nextLine(), sc);

		// Kiem tra dieu kien rut tien, neu khong hop le yeu cau nhap lai
		while (!savingsAccount.isAccepted(Double.parseDouble(amount))) {
			System.out.println("So tien rut khong hop le!");
			System.out.print("Vui long nhap lai: ");
			amount = Utilities.getKeyStringNumber(sc.nextLine(), sc);
		}

		if (savingsAccount.withdraw(Double.parseDouble(amount))) {
			try {
				AccountDao.update(savingsAccount); // cap nhat tai khoan trong file accounts.dat
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Rut tien thanh cong, bien lai giao dich:");
			savingsAccount.log(Double.parseDouble(amount), TransactionType.WITHDRAW, ""); // in bien lai giao dich
		} else {
			System.out.println("Rut tien khong thanh cong!");
		}

	}

	// Hien thi thong tin cua khach hang va danh sach cac tai khoan (neu co)
	public void displayInformation() {
		System.out.printf("%-12s | %-25s | %-7s | %23s%s\n", getCustomerId(), getName(),
				(isCustomerPremium() ? "Premium" : "Normal"), Utilities.formatCurrencyVND(getTotalAccountBalance()), "đ");
		List<Account> accounts = this.getAccounts();
		if (accounts != null) {
			for (int i = 0; i < accounts.size(); i++) {
				System.out.printf("%s", (i + 1));
				System.out.printf("%11s | %-25s | %33s%s", accounts.get(i).getAccountNumber(),
						accounts.get(i).getAccountType(),
						Utilities.formatCurrencyVND(accounts.get(i).getBalance()), "đ");
				System.out.println();
			}
		} else {
			System.out.println("Khach hang chua co tai khoan nao!");
		}
	}

	// Hien thi thong tin lich su giao dich cua khach hang
	public void displayTransactionInformation() {
		displayInformation(); // Hien thi thong tin khach hang, ds tai khoan
		List<Account> accounts = getAccounts();
		if (accounts != null) {
			System.out.println("*****************************************************************************");
			for (Account account : accounts) {
				if (account.getTransactions() != null) {
					account.displayTransactionsList(); // hien thi thong tin giao dich cua tai khoan
				} else {
					System.out.println("Tai khoan " + account.getAccountNumber() + " chua co giao dich nao!");
				}
			}
		}
	}

	// Ham tinh tong so du cua cac tai khoan ma khach hang co
	public double getTotalAccountBalance() {
		double totalBalance = 0;
		if (getAccounts() != null) {
			for (Account account : getAccounts()) {
				totalBalance += account.getBalance();
			}
		}
		return totalBalance;
	}

	public String getName() {
		return name;
	}

	public String getCustomerId() {
		return customerId;
	}
}
