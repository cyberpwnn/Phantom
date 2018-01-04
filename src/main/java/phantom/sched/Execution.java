package phantom.sched;

public abstract class Execution implements Runnable
{
	public static int id = 0;
	public int idx = id++;

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + idx;
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
		Execution other = (Execution) obj;
		if(idx != other.idx)
			return false;
		return true;
	}
}
