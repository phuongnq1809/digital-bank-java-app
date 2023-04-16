package vn.funix.fx18559.java.asm04.model;

import java.util.UUID;

public class Bank {
	private String bankId;
	private String bankName;

	public Bank(String bankName) {
		this.bankId = String.valueOf(UUID.randomUUID());
		this.bankName = bankName;
	}
}
