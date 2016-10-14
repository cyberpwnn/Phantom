package org.phantomapi.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;

/**
 * Archive utils
 * 
 * @author cyberpwn
 */
public class ArchiveUtils
{
	/**
	 * Create a zip archive
	 * 
	 * @param fileSet
	 *            the set of files GMap<FROM, INTERNAL>
	 * @param destination
	 *            the destination for the zip file
	 * @throws FileNotFoundException
	 *             shit happens
	 * @throws IOException
	 *             shit happens
	 */
	public static void createZipArchive(GMap<File, File> fileSet, File destination) throws FileNotFoundException, IOException
	{
		FileOutputStream fos = new FileOutputStream(destination);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		for(File i : fileSet.k())
		{
			File from = i;
			File to = fileSet.get(from);
			Integer length;
			FileInputStream fis = new FileInputStream(from);
			ZipEntry zipEntry = new ZipEntry(to.getPath().startsWith("\\") ? to.getPath().substring(1) : to.getPath());
			zos.putNextEntry(zipEntry);
			
			byte[] bytes = new byte[1024];
			
			while((length = fis.read(bytes)) >= 0)
			{
				zos.write(bytes, 0, length);
			}
			
			zos.closeEntry();
			fis.close();
		}
		
		zos.close();
		fos.close();
	}
	
	/**
	 * Extract a zip archive to a root destination
	 * 
	 * @param archive
	 *            the archive
	 * @param rootDestination
	 *            the root destination to put files
	 * @throws ZipException
	 *             shit happens
	 * @throws IOException
	 *             shit happens
	 */
	public static void extractZipArchive(File archive, File rootDestination) throws ZipException, IOException
	{
		rootDestination.mkdirs();
		ZipFile zipFile = new ZipFile(archive);
		Enumeration<?> enu = zipFile.entries();
		
		while(enu.hasMoreElements())
		{
			ZipEntry zipEntry = (ZipEntry) enu.nextElement();
			String name = zipEntry.getName();
			
			File file = new File(rootDestination, name);
			if(name.endsWith("/"))
			{
				file.mkdirs();
				
				continue;
			}
			
			File parent = file.getParentFile();
			
			if(parent != null)
			{
				parent.mkdirs();
			}
			
			Integer length;
			InputStream is = zipFile.getInputStream(zipEntry);
			FileOutputStream fos = new FileOutputStream(file);
			
			byte[] bytes = new byte[1024];
			
			while((length = is.read(bytes)) >= 0)
			{
				fos.write(bytes, 0, length);
			}
			
			is.close();
			fos.close();
			
		}
		zipFile.close();
	}
	
	/**
	 * Get all files from root
	 * 
	 * @param root
	 *            the root
	 * @return the files
	 */
	public static GList<File> allFiles(File root)
	{
		GList<File> files = new GList<File>();
		
		for(File i : root.listFiles())
		{
			if(i.isFile())
			{
				files.add(i);
			}
			
			else
			{
				files.add(allFiles(i));
			}
		}
		
		return files;
	}
	
	/**
	 * Crop the files
	 * 
	 * @param cropper
	 *            the cropper
	 * @param files
	 *            the files to crop
	 * @return the cropped files
	 */
	public static GList<File> cropFiles(File cropper, GList<File> files)
	{
		GList<File> newFiles = new GList<File>();
		
		for(File i : files)
		{
			newFiles.add(cropFile(cropper, i));
		}
		
		return newFiles;
	}
	
	/**
	 * Crop a file
	 * 
	 * @param cropper
	 *            the cropper
	 * @param file
	 *            the file to be cropped
	 * @return the cropped file
	 */
	public static File cropFile(File cropper, File file)
	{
		if(file.getPath().startsWith(cropper.getPath()))
		{
			return new File(file.getPath().replace(cropper.getPath(), ""));
		}
		
		return file;
	}
}
