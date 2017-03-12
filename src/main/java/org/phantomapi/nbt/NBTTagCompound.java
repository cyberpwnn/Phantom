package org.phantomapi.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class NBTTagCompound extends NBTBase implements Cloneable
{
	static NBTBase createNBTBase(byte type, String s, DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws Exception
	{
		NBTBase nbtbase = NBTBase.createTag(type);
		nbtbase.load(datainput, i, nbtreadlimiter);
		return nbtbase;
	}
	
	static Map getDataAsMap(NBTTagCompound nbttagcompound)
	{
		return nbttagcompound.getMap();
	}
	
	private static void writeTag(String s, NBTBase nbtbase, DataOutput dataoutput) throws Exception
	{
		try
		{
			dataoutput.writeByte(nbtbase.getTypeId());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		if(nbtbase.getTypeId() != 0)
		{
			try
			{
				dataoutput.writeUTF(s);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			nbtbase.write(dataoutput);
		}
	}
	
	private static String readString(DataInput datainput, NBTReadLimiter nbtreadlimiter)
	{
		try
		{
			return datainput.readUTF();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private static byte readByte(DataInput datainput, NBTReadLimiter nbtreadlimiter)
	{
		try
		{
			return datainput.readByte();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return 0x00;
		}
	}
	
	private Map map = new HashMap();
	
	public NBTTagCompound()
	{
	}
	
	public byte getNBTBaseType(String s)
	{
		NBTBase nbtbase = (NBTBase) this.getMap().get(s);
		return nbtbase != null ? nbtbase.getTypeId() : 0;
	}
	
	public Set c()
	{
		return this.getMap().keySet();
	}
	
	@Override
	public NBTBase clone()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		Iterator iterator = this.getMap().keySet().iterator();
		
		while(iterator.hasNext())
		{
			String s = (String) iterator.next();
			
			nbttagcompound.set(s, ((NBTBase) this.getMap().get(s)).clone());
		}
		
		return nbttagcompound;
	}
	
	@Override
	public boolean equals(Object object)
	{
		if(super.equals(object))
		{
			NBTTagCompound nbttagcompound = (NBTTagCompound) object;
			
			return this.getMap().entrySet().equals(nbttagcompound.getMap().entrySet());
		}
		else
		{
			return false;
		}
	}
	
	public NBTBase get(String s)
	{
		return (NBTBase) this.getMap().get(s);
	}
	
	public boolean getBoolean(String s)
	{
		return this.getByte(s) != 0;
	}
	
	public byte getByte(String s)
	{
		try
		{
			return !this.getMap().containsKey(s) ? 0 : ((NBTNumber) this.getMap().get(s)).asByte();
		}
		catch(ClassCastException classcastexception)
		{
			return (byte) 0;
		}
	}
	
	public byte[] getByteArray(String s)
	{
		try
		{
			return !this.getMap().containsKey(s) ? new byte[0] : ((NBTTagByteArray) this.getMap().get(s)).getData();
		}
		catch(ClassCastException classcastexception)
		{
			throw new RuntimeException();
		}
	}
	
	public NBTTagCompound getCompound(String s)
	{
		try
		{
			return !this.getMap().containsKey(s) ? new NBTTagCompound() : (NBTTagCompound) this.getMap().get(s);
		}
		catch(ClassCastException classcastexception)
		{
			throw new RuntimeException();
		}
	}
	
	public double getDouble(String s)
	{
		try
		{
			return !this.getMap().containsKey(s) ? 0.0D : ((NBTNumber) this.getMap().get(s)).asDouble();
		}
		catch(ClassCastException classcastexception)
		{
			return 0.0D;
		}
	}
	
	public float getFloat(String s)
	{
		try
		{
			return !this.getMap().containsKey(s) ? 0.0F : ((NBTNumber) this.getMap().get(s)).asFloat();
		}
		catch(ClassCastException classcastexception)
		{
			return 0.0F;
		}
	}
	
	public int getInt(String s)
	{
		try
		{
			return !this.getMap().containsKey(s) ? 0 : ((NBTNumber) this.getMap().get(s)).asInt();
		}
		catch(ClassCastException classcastexception)
		{
			return 0;
		}
	}
	
	public int[] getIntArray(String s)
	{
		try
		{
			return !this.getMap().containsKey(s) ? new int[0] : ((NBTTagIntArray) this.getMap().get(s)).getData();
		}
		catch(ClassCastException classcastexception)
		{
			throw new RuntimeException();
		}
	}
	
	public NBTTagList getList(String s)
	{
		try
		{
			if(this.getNBTBaseType(s) != 9)
			{
				return new NBTTagList();
			}
			else
			{
				NBTTagList nbttaglist = (NBTTagList) this.getMap().get(s);
				
				return nbttaglist;
			}
		}
		catch(ClassCastException classcastexception)
		{
			throw new RuntimeException();
		}
	}
	
	public long getLong(String s)
	{
		try
		{
			return !this.getMap().containsKey(s) ? 0L : ((NBTNumber) this.getMap().get(s)).asLong();
		}
		catch(ClassCastException classcastexception)
		{
			return 0L;
		}
	}
	
	public short getShort(String s)
	{
		try
		{
			return !this.getMap().containsKey(s) ? 0 : ((NBTNumber) this.getMap().get(s)).asShort();
		}
		catch(ClassCastException classcastexception)
		{
			return (short) 0;
		}
	}
	
	public String getString(String s)
	{
		String str = "";
		try
		{
			str = !this.getMap().containsKey(s) ? "" : ((NBTBase) this.getMap().get(s)).toString();
		}
		catch(ClassCastException classcastexception)
		{
			return "";
		}
		
		return str.replaceAll("\"", "");
	}
	
	@Override
	public byte getTypeId()
	{
		return (byte) 10;
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode() ^ this.getMap().hashCode();
	}
	
	public boolean hasKey(String s)
	{
		return this.getMap().containsKey(s);
	}
	
	public boolean hasKeyOfType(String s, int i)
	{
		byte b0 = this.getNBTBaseType(s);
		
		return b0 == i ? true : i != 99 ? false : b0 == 1 || b0 == 2 || b0 == 3 || b0 == 4 || b0 == 5 || b0 == 6;
	}
	
	public boolean isEmpty()
	{
		return this.getMap().isEmpty();
	}
	
	@Override
	void load(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws Exception
	{
		if(i > 512)
		{
			throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
		}
		else
		{
			this.getMap().clear();
			
			byte b0;
			
			while((b0 = readByte(datainput, nbtreadlimiter)) != 0)
			{
				String s = readString(datainput, nbtreadlimiter);
				
				nbtreadlimiter.readBytes(16 * s.length());
				NBTBase nbtbase = createNBTBase(b0, s, datainput, i + 1, nbtreadlimiter);
				
				this.getMap().put(s, nbtbase);
			}
		}
	}
	
	public void remove(String s)
	{
		this.getMap().remove(s);
	}
	
	public void set(String s, NBTBase nbtbase)
	{
		this.getMap().put(s, nbtbase);
	}
	
	public void setBoolean(String s, boolean flag)
	{
		this.setByte(s, (byte) (flag ? 1 : 0));
	}
	
	public void setByte(String s, byte b0)
	{
		this.getMap().put(s, new NBTTagByte(b0));
	}
	
	public void setByteArray(String s, byte[] abyte)
	{
		this.getMap().put(s, new NBTTagByteArray(abyte));
	}
	
	public void setDouble(String s, double d0)
	{
		this.getMap().put(s, new NBTTagDouble(d0));
	}
	
	public void setFloat(String s, float f)
	{
		this.getMap().put(s, new NBTTagFloat(f));
	}
	
	public void setInt(String s, int i)
	{
		this.getMap().put(s, new NBTTagInt(i));
	}
	
	public void setIntArray(String s, int[] aint)
	{
		this.getMap().put(s, new NBTTagIntArray(aint));
	}
	
	public void setLong(String s, long i)
	{
		this.getMap().put(s, new NBTTagLong(i));
	}
	
	public void setShort(String s, short short1)
	{
		this.getMap().put(s, new NBTTagShort(short1));
	}
	
	public void setString(String s, String s1)
	{
		this.getMap().put(s, new NBTTagString(s1));
	}
	
	@Override
	public String toString()
	{
		String s = "{";
		
		String s1;
		
		for(Iterator iterator = this.getMap().keySet().iterator(); iterator.hasNext(); s = s + s1 + ':' + this.getMap().get(s1) + ',')
		{
			s1 = (String) iterator.next();
		}
		
		return s + "}";
	}
	
	@Override
	void write(DataOutput dataoutput) throws Exception
	{
		Iterator iterator = this.getMap().keySet().iterator();
		
		while(iterator.hasNext())
		{
			String s = (String) iterator.next();
			NBTBase nbtbase = (NBTBase) this.getMap().get(s);
			
			writeTag(s, nbtbase, dataoutput);
		}
		
		try
		{
			dataoutput.writeByte(0);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}
}
