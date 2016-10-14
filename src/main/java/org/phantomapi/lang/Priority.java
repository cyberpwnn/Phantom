package org.phantomapi.lang;

public enum Priority
{
	LOWEST, VERY_LOW, LOW, NORMAL, HIGH, VERY_HIGH, HIGHEST;
	
	public static GList<Priority> topDown()
	{
		return new GList<Priority>().qadd(HIGHEST).qadd(VERY_HIGH).qadd(HIGH).qadd(NORMAL).qadd(LOW).qadd(VERY_LOW).qadd(LOWEST);
	}
}
