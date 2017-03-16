package org.phantomapi.data;

public enum PackageType
{
	STRING((byte) 10, DString.class),
	SHORT((byte) 9, DShort.class),
	LONG((byte) 8, DLong.class),
	INTEGER((byte) 7, DInteger.class),
	FLOAT((byte) 6, DFloat.class),
	DOUBLE((byte) 5, DDouble.class),
	CHAR((byte) 4, DChar.class),
	BYTES((byte) 3, DBytes.class),
	BYTE((byte) 2, DByte.class),
	BOOLEAN((byte) 1, DBoolean.class),
	DATA_PACK((byte) 0, DataPack.class);
	
	private byte type;
	private Class<? extends DataHandle> clazz;
	
	private PackageType(byte type, Class<? extends DataHandle> clazz)
	{
		this.type = type;
		this.clazz = clazz;
	}
	
	public byte getType()
	{
		return type;
	}	
	
	public Class<? extends DataHandle> getClazz()
	{
		return clazz;
	}
	
	public DataHandle getInstance()
	{
		try
		{
			return getClazz().getConstructor().newInstance();
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static PackageType getType(byte type)
	{
		for(PackageType i : PackageType.values())
		{
			if(i.getType() == type)
			{
				return i;
			}
		}
		
		return null;
	}
	
	public static DataHandle getInstance(byte type)
	{
		PackageType t = getType(type);
		
		if(t == null)
		{
			return null;
		}
		
		return t.getInstance();
	}
}
