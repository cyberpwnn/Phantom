package phantom.lang;

import java.io.Serializable;

public class GTriset<A, B, C> implements Serializable
{
	private static final long serialVersionUID = 1912465707826963942L;
	private A a;
	private B b;
	private C c;

	public GTriset(A a, B b, C c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public A getA()
	{
		return a;
	}

	public void setA(A a)
	{
		this.a = a;
	}

	public B getB()
	{
		return b;
	}

	public void setB(B b)
	{
		this.b = b;
	}

	public C getC()
	{
		return c;
	}

	public void setC(C c)
	{
		this.c = c;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		GTriset<?, ?, ?> other = (GTriset<?, ?, ?>) obj;
		if(a == null)
		{
			if(other.a != null)
				return false;
		}
		else if(!a.equals(other.a))
			return false;
		if(b == null)
		{
			if(other.b != null)
				return false;
		}
		else if(!b.equals(other.b))
			return false;
		if(c == null)
		{
			if(other.c != null)
				return false;
		}
		else if(!c.equals(other.c))
			return false;
		return true;
	}
}
