package org.phantomapi.nbt;

import java.util.UUID;

public class Modifier
{
	
	private String _name;
	private double _amount;
	private int _operation;
	private UUID _uuid;
	
	public static Modifier fromNBT(NBTTagCompound data)
	{
		return new Modifier(data.getString("Name"), data.getDouble("Amount"), data.getInt("Operation"), new UUID(data.getLong("UUIDMost"), data.getLong("UUIDLeast")));
	}
	
	public Modifier(String name, double amount, int operation)
	{
		this(name, amount, operation, UUID.randomUUID());
	}
	
	public Modifier(String name, double amount, int operation, UUID uuid)
	{
		_name = name;
		_amount = amount;
		_operation = Math.max(Math.min(operation, 2), 0);
		_uuid = uuid;
	}
	
	public final String getName()
	{
		return _name;
	}
	
	public final double getAmount()
	{
		return _amount;
	}
	
	public final int getOperation()
	{
		return _operation;
	}
	
	public final UUID getUUID()
	{
		return _uuid;
	}
	
	public NBTTagCompound toNBT()
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setString("Name", _name);
		data.setDouble("Amount", _amount);
		data.setInt("Operation", _operation);
		data.setLong("UUIDMost", _uuid.getMostSignificantBits());
		data.setLong("UUIDLeast", _uuid.getLeastSignificantBits());
		return data;
	}
	
}
