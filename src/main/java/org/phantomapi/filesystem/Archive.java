package org.phantomapi.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents an archive
 * 
 * @author cyberpwn
 */
public interface Archive
{
	/**
	 * Add a file to the archive
	 * 
	 * @param file
	 *            the file
	 * @param internal
	 *            the internal file location
	 */
	public void add(File file, File internal);
	
	/**
	 * Remove a file by it's internal path
	 * 
	 * @param internal
	 *            the internal path
	 */
	public void remove(File internal);
	
	/**
	 * Compress the referenced file to an archive
	 * 
	 * @param destination
	 *            the archive destination
	 */
	public void compress(File destination) throws FileNotFoundException, IOException;
	
	/**
	 * Get this archive type
	 * 
	 * @return the archive type
	 */
	public ArchiveType getType();
}
