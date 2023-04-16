package vn.funix.fx18559.java.asm04.model;

import org.junit.*;

import static org.junit.Assert.*;

public class SavingsAccountTest {
	private SavingsAccount sendAccount;

	@Before
	public void setup() {
		sendAccount = new SavingsAccount("123456", 5000000, "001090030789"); //balance = 5_000_000d
	}

	@Test
	public void isAccepted() {
		assertTrue(sendAccount.isAccepted(500000));//amount hop le
		assertFalse(sendAccount.isAccepted(40000)); // amount < 50_000d
		assertFalse(sendAccount.isAccepted(523400)); // amount ko la boi so cua 10_000d
		assertFalse(sendAccount.isAccepted(5000000)); // so du sau khi rut < 50_000d
	}

	@Test
	public void withdraw() {
		assertTrue(sendAccount.withdraw(500000));
		assertFalse(sendAccount.withdraw(40000));
		assertFalse(sendAccount.withdraw(523400));
		assertFalse(sendAccount.withdraw(5000000));
	}

	@Test
	public void transfers() {
		SavingsAccount receiveAccount = new SavingsAccount("234567", 5000000, "001090030789");
		assertTrue(sendAccount.transfers(receiveAccount, 500000));
		assertFalse(sendAccount.transfers(receiveAccount, 40000));
		assertFalse(sendAccount.transfers(receiveAccount, 523400));
		assertFalse(sendAccount.transfers(receiveAccount, 5000000));
	}

}