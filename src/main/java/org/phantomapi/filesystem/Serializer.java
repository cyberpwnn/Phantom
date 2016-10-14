package org.phantomapi.filesystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.google.common.io.ByteStreams;

/**
 * Serializable utils
 * 
 * @author cyberpwn
 */
public class Serializer
{
	/**
	 * Serialize the object to bytes
	 * 
	 * @param serializable
	 *            the serial object
	 * @return the bytes
	 * @throws IOException
	 *             better be serializable
	 */
	public static byte[] serialize(Serializable serializable) throws IOException
	{
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		GZIPOutputStream gzo = new GZIPOutputStream(boas);
		ObjectOutputStream oos = new ObjectOutputStream(gzo);
		oos.writeObject(serializable);
		oos.close();
		
		return boas.toByteArray();
	}
	
	/**
	 * Deserialize the bytes
	 * 
	 * @param object
	 *            the bytes
	 * @return the object
	 * @throws IOException
	 *             better be serial data
	 * @throws ClassNotFoundException
	 *             non existant class or different version
	 */
	public static Serializable deserialize(byte[] object) throws IOException, ClassNotFoundException
	{
		ByteArrayInputStream bois = new ByteArrayInputStream(object);
		GZIPInputStream gzi = new GZIPInputStream(bois);
		ObjectInputStream ois = new ObjectInputStream(gzi);
		Object o = ois.readObject();
		
		return (Serializable) o;
	}
	
	/**
	 * Serialize an object to file
	 * 
	 * @param serializable
	 *            the object
	 * @param file
	 *            the file
	 * @throws IOException
	 *             shit happens
	 */
	public static void serializeToFile(Serializable serializable, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(serialize(serializable));
		fos.close();
	}
	
	/**
	 * Deserialize the given object
	 * 
	 * @param file
	 *            the file
	 * @return the object
	 * @throws IOException
	 *             shit happens
	 * @throws ClassNotFoundException
	 *             bad shit happens
	 */
	public static Serializable deserializeFromFile(File file) throws IOException, ClassNotFoundException
	{
		FileInputStream fin = new FileInputStream(file);
		Serializable object = deserialize(ByteStreams.toByteArray(fin));
		fin.close();
		
		return object;
	}
}
