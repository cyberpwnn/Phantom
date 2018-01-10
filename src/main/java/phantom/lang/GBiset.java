package phantom.lang;

import java.io.Serializable;

import phantom.util.metrics.Documented;

/**
 * A Biset
 *
 * @author cyberpwn
 *
 * @param <A>
 *            the first object type
 * @param <B>
 *            the second object type
 */
@Documented
public class GBiset<A, B> implements Serializable
{
	private static final long serialVersionUID = 1L;
	private A a;
	private B b;

	/**
	 * Create a new Biset
	 *
	 * @param a
	 *            the first object
	 * @param b
	 *            the second object
	 */
	public GBiset(A a, B b)
	{
		this.a = a;
		this.b = b;
	}

	/**
	 * Get the object of the type A
	 *
	 * @return the first object
	 */
	public A getA()
	{
		return a;
	}

	/**
	 * Set the first object
	 *
	 * @param a
	 *            the first object A
	 */
	public void setA(A a)
	{
		this.a = a;
	}

	/**
	 * Get the second object
	 *
	 * @return the second object
	 */
	public B getB()
	{
		return b;
	}

	/**
	 * Set the second object
	 *
	 * @param b
	 */
	public void setB(B b)
	{
		this.b = b;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
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
		GBiset<?, ?> other = (GBiset<?, ?>) obj;
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
		return true;
	}

}
