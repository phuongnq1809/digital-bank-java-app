package vn.funix.fx18559.java.asm04.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Utilities {

	// Ham tra ve string la 1 chuoi so
	public static String getKeyStringNumber(String strInput, Scanner sc) {
		// Goi ham kiem tra chuoi nhap vao co phai la so hay khong
		while (!isStringNumber(strInput)) {
			System.out.print("Vui long nhap so: ");
			strInput = sc.nextLine();
		}
		return strInput;
	}

	// Ham kiem tra input nhap vao co phai la so hay khong
	public static boolean isStringNumber(String strInput) {
		Pattern pattern = Pattern.compile("[0-9]+");
		return pattern.matcher(strInput).matches();
	}

	// Ham tra ve string la 1 chuoi ky tu
	public static String getKeyStringAlpha(String strInput, Scanner sc) {
		// Goi ham kiem tra chuoi nhap vao co phai la ki tu hay khong
		while (!isStringAlpha(strInput)) {
			System.out.print("Vui long nhap ky tu: ");
			strInput = sc.nextLine();
		}
		return strInput;
	}

	// Ham kiem tra input nhap vao co phai la chuoi ki tu hay khong
	public static boolean isStringAlpha(String strInput) {
		Pattern pattern = Pattern.compile("[a-z A-Z]+");
		return pattern.matcher(strInput).matches();
	}

	// Ham tra ve chuoi ki tu so co n phan tu
	public static String getKeyStringNumberNcharacters(int n, String strInput, Scanner sc) {
		while (strInput.length() != n) {
			System.out.print("Vui long nhap day so co " + n + " chu so: ");
			strInput = getKeyStringNumber(sc.nextLine(), sc);
		}
		return strInput;
	}

	// Ham tra ve phim chuc nang dung theo menu
	public static String getKeyValid(String keyInput, int[] numbers, Scanner sc) {
		boolean isInvalid = true;
		while (isInvalid) {
			for (int i = 0; i < numbers.length; i++) {
				if (Integer.parseInt(keyInput) == numbers[i]) {
					isInvalid = false;
				}
			}
			if (!isInvalid) {
				break;
			} else {
				System.out.print("Vui long nhap dung phim chuc nang: ");
				keyInput = getKeyStringNumber(sc.nextLine(), sc);
			}
		}
		return keyInput;
	}

	// Ham dinh dang hien thi kieu tien VND
	public static String formatCurrencyVND(double money) {
		if (money == 0) {
			return "0";
		} else {
			DecimalFormat formatter = new DecimalFormat("###,###,###.00");
			return formatter.format(money);
		}
	}

	// Ham tra ve thoi gian hien tai o thoi diem goi
	public static String getDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		return formatter.format(date);
	}

	public static boolean isValidFilePathFormat(String filePathInput) {
		if (filePathInput.equals("file") || filePathInput.equals("file/")
				|| filePathInput.startsWith("/") || filePathInput.startsWith(".")) {
			return false;
		}
		return true;
	}
}
