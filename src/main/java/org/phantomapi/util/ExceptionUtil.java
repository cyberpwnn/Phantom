package org.phantomapi.util;

/**
 * Exception utils
 * 
 * @author cyberpwn
 */
public class ExceptionUtil
{
	/**
	 * Print the exception
	 * 
	 * @param e
	 *            the exception
	 */
	public static void print(Exception e)
	{
		D d = new D("EX");
		d.f(e.getClass().getSimpleName() + " thrown at " + C.YELLOW + e.getStackTrace()[0].getClassName());
		d.w(e.getMessage());
		
		for(StackTraceElement i : e.getStackTrace())
		{
			d.i("  " + i.getClassName() + "." + C.YELLOW + i.getMethodName() + "(" + i.getLineNumber() + ")");
		}
	}
}
