package phantom.util.data;

import java.io.UTFDataFormatException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.phantomapi.Phantom;

public class StringUtil
{
	public static byte[] utfToBytes(String str) throws UTFDataFormatException
	{
		int strlen = str.length();
		int utflen = 0;
		int c, count = 0;

		for(int i = 0; i < strlen; i++)
		{
			c = str.charAt(i);

			if((c >= 0x0001) && (c <= 0x007F))
			{
				utflen++;
			}

			else if(c > 0x07FF)
			{
				utflen += 3;
			}

			else
			{
				utflen += 2;
			}
		}

		byte[] data = new byte[utflen + 2];

		if(utflen > 65535)
		{
			throw new UTFDataFormatException("encoded string too long: " + utflen + " bytes");
		}

		byte[] bytearr = new byte[utflen + 2];
		bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
		bytearr[count++] = (byte) ((utflen >>> 0) & 0xFF);

		int i = 0;

		for(i = 0; i < strlen; i++)
		{
			c = str.charAt(i);
			if(!((c >= 0x0001) && (c <= 0x007F)))
				break;
			bytearr[count++] = (byte) c;
		}

		for(; i < strlen; i++)
		{
			c = str.charAt(i);

			if((c >= 0x0001) && (c <= 0x007F))
			{
				bytearr[count++] = (byte) c;

			}

			else if(c > 0x07FF)
			{
				bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
				bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			}

			else
			{
				bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
				bytearr[count++] = (byte) (0x80 | ((c >> 0) & 0x3F));
			}
		}

		for(i = 0; i < utflen; i++)
		{
			data[i] = bytearr[i];
		}

		return data;
	}

	public static String bytesToUTF(byte[] data) throws UTFDataFormatException
	{
		int utflen = (short) ((data[0] << 8) + (data[1] << 0));
		byte[] bytearr = new byte[utflen];
		char[] chararr = new char[utflen];
		int c, char2, char3;
		int count = 0;
		int chararr_count = 0;

		for(int i = 0; i < utflen; i++)
		{
			bytearr[i] = data[i + 2];
		}

		while(count < utflen)
		{
			c = (int) bytearr[count] & 0xff;

			if(c > 127)
			{
				break;
			}

			count++;
			chararr[chararr_count++] = (char) c;
		}

		while(count < utflen)
		{
			c = (int) bytearr[count] & 0xff;

			switch(c >> 4)
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
					count++;
					chararr[chararr_count++] = (char) c;
					break;
				case 12:
				case 13:
					count += 2;

					if(count > utflen)
					{
						throw new UTFDataFormatException("malformed input: partial character at end");
					}

					char2 = (int) bytearr[count - 1];

					if((char2 & 0xC0) != 0x80)
					{
						throw new UTFDataFormatException("malformed input around byte " + count);
					}

					chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));

					break;
				case 14:
					count += 3;

					if(count > utflen)
					{
						throw new UTFDataFormatException("malformed input: partial character at end");
					}

					char2 = (int) bytearr[count - 2];
					char3 = (int) bytearr[count - 1];

					if(((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
					{
						throw new UTFDataFormatException("malformed input around byte " + (count - 1));
					}

					chararr[chararr_count++] = (char) (((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
					break;
				default:
					throw new UTFDataFormatException("malformed input around byte " + count);
			}
		}

		return new String(chararr, 0, chararr_count);
	}

	public String worldToString(World w)
	{
		return w.getName();
	}

	public World worldFromString(String w)
	{
		return Phantom.getWorld(w);
	}

	public String vectortoString(Vector v)
	{
		return vectortoString(v, true);
	}

	public String vectortoString(Vector v, boolean accurate)
	{
		return (accurate ? v.getX() : v.getBlockX()) + ";" + (accurate ? v.getY() : v.getBlockY()) + ";" + (accurate ? v.getZ() : v.getBlockZ());
	}

	public Vector vectorFromString(String v)
	{
		String[] a = v.split(";");

		if(a.length == 3)
		{
			return new Vector(Double.valueOf(a[0]), Double.valueOf(a[1]), Double.valueOf(a[2]));
		}

		return null;
	}

	public String locationToString(Location l)
	{
		return locationToString(l, true, true);
	}

	public String locationToString(Location l, boolean accurate, boolean direction)
	{
		return worldToString(l.getWorld()) + ";" + (accurate ? l.getX() : l.getBlockX()) + ";" + (accurate ? l.getY() : l.getBlockY()) + ";" + (accurate ? l.getZ() : l.getBlockZ()) + (direction ? ";" + l.getYaw() + ";" + l.getPitch() : "");
	}

	public Location locationFromString(String l)
	{
		String[] a = l.split(";");
		World w = null;
		double x = 0;
		double y = 0;
		double z = 0;
		float yaw = 0f;
		float pitch = 0f;

		if(a.length >= 4)
		{
			w = worldFromString(a[0]);
			x = Double.valueOf(a[1]);
			y = Double.valueOf(a[2]);
			z = Double.valueOf(a[3]);

			if(a.length >= 6)
			{
				yaw = Float.valueOf(a[4]);
				pitch = Float.valueOf(a[5]);
			}
		}

		if(w != null)
		{
			return new Location(w, x, y, z, yaw, pitch);
		}

		return null;
	}
}
