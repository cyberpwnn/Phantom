package phantom.math;

import phantom.util.metrics.Documented;

/**
 * For some odd reason this is useful.
 *
 * @author cyberpwn
 */
@Documented
public class DoubleArrayUtils
{
	/**
	 * Shift a double array to the right, pushing the given value into the
	 * made-vacant spot near the 0 index (left).
	 *
	 * @param values
	 *            the values to shift
	 * @param push
	 *            the number to push
	 */
	public static void shiftRight(double[] values, double push)
	{
		for(int index = values.length - 2; index >= 0; index--)
		{
			values[index + 1] = values[index];
		}

		values[0] = push;
	}

	/**
	 * Wrap the values to the right. Simply pushes all values to the right, the
	 * value popped off the list is moved to the 0 index (left) vacant slot.
	 *
	 * @param values
	 *            the values to roll
	 */
	public static void wrapRight(double[] values)
	{
		double last = values[values.length - 1];
		shiftRight(values, last);
	}

	/**
	 * Fill the array with a value
	 *
	 * @param values
	 *            the values array
	 * @param value
	 *            the value to fill it with
	 */
	public static void fill(double[] values, double value)
	{
		for(int i = 0; i < values.length; i++)
		{
			values[i] = value;
		}
	}
}
