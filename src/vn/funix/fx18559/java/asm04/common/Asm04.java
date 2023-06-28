/*
* Main Class
*/
package vn.funix.fx18559.java.asm04.common;

import vn.funix.fx18559.java.asm04.exception.CustomerIdNotValidException;
import vn.funix.fx18559.java.asm04.model.Customer;
import vn.funix.fx18559.java.asm04.model.DigitalBank;
import vn.funix.fx18559.java.asm04.service.TextFileService;

import java.util.Scanner;

public class Asm04 {

	private static final String AUTHOR = "FX18559";
	private static final String VERSION = "V4.0.0";

	private static final DigitalBank activeBank = new DigitalBank("QP BANK");
	private static Scanner sc = new Scanner(System.in);
	private static final int[] keyNumbersMainMenu = {0, 1, 2, 3, 4, 5, 6};

	// Ham hien thi menu chinh cua chuong trinh
	private static void showMainMenu() {
		System.out.println("+----------------+-----------------------------------------+----------------+");
		System.out.println("| NGAN HANG SO | " + AUTHOR + "@" + VERSION + "						              			|");
		System.out.println("+----------------+-----------------------------------------+----------------+");
		System.out.println("| 1. Danh sach khach hang							              			|");
		System.out.println("| 2. Nhap danh sach khach hang						             			|");
		System.out.println("| 3. Them tai khoan ATM								              			|");
		System.out.println("| 4. Chuyen tien									              			|");
		System.out.println("| 5. Rut tien										              			|");
		System.out.println("| 6. Tra cuu lich su giao dich						              			|");
		System.out.println("| 0. Thoat											              			|");
		System.out.println("+----------------+-----------------------------------------+----------------+");
	}

	// Chuc nang 1. Danh sach khach hang
	private static void showCustomersFunction() {
		System.out.println("++++++++++++++++------------ DANH SACH KHACH HANG -----------++++++++++++++++");
		activeBank.showCustomers();
		System.out.println("-----------------------------------------------------------------------------");
	}


	// Chuc nang 2. Nhap danh sach khach hang
	private static void addCustomersFunction() {
		System.out.println("+++++++++++++------------- NHAP DANH SACH KHACH HANG -----------+++++++++++++");

		System.out.print("Nhap duong dan den file (file/customers.txt): ");
		String filePathInput = sc.nextLine();
		String filePath = "src/vn/funix/fx18559/java/asm04/" + filePathInput;

		while (!Utilities.isValidFilePathFormat(filePathInput) || !TextFileService.isFileExisted(filePath)) {
			System.out.println("File khong ton tai, vui long nhap lai!");
			System.out.print("Nhap duong dan den file (file/customers.txt): ");
			filePathInput = sc.nextLine();
			filePath = "src/vn/funix/fx18559/java/asm04/" + filePathInput;
		}

		//Goi ham them khach hang
		activeBank.addCustomers(filePath);
	}

	// Ham kiem tra so CCCD co hop le hay khong
	private static boolean validateId(String idInput) throws CustomerIdNotValidException {
		if (CustomerIDValidator.validateCustomerId(idInput)) {
			return true;
		} else {
			throw new CustomerIdNotValidException("So CCCD khong hop le!");
		}
	}

	// Ham kiem tra va tra ve so CCCD hop le
	// Su dung cac method cua class Utilities de kiem tra input nhap vao
	private static String getIdValid(String idInput, Scanner sc) {
		// Yeu cau day so nhap vao phai co dung 12 chu so
		idInput = Utilities.getKeyStringNumberNcharacters(12, idInput, sc);
		// Kiem tra so CCCD co hop le hay khong
		boolean isValid = false;
		while (!isValid) {
			try {
				if (validateId(idInput)) {
					isValid = true;
				}
			} catch (CustomerIdNotValidException e) {
				System.out.println(e.getMessage());
				System.out.print("Vui long nhap lai: ");
				idInput = Utilities.getKeyStringNumber(sc.nextLine(), sc); // yeu cau nhap so
				idInput = Utilities.getKeyStringNumberNcharacters(12, idInput, sc); // tra ve chuoi co dung 12 chu so
			}
		}

		return idInput;
	}

	// Chuc nang 3. Them tai khoan ATM
	private static void addAccountForCustomerFunction() {
		if (activeBank.isBankHasCustomers()) {
			System.out.print("Nhap so CCCD cua khach hang: ");
			String idInput = Utilities.getKeyStringNumber(sc.nextLine(), sc);
			String customerId = getIdValid(idInput, sc); // Get ve so CCCD da hop le

			activeBank.addSavingAccount(sc, customerId);

		} else {
			System.out.println("Chua co khach hang nao trong bank, ban hay them khach hang truoc khi them tai khoan!");
		}
	}

	// Chuc nang 4. Chuyen tien
	private static void transfersFunction() {
		if (activeBank.isBankHasCustomers()) {
			System.out.print("Nhap so CCCD cua khach hang: ");
			String idInput = Utilities.getKeyStringNumber(sc.nextLine(), sc);
			String customerId = getIdValid(idInput, sc); // Get ve so CCCD da hop le

			activeBank.transfers(sc, customerId);

		} else {
			System.out.println("Chua co khach hang nao trong bank, ban hay them thong tin!");
		}
	}

	// Chuc nang 5. Rut tien
	private static void withdrawFromAccount() {
		if (activeBank.isBankHasCustomers()) {
			System.out.print("Nhap so CCCD cua khach hang: ");
			String idInput = Utilities.getKeyStringNumber(sc.nextLine(), sc);
			String customerId = getIdValid(idInput, sc); // Get ve so CCCD da hop le

			activeBank.withdraw(sc, customerId);

		} else {
			System.out.println("Chua co khach hang nao trong bank, ban hay them thong tin!");
		}
	}

	// Chuc nang 6. Lich su giao dich
	private static void showTransactionHistory() {
		if (activeBank.isBankHasCustomers()) {
			System.out.print("Nhap so CCCD cua khach hang: ");
			String idInput = Utilities.getKeyStringNumber(sc.nextLine(), sc);
			String customerId = getIdValid(idInput, sc); // Get ve so CCCD da hop le

			Customer customer = activeBank.getCustomerById(activeBank.getCustomers(), customerId);

			if (customer != null) {
				System.out.println("+----------------+-----------------------------------------+----------------+");
				System.out.println("|							  LICH SU GIAO DICH 	 	                    |");
				System.out.println("+----------------+-----------------------------------------+----------------+");
				activeBank.showTransactionsHistory(customer);
				System.out.println("-----------------------------------------------------------------------------");
			} else {
				System.out.println("Khong tim thay khach hang voi so CCCD " + customerId + ", tac vu khong thanh cong!");
			}

		} else {
			System.out.println("Chua co khach hang nao trong bank, ban hay them thong tin!");
		}
	}

	public static void main(String[] args) {

		boolean isQuit = false;
		while (!isQuit) {
			showMainMenu();
			System.out.print("Moi ban chon chuc nang: ");

			String keyMainInput = Utilities.getKeyStringNumber(sc.nextLine(), sc); // Yeu cau nhap so
			int keyMainValid = Integer.parseInt(Utilities.getKeyValid(keyMainInput, keyNumbersMainMenu, sc)); // Tra ve phim dung theo menu

			if (keyMainValid != 0) {
				switch (keyMainValid) {
					case 1: //Chuc nang 1. Danh sach khach hang
						showCustomersFunction();
						break;
					case 2: // Chuc nang 2. Nhap danh sach khach hang
						addCustomersFunction();
						break;
					case 3: // Chuc nang 3. Them tai khoan ATM
						addAccountForCustomerFunction();
						break;
					case 4: // Chuc nang 4. Chuyen tien
						transfersFunction();
						break;
					case 5: // Chuc nang 5. Rut tien
						withdrawFromAccount();
						break;
					case 6: // Chuc nang 6. Lich su giao dich
						showTransactionHistory();
						break;
					default:
						break;
				}
			} else { // 0. Thoat
				isQuit = true;
				System.out.println("Cam on ban da su dung dich vu cua chung toi! Hen gap lai!");
			}
		}
	}
}
