package vn.funix.fx18559.java.asm04.model;

import java.io.Serializable;
import java.util.UUID;

public class Transaction implements Serializable {
	private static final long serialVersionUID = 3L;
	private String id;
	private String accountNumber;
	private double amount;
	private String time;
	private boolean status;
	private TransactionType type;

	public Transaction(String accountNumber, double amount, String time, boolean status, TransactionType type) {
		this.id = String.valueOf(UUID.randomUUID());
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.time = time;
		this.status = status;
		this.type = type;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public double getAmount() {
		return amount;
	}

	public String getTime() {
		return time;
	}

	public boolean isStatus() {
		return status;
	}

	public TransactionType getType() {
		return type;
	}

	@Override
	public String toString() {
		return type.toString();
	}
}
