package lab.storm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectByteOperations {
	public static byte[] object2byte(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Object byte2Object(byte[] cacheValue) {
		ByteArrayInputStream bis = new ByteArrayInputStream(cacheValue);
		try {
			ObjectInputStream ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
