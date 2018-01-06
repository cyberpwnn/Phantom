package phantom.util.files;

import java.io.File;

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
