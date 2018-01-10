package phantom.util.files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import phantom.util.metrics.Documented;

/**
 * File tools
 *
 * @author cyberpwn
 */
@Documented
public class FS
{
	/**
	 * Read a file fully
	 *
	 * @param f
	 *            the file
	 * @return the byte array or null if the file doesnt exist or is a folder
	 * @throws IOException
	 *             shit happens
	 */
	public static byte[] readFully(File f) throws IOException
	{
		if(f.exists() && f.isFile())
		{
			byte[] buf = new byte[4096];
			int read = 0;
			FileInputStream fin = new FileInputStream(f);
			ByteArrayOutputStream boas = new ByteArrayOutputStream();
			while((read = (fin.read(buf))) != -1)
			{
				boas.write(buf, 0, read);
			}

			return boas.toByteArray();
		}

		return null;
	}

	/**
	 * Write bytes to the file
	 *
	 * @param f
	 *            the file
	 * @param b
	 *            the bytes
	 * @throws IOException
	 *             shit happens
	 */
	public static void writeFully(File f, byte[] b) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(b);
		fos.close();
	}

	/**
	 * Delete all files
	 *
	 * @param f
	 *            the file or folder
	 */
	public static void deleteAll(File f)
	{
		if(f.isDirectory())
		{
			for(File i : f.listFiles())
			{
				deleteAll(i);
			}
		}

		f.delete();
	}
}
