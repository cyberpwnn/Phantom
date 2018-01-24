package phantom.util.developer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Developer
{
	private DeveloperKey key;

	public Developer(byte[] bkey)
	{
		key = new DeveloperKey(bkey);
	}

	public Developer(String name, int number) throws NoSuchAlgorithmException, IOException
	{
		key = new DeveloperKey(name, number);
	}

	public DeveloperKey getKey()
	{
		return key;
	}
}
