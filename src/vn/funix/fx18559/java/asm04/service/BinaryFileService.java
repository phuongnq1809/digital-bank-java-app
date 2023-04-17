package vn.funix.fx18559.java.asm04.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileService {

	// Doc noi dung tu file, tra ve List<Objects>
	public static <T> List<T> readFile(String fileName) {
		List<T> objects = new ArrayList<>();
		try (ObjectInputStream file = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
			boolean eof = false;
			while (!eof) {
				try {
					T object = (T) file.readObject();
					objects.add(object);
				} catch (EOFException e) {
					eof = true;
				}
			}
		} catch (EOFException e) {
			return new ArrayList<>();
		} catch (IOException io) {
			System.out.println("IO Exception: " + io.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: " + e.getMessage());
		}
		return objects;
	}

	// Ghi du lieu List<objects> vao file
	public static <T> void writeFile(String fileName, List<T> objects) {

		if (!isFileExisted(fileName)) { // ghi du lieu lan dau
			try (ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
				for (T object : objects) {
					file.writeObject(object);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // Ghi them doi tuong moi vao file da co (ko xoa du lieu cu)
			try (ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName, true))) {
				@Override
				protected void writeStreamHeader() throws IOException {
					reset();
				}
			}) {
				for (T object : objects) {
					file.writeObject(object);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static <T> void writeFile(String fileName, List<T> objects, boolean isUpdate) {

		if (isUpdate) {
			try (ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
				for (T object : objects) {
					file.writeObject(object);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Kiem tra xem file co ton tai hay khong
	public static boolean isFileExisted(String fileName) {
		File f = new File(fileName);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

}
