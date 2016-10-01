package org.phantomapi.clust;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Represents a data file
 * 
 * @author cyberpwn
 */
public class DataFile extends DataCluster implements DataEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Load data from the given file. If it doesnt exist it will be created
	 * 
	 * @param file
	 *            the file
	 * @throws IOException
	 *             shit happens
	 */
	public void load(File file) throws IOException
	{
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
			return;
		}
		
		clear();
		fromData(Files.readAllBytes(file.toPath()));
	}
	
	/**
	 * Save the file. If it does not exist it will be created
	 * 
	 * @param file
	 *            the file
	 * @throws IOException
	 *             this happens
	 */
	public void save(File file) throws IOException
	{
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		
		FileOutputStream fos = new FileOutputStream(file, false);
		fos.write(toData());
		fos.close();
	}
	
	@Override
	public byte[] toData() throws IOException
	{
		return compress();
	}
	
	@Override
	public void fromData(byte[] data) throws IOException
	{
		addCompressed(data);
	}
}
