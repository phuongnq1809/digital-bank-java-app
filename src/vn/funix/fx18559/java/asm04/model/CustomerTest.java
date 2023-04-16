package vn.funix.fx18559.java.asm04.model;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CustomerTest {
	private Customer customer;

	@Before
	public void setup() {
		customer = new Customer("001090030789", "QPhuongTest");
	}

	@Test
	public void isCustomerPremium() {
		// Test case return false boi vi du lieu tai khoan cua ham nay duoc get ve tu file accounts.dat
		// nen noi dung file co the bi thay doi hoac file bi xoa.
		// hoac truong hop khong get ve duoc danh sach tai khoan ung voi khach hang hien tai (customer)
		assertFalse(customer.isCustomerPremium());
	}

	@Test
	public void getTotalAccountBalance() {
		// Test case return gia tri default bang 0 boi vi du lieu tai khoan cua ham nay duoc get ve tu file accounts.dat
		// nen noi dung file co the bi thay doi hoac file bi xoa.
		// hoac truong hop khong get ve duoc danh sach tai khoan ung voi khach hang hien tai (customer)
		assertEquals(0, customer.getTotalAccountBalance(), 0);
	}

	@Test
	public void getAccountByAccountNumber() {
		Account account1 = new Account("111222", 100000, "SAVINGS", "001090030789");
		Account account2 = new Account("222333", 100000, "SAVINGS", "001090030789");
		List<Account> accountList = new ArrayList<>();
		accountList.add(account1);
		accountList.add(account2);
		assertNull(customer.getAccountByAccountNumber(accountList, "123456"));
		assertNotNull(customer.getAccountByAccountNumber(accountList, "222333"));
		assertEquals(account1, customer.getAccountByAccountNumber(accountList, "111222"));
		assertNotEquals(account2, customer.getAccountByAccountNumber(accountList, "111222"));
	}
}