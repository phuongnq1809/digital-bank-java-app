package vn.funix.fx18559.java.asm04.model;

public interface Transfer {
	boolean transfers(Account receiveAccount, double amount);

	boolean withdraw(double amount);

	boolean isAccepted(double amount);
}
