package vn.funix.fx18559.java.asm04.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
	@Test
	public void isAccountPremium_true() {
		Account account = new Account("123456", 20000000, "SAVINGS", "001090030784");//balance >= 10_000_000d
		assertTrue(account.isAccountPremium());
	}

	@Test
	public void isAccountPremium_false() {
		Account account = new Account("123456", 5000000, "SAVINGS", "001090030784"); //balance < 10_000_000d
		assertFalse(account.isAccountPremium());
	}

}