package vn.funix.fx18559.java.asm04.model;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DigitalBankTest {
	private DigitalBank digitalBank;
	private List<Customer> customerList;
	private List<Account> accountList;
//	List<Account>
//	List<Customer>

	@Before
	public void setup() {
		digitalBank = new DigitalBank("QPBankTest");
		customerList = new ArrayList<>();
		accountList = new ArrayList<>();
	}

	@Test
	public void isBankHasCustomers() {
		assertTrue(digitalBank.isBankHasCustomers()); // neu file chua du lieu khach hang co ton tai
//		assertFalse(digitalBank.isBankHasCustomers()); // neu file chua du lieu khach hang khong ton tai
	}

	@Test
	public void getCustomers() {
		assertNotNull(digitalBank.getCustomers()); // neu ham isBankHasCustomers() return true
//		assertNull(digitalBank.getCustomers()); // neu ham isBankHasCustomers() return false
	}

	@Test
	public void getAccounts() {
		assertNotNull(digitalBank.getAccounts()); // neu file chua du lieu tai khoan co ton tai
//		assertNull(digitalBank.getAccounts()); // neu file chua du lieu tai khoan khong ton tai
	}

	@Test
	public void isCustomerExisted() {
		Customer customer1 = new Customer("001090030789", "QPhuongTest");
		Customer customer2 = new Customer("001090030777", "QPhuongTest2");
		Customer customer3 = new Customer("001090030888", "QPhuongTest3");
		customerList.add(customer1);
		customerList.add(customer2);

		assertTrue(digitalBank.isCustomerExisted(customerList, customer1));
		assertTrue(digitalBank.isCustomerExisted(customerList, customer2));
		assertFalse(digitalBank.isCustomerExisted(customerList, customer3));
	}

	@Test
	public void getCustomerById() {
		Customer customer1 = new Customer("001090030789", "QPhuongTest");
		Customer customer2 = new Customer("001090030777", "QPhuongTest2");
		Customer customer3 = new Customer("001090030888", "QPhuongTest3");
		customerList.add(customer1);
		customerList.add(customer2);

		assertNull(digitalBank.getCustomerById(customerList, "001122334455"));
		assertNotNull(digitalBank.getCustomerById(customerList, "001090030789"));
		assertEquals(customer2, digitalBank.getCustomerById(customerList, "001090030777"));
		assertNotEquals(customer3, digitalBank.getCustomerById(customerList, "001090030888"));
	}

	@Test
	public void getAccountByAccountNumber() {
		Account account1 = new Account("111222", 100000, "SAVINGS", "001090030789");
		Account account2 = new Account("222333", 100000, "SAVINGS", "001090030777");
		accountList.add(account1);
		accountList.add(account2);

		assertNull(digitalBank.getAccountByAccountNumber(accountList, "123456"));
		assertNotNull(digitalBank.getAccountByAccountNumber(accountList, "111222"));
		assertEquals(account2, digitalBank.getAccountByAccountNumber(accountList, "222333"));
		assertNotEquals(account1, digitalBank.getAccountByAccountNumber(accountList, "123456"));
	}
}