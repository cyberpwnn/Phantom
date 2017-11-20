package org.phantomapi.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.cyberpwn.glang.GList;

/**
 * Archive utilities
 *
 * @author cyberpwn
 *
 */
public class Archives
{
	/**
	 * List all nodes in the archive. Any subdirectories are noted as
	 * "folder/file.name", the same as ZipEntries
	 *
	 * @param archive
	 *            the archive file (jars, zips)
	 * @return an array of entries (as strings)
	 * @throws IOException
	 *             shit happens
	 */
	public static String[] listFiles(File archive) throws IOException
	{
		GList<String> classes = new GList<String>();
		FileInputStream fin = new FileInputStream(archive);
		ZipInputStream zip = new ZipInputStream(fin);

		for(ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
		{
			if(!entry.isDirectory() && entry.getName().endsWith(".class"))
			{
				classes.add(entry.getName());
			}
		}

		zip.close();

		return classes.toArray(new String[classes.size()]);
	}

	/**
	 * Read a resource from the archive based on the given node
	 * (afolder/somefile.txt) into a byte array
	 *
	 * @param archive
	 *            the archive file (jar/zip)
	 * @param node
	 *            the entry node
	 * @return a byte array represending the file's contents
	 * @throws IOException
	 *             shit happens
	 */
	public static byte[] readResource(File archive, String node) throws IOException
	{
		String target = node;
		File jar = archive;
		ZipFile zipFile = new ZipFile(jar);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();

		while(entries.hasMoreElements())
		{
			ZipEntry entry = entries.nextElement();

			if(entry.getName().equals(target))
			{
				byte[] buff = new byte[8192];
				InputStream stream = zipFile.getInputStream(entry);
				ByteArrayOutputStream boas = new ByteArrayOutputStream();
				int read = 0;

				while((read = stream.read(buff)) != -1)
				{
					boas.write(buff, 0, read);
				}

				stream.close();

				return boas.toByteArray();
			}
		}

		zipFile.close();

		return null;
	}
}
