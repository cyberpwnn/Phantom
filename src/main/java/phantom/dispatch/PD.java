package phantom.dispatch;

public class PD
{
	private static final D pd = new D("Phantom");
	
	public static void l(String message)
	{
		pd.l(message);
	}
	
	public static void w(String message)
	{
		pd.w(message);
	}
	
	public static void f(String message)
	{
		pd.f(message);
	}
	
	public static void v(String message)
	{
		pd.v(message);
	}
}
