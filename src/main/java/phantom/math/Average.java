package phantom.math;

/**
 * Represents a custom sized rolling average
 *
 * @author cyberpwn
 */
public class Average
{
	private double[] values;
	private double average;
	private boolean dirty;

	/**
	 * Represents a rolling average
	 * 
	 * @param size
	 *            the maximum size (radius) of the average
	 */
	public Average(int size)
	{
		values = new double[size];
		DoubleArrayUtils.fill(values, 0);
		average = 0;
		dirty = false;
	}

	/**
	 * Put a value into the average
	 * 
	 * @param i
	 *            the value
	 */
	public void put(double i)
	{
		DoubleArrayUtils.shiftRight(values, i);
		dirty = true;
	}

	/**
	 * Get the last computed average. If new data has entered the roll, then it
	 * is first computed.
	 * 
	 * @return the average of all of the numbers.
	 */
	public double getAverage()
	{
		if(dirty)
		{
			calculateAverage();
			return getAverage();
		}

		return average;
	}

	private void calculateAverage()
	{
		double d = 0;

		for(double i : values)
		{
			d += i;
		}

		average = d / (double) values.length;
		dirty = false;
	}
}
