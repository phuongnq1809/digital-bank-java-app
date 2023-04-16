package vn.funix.fx18559.java.asm04.model;

import vn.funix.fx18559.java.asm04.common.Utilities;

public class SavingsAccount extends Account implements Transfer, Report {
	private final double SAVINGS_ACCOUNT_MAX_WITHDRAW = 5000000;

	public SavingsAccount(String accountNumber, double balance, String customerId) {
		super(accountNumber, balance, "SAVINGS", customerId);
	}

	// Kiem tra dieu kien rut tien/chuyen tien
	@Override
	public boolean isAccepted(double amount) {
		if ((amount >= 50000.00) && (amount % 10000.00 == 0) && ((getBalance() - amount) >= 50000.00)) {
			if (isAccountPremium()) {
				return true;
			} else {
				if (amount <= SAVINGS_ACCOUNT_MAX_WITHDRAW)
					return true;
			}
		}
		return false;
	}

	// Rut tien
	@Override
	public boolean withdraw(double amount) {
		double newBalance;
		if (isAccepted(amount)) {
			newBalance = getBalance() - amount;
			setBalance(newBalance);
			// them lich su giao dich thanh cong vao file transactions.dat
			createTransaction(getAccountNumber(), -amount, Utilities.getDateTime(), true, TransactionType.WITHDRAW);
			return true;
		} else {
			// ghi lai lich su giao dich that bai
			createTransaction(getAccountNumber(), -amount, Utilities.getDateTime(), false, TransactionType.WITHDRAW);
			return false;
		}
	}

	// Chuyen tien
	@Override
	public boolean transfers(Account receiveAccount, double amount) {

		if (isAccepted(amount)) { // dieu kien rut tien hop le
			// Tru tien cua tai khoan chuyen va ghi lai lich su giao dich
			this.setBalance(this.getBalance() - amount);
			createTransaction(this.getAccountNumber(), -amount, Utilities.getDateTime(), true, TransactionType.TRANSFERS);

			// Cong tien cho tai khoan nhan va ghi lai lich su giao dich
			receiveAccount.setBalance(receiveAccount.getBalance() + amount);
			createTransaction(receiveAccount.getAccountNumber(), amount, Utilities.getDateTime(), true, TransactionType.TRANSFERS);

			return true;
		} else {
			// ghi lai lich su giao dich that bai cua tai khoan chuyen
			createTransaction(this.getAccountNumber(), -amount, Utilities.getDateTime(), false, TransactionType.TRANSFERS);
			return false;
		}
	}

	// In bien lai giao dich
	@Override
	public void log(double amount, TransactionType type, String receiveAccount) {

		if (type.equals(TransactionType.WITHDRAW) && receiveAccount.equals("")) {
			Transaction lastWithdrawTransaction = getTransactions().get(getTransactions().size() - 1);
			System.out.println("+------------+-----------------------+------------+");
			System.out.println("|            BIEN LAI RUT TIEN SAVINGS            |");
			System.out.printf("| NGAY G/D: %37s |%n", lastWithdrawTransaction.getTime());
			System.out.printf("| ATM ID: %39s |%n", "DIGITAL-BANK-ATM 2022");
			System.out.printf("| SO TK: %40s |%n", lastWithdrawTransaction.getAccountNumber());
			System.out.printf("| SO TIEN RUT: %33s%s |%n", Utilities.formatCurrencyVND(amount), "đ");
			System.out.printf("| SO DU TK: %36s%s |%n", Utilities.formatCurrencyVND(getBalance()), "đ");
			System.out.printf("| PHI + VAT: %35s%s |%n", "0", "đ");
			System.out.println("+------------+-----------------------+------------+");

		} else if (type.equals(TransactionType.TRANSFERS)) {
			Transaction lastTransfersTransaction = getTransactions().get((getTransactions().size() - 2));
			System.out.println("+------------+-----------------------+------------+");
			System.out.println("|           BIEN LAI CHUYEN TIEN SAVINGS          |");
			System.out.printf("| NGAY G/D: %37s |%n", lastTransfersTransaction.getTime());
			System.out.printf("| ATM ID: %39s |%n", "DIGITAL-BANK-ATM 2022");
			System.out.printf("| SO TK: %40s |%n", lastTransfersTransaction.getAccountNumber());
			System.out.printf("| SO TK NHAN: %35s |%n", receiveAccount);
			System.out.printf("| SO TIEN CHUYEN: %30s%s |%n", Utilities.formatCurrencyVND(amount), "đ");
			System.out.printf("| SO DU TK: %36s%s |%n", Utilities.formatCurrencyVND(getBalance()), "đ");
			System.out.printf("| PHI + VAT: %35s%s |%n", "0", "đ");
			System.out.println("+------------+-----------------------+------------+");
		}


	}
}
