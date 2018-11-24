package com.volmit.phantom.test;

import java.lang.reflect.InvocationTargetException;
import com.volmit.phantom.json.JSONObject;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.pawn.ListType;
import com.volmit.phantom.pawn.UniversalParser;

public class Test
{
	private int i = 123;
	private double d = 1.23;
	private long l = System.currentTimeMillis();
	private float f = 1.55f;
	private byte y = 4;
	private short s = 2;
	private boolean b = false;
	private String r = "String";
	
	@ListType(String.class)
	private GList<String> gls = new GList<String>().qadd("A").qadd("B");
	
	@ListType(Double.class)
	private GList<Double> gld = new GList<Double>().qadd(2d).qadd(4d);
	
	@ListType(Float.class)
	private GList<Float> glf = new GList<Float>().qadd(1f).qadd(2f);
	
	public static void main(String[] a) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Test t = new Test();
		JSONObject j = UniversalParser.toJSON(t);
		System.out.println(j.toString(4));
	}
}
