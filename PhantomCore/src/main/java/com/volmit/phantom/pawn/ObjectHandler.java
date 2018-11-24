package com.volmit.phantom.pawn;

public interface ObjectHandler<T>
{
	public Object toString(T t);
	
	public T fromString(Object o);
	
	public Class<? extends T> getHandledClass();
}
