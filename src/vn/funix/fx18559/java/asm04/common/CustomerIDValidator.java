package vn.funix.fx18559.java.asm04.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CustomerIDValidator {

	/* Khoi tao du lieu ma tinh, thanh pho, noi dung nhap tu file "ma-tinh-tp.txt"
		Mang gom 63 hang (tuong ung 63 tinh thanh) va 2 cot (cot 1: ma tinh/tp; cot 2: ten tinh/tp tuong ung) */
	private static final String[][] idCities = inputArrayData("src/vn/funix/fx18559/java/asm04/file/ma-tinh-tp.txt", 63, 2);

	/* Khoi tao du lieu ma the ky, gioi tinh, noi dung nhap tu file "century-and-gender.txt"
		Mang gom 5 hang (tuong ung voi 5 the ky quy dinh) va 3 cot
		(cot 1: 2 so dau cua nam sinh tuong ung voi the ky; cot 2: ma gioi tinh nam; cot 3: ma gioi tinh nu) */
	private static final String[][] idCenturyGenders = inputArrayData("src/vn/funix/fx18559/java/asm04/file/century-and-gender.txt", 5, 3);

	// Ham kiem tra xem CCCD nhap vao co hop le hay khong (return true or false)
	public static boolean validateCustomerId(String idInput) {
		/* Kiem tra cac dieu kien xem co hop le hay khong
		- Ma tinh/tp
		- Ma gioi tinh
		- 2 so cuoi cua nam sinh */
		boolean isCityValid = false;
		boolean isGenderValid = false;
		boolean isYearOfBirthValid = false;

		for (int i = 0; i < idCities.length; i++) {
			if ((idInput.substring(0, 3)).equals(idCities[i][0])) {
				isCityValid = true;
				break; // thoat khoi vong lap for
			}
		}

		for (int j = 0; j < idCenturyGenders.length; j++) {
			for (int k = 0; k < idCenturyGenders[j].length; k++) {
				if ((idInput.substring(3, 4)).equals(idCenturyGenders[j][k])) {
					isGenderValid = true;
					break; //thoat khoi vong lap for

				}
			}
		}

		String yearOfBirth = idInput.substring(4, 6);
		if (Integer.parseInt(yearOfBirth) >= 0 && Integer.parseInt(yearOfBirth) <= 99) {
			isYearOfBirthValid = true;
		}

		if (isCityValid && isGenderValid && isYearOfBirthValid) {
			return true;
		} else {
			return false;
		}
	}

	// Ham nhap du lieu tu file, tra ve mang 2 chieu
	private static String[][] inputArrayData(String fileName, int arrayRow, int arrayCol) {
		String[][] arrData = new String[arrayRow][arrayCol];
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int row = 0;
			String line = "";
			// Nhap du lieu cho mang tu noi dung cua file (theo tung dong)
			while ((line = bufferedReader.readLine()) != null) {
				if (arrayCol == 2) {
					arrData[row][0] = line.substring(0, 3);
					arrData[row][1] = line.substring(4);
				} else if (arrayCol == 3) {
					arrData[row][0] = line.substring(0, 2);
					arrData[row][1] = line.substring(3, 4);
					arrData[row][2] = line.substring(5);
				}
				row++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Loi: Khong tim thay file.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrData;
	}
}

