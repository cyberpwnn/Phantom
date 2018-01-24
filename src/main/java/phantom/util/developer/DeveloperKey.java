package phantom.util.developer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

public class DeveloperKey
{
	private byte[] key;

	public DeveloperKey(byte[] key)
	{
		this.key = key;
	}

	public DeveloperKey(String developer, int favoriteNumber) throws NoSuchAlgorithmException, IOException
	{
		String a = UUID.randomUUID().toString();
		String b = developer + favoriteNumber + "-?" + a;
		String c = UUID.randomUUID().toString();
		String d = UUID.randomUUID() + c + "-" + UUID.randomUUID() + "=" + UUID.randomUUID();
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		GZIPOutputStream gzo = new GZIPOutputStream(boas);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] h = digest.digest((c + b + a + "$" + c + d).getBytes(StandardCharsets.UTF_8));
		gzo.write(h);
		gzo.close();
		key = boas.toByteArray();
	}

	public byte[] getKey()
	{
		return key;
	}
}
