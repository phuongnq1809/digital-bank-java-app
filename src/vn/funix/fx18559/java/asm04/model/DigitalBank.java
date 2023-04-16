package vn.funix.fx18559.java.asm04.model;

import vn.funix.fx18559.java.asm04.common.CustomerIDValidator;
import vn.funix.fx18559.java.asm04.dao.AccountDao;
import vn.funix.fx18559.java.asm04.dao.CustomerDao;
import vn.funix.fx18559.java.asm04.service.TextFileService;
import vn.funix.fx18559.java.asm04.common.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class DigitalBank extends Bank {
	public DigitalBank(String name) {
		super(name);
	}

	// Them khach hang vao bank tu file
	public void addCustomers(String fileName) {

		// doc du lieu khach hang tu file customers.txt
		List<List<String>> customersTxtFile = TextFileService.readFile(fileName);

		List<Customer> customerList = new ArrayList<>();

		for (List<String> customerInfo : customersTxtFile) {
			if (CustomerIDValidator.validateCustomerId(customerInfo.get(0))) { // kiem tra dieu kien CCCD hop le hay ko
				Customer newCustomer = new Customer(customerInfo);

				if (isBankHasCustomers()) { // neu da ton tai file customers.dat
					// doc du lieu da co trong file de kiem tra xem khach hang co ton tai hay chua
					List<Customer> customersDatFile = CustomerDao.list();

					if (!isCustomerExisted(customersDatFile, newCustomer)) {
						customerList.add(newCustomer);
						System.out.println("Da them thanh cong khach hang " + newCustomer.getCustomerId() + " vao danh sach khach hang!");
					} else {
						System.out.println("Khach hang " + newCustomer.getCustomerId() + " da ton tai, them khong thanh cong!");
					}
				} else { // them moi lan dau tien
					customerList.add(newCustomer);
					System.out.println("Da them thanh cong khach hang " + newCustomer.getCustomerId() + " vao danh sach khach hang!");
				}

			} else {
				System.out.println("So CCCD: " + customerInfo.get(0) + " khong hop le!");
			}
		}

		if (!customerList.isEmpty()) {
			// Luu danh sach khach hang vao file customers.dat
			CustomerDao.save(customerList);
		} else {
			System.out.println("Khong co khach hang duoc them moi vao danh sach!");
		}

	}

	// Them tai khoan moi vao bank
	public void addSavingAccount(Scanner sc, String customerId) {
		Customer customer = getCustomerById(getCustomers(), customerId);

		if (customer != null) {
			// Neu tim thay khach hang, hien thi thong tin ve khach hang: ten, CCCD
			System.out.println("Ban dang them tai khoan ATM cho khach hang co ten: " +
					customer.getName() + " voi so CCCD: " + customer.getCustomerId());

			System.out.print("Nhap so tai khoan gom 6 chu so: ");
			String accountNumber = Utilities.getKeyStringNumber(sc.nextLine(), sc);
			accountNumber = Utilities.getKeyStringNumberNcharacters(6, accountNumber, sc);

			System.out.print("Nhap so du cho tai khoan (>= 50.000d): ");
			String accountBalance = Utilities.getKeyStringNumber(sc.nextLine(), sc);
			while (Double.parseDouble(accountBalance) < 50000) {
				System.out.println("So du tai khoan khong duoc nho hon 50.000d");
				System.out.print("Vui long nhap lai so du hop le: ");
				accountBalance = Utilities.getKeyStringNumber(sc.nextLine(), sc);
			}

			SavingsAccount savingsAccount = new SavingsAccount(accountNumber,
					Double.parseDouble(accountBalance),
					customer.getCustomerId());
			customer.addSavingsAccount(savingsAccount);

		} else {
			System.out.println("Khong tim thay khach hang voi so CCCD " + customerId + ", tac vu khong thanh cong!");
		}
	}

	// Kiem tra xem 1 Customer da ton tai hay chua
	// Su dung bieu thuc lambda voi Predicate interface
	public boolean isCustomerExisted(List<Customer> customers, Customer newCustomer) {
		Predicate<Customer> customerCondition = customer -> customer.getCustomerId().equals(newCustomer.getCustomerId());
		for (Customer customer : customers) {
			if (customerCondition.test(customer)) {
				return true;
			}
		}
		return false;
	}

	// Ham kiem tra xem da co khach hang trong bank hay chua
	public boolean isBankHasCustomers() {
		if (CustomerDao.isFileExisted()) {
			return true;
		} else {
			return false;
		}
	}

	// Hien thi danh sach khach hang co trong bank
	public void showCustomers() {
		if (isBankHasCustomers()) {
			// doc du lieu khach hang da co trong file
			List<Customer> customersDatFile = CustomerDao.list();
			for (Customer customer : customersDatFile) {
				customer.displayInformation();
			}
		} else {
			System.out.println("Chua co khach hang nao trong danh sach, ban hay them thong tin!");
		}
	}

	// Lay ve danh sach khach hang trong file customers.dat
	public List<Customer> getCustomers() {
		if (isBankHasCustomers()) {
			// doc va tra ve du lieu da co trong file
			return CustomerDao.list();
		}
		return null;
	}

	public Customer getCustomerById(List<Customer> customers, String customerId) {
		for (Customer customer : customers) {
			if (customer.getCustomerId().equals(customerId)) {
				return customer;
			}
		}
		return null;
	}

	// Lay ve danh sach tai khoan co trong file accounts.dat
	public List<Account> getAccounts() {
		if (AccountDao.isFileExisted()) {
			return AccountDao.list();
		}
		return null;
	}

	// Lay ve 1 Account trong danh sach
	public Account getAccountByAccountNumber(List<Account> accounts, String accountNumber) {
		for (Account account : accounts) {
			if (account.getAccountNumber().equals(accountNumber)) {
				return account;
			}
		}
		return null;
	}

	// Chuyen tien (co the chuyen den so tai khoan cua khach hang khac trong bank
	public void transfers(Scanner sc, String customerId) {
		Customer customer = getCustomerById(getCustomers(), customerId);
		if (customer != null) {
			customer.displayInformation(); // hien thi thong tin khach hang
			if (customer.getAccounts() != null) {
				System.out.print("Nhap so tai khoan muon chuyen tien: ");
				String sendAccountNumber = Utilities.getKeyStringNumber(sc.nextLine(), sc);
				sendAccountNumber = Utilities.getKeyStringNumberNcharacters(6, sendAccountNumber, sc);
				Account sendAccount = customer.getAccountByAccountNumber(customer.getAccounts(), sendAccountNumber);

				while (sendAccount == null) {
					System.out.println("So tai khoan khong ton tai hoac khong dung voi khach hang hien tai!");
					System.out.print("Vui long nhap lai: ");
					sendAccountNumber = Utilities.getKeyStringNumber(sc.nextLine(), sc);
					sendAccountNumber = Utilities.getKeyStringNumberNcharacters(6, sendAccountNumber, sc);
					sendAccount = customer.getAccountByAccountNumber(customer.getAccounts(), sendAccountNumber);
				}

				System.out.print("Nhap so tai khoan nhan: ");
				String receiveAccountNumber = Utilities.getKeyStringNumber(sc.nextLine(), sc);
				receiveAccountNumber = Utilities.getKeyStringNumberNcharacters(6, receiveAccountNumber, sc);
				Account receiveAccount = this.getAccountByAccountNumber(this.getAccounts(), receiveAccountNumber);

				while (receiveAccount == null || receiveAccountNumber.equals(sendAccountNumber)) {
					System.out.println("So tai khoan khong ton tai hoac trung voi tai khoan chuyen!");
					System.out.print("Vui long nhap lai: ");
					receiveAccountNumber = Utilities.getKeyStringNumber(sc.nextLine(), sc);
					receiveAccountNumber = Utilities.getKeyStringNumberNcharacters(6, receiveAccountNumber, sc);
					receiveAccount = this.getAccountByAccountNumber(this.getAccounts(), receiveAccountNumber);
				}

				System.out.println("Ban dang chuyen tien tu tai khoan: " + sendAccount.getAccountNumber() +
						" | " + sendAccount.getCustomer().getName());
				System.out.println("Gui den so tai khoan: " + receiveAccount.getAccountNumber() +
						" | " + receiveAccount.getCustomer().getName());

				SavingsAccount sendSavingsAccount = (SavingsAccount) sendAccount;
				SavingsAccount receiveSavingsAccount = (SavingsAccount) receiveAccount;

				customer.transfers(sc, sendSavingsAccount, receiveSavingsAccount);

			} else {
				System.out.println("Khach hang chua co tai khoan, vui long them tai khoan truoc khi chuyen tien!");
			}
		} else {
			System.out.println("Khong tim thay khach hang voi so CCCD " + customerId + ", tac vu khong thanh cong!");
		}
	}

	// Rut tien
	public void withdraw(Scanner scanner, String customerId) {
		Customer customer = getCustomerById(getCustomers(), customerId);

		if (customer != null) {
			customer.displayInformation();
			if (customer.getAccounts() != null) {
				customer.withdraw(scanner);
			} else {
				System.out.println("Khach hang chua co tai khoan, vui long them tai khoan truoc khi rut tien!");
			}
		} else {
			System.out.println("Khong tim thay khach hang voi so CCCD " + customerId + ", tac vu khong thanh cong!");
		}
	}

	// Hien thi thong tin lich su giao dich cua khach hang
	public void showTransactionsHistory(Customer customer) {
		customer.displayTransactionInformation();
	}

}
