/*
* Account
*/
package vn.funix.fx18559.java.asm04.model;

import vn.funix.fx18559.java.asm04.common.Utilities;
import vn.funix.fx18559.java.asm04.dao.CustomerDao;
import vn.funix.fx18559.java.asm04.dao.TransactionDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Account implements Serializable {
	private static final long serialVersionUID = 2L;
	private String accountNumber;
	private double balance;
	private String accountType;
	private String customerId;

	public Account(String accountNumber, double balance, String accountType, String customerId) {
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.accountType = accountType;
		this.customerId = customerId;
	}

	// Kiem tra xem co phai tai khoan Premium hay khong
	// La premium khi so du tai khoan >= 10_000_000d
	public boolean isAccountPremium() {
		if (this.balance >= 10000000) return true;
		return false;
	}

	// Tao giao dich them tien khi them moi tai khoan vao bank
	public void input(double amount) {
		createTransaction(this.accountNumber, amount, Utilities.getDateTime(), true, TransactionType.DEPOSIT);
	}

	// Tao them mot giao dich cho account va cap nhat so du tai khoan
	public void createTransaction(String accountNumber, double amount, String time, boolean status, TransactionType type) {
		Transaction transaction = new Transaction(accountNumber, amount, time, status, type);
		List<Transaction> transactionList = new ArrayList<>();
		transactionList.add(transaction);
		TransactionDao.save(transactionList);
	}

	// Hien thi thong tin cac giao dich cua tai khoan
	public void displayTransactionsList() {
		List<Transaction> transactions = this.getTransactions();
		if (transactions != null) {
			for (int i = 0; i < transactions.size(); i++) {
				System.out.print("[GD]");
				System.out.printf("%8s | %-9s | %23s | %24s",
						transactions.get(i).getAccountNumber(),
						transactions.get(i).getType(),
						(Utilities.formatCurrencyVND(transactions.get(i).getAmount()) + "đ"),
						transactions.get(i).getTime());
				System.out.println();
			}
		}
	}

	//
	public Customer getCustomer() {
		List<Customer> customers = CustomerDao.list();
		for (Customer customer : customers) {
			if (customer.getCustomerId().equals(getCustomerId())) {
				return customer;
			}
		}
		return null;
	}

	// Ham lay ve tat ca cac giao dich cua tai khoan
	// su dung stream va bieu thuc lambda de get ve cac giao dich ung voi so ID cua tai khoan hien tai
	public List<Transaction> getTransactions() {
		if (TransactionDao.isFileExisted()) {
			List<Transaction> transactionsFile = TransactionDao.list();
			List<Transaction> accountTransactions = transactionsFile.stream()
					.filter(transaction -> transaction.getAccountNumber().equals(this.accountNumber))
					.collect(Collectors.toList());
			if (!accountTransactions.isEmpty()) {
				return accountTransactions;
			} else {
				return null;
			}
		}
		return null;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public String getAccountType() {
		return accountType;
	}

	public String getCustomerId() {
		return customerId;
	}

}
