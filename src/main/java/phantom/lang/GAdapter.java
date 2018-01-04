package phantom.lang;

import phantom.util.metrics.Documented;

/**
 * A GAdapter for adapting objects to other objects
 *
 * @author cyberpwn
 * @param <FROM>
 *            the from object
 * @param <TO>
 *            the to object
 */
@Documented
public abstract class GAdapter<FROM, TO> implements Adapter<FROM, TO>
{
	@Override
	public TO adapt(FROM from)
	{
		return onAdapt(from);
	}

	@Override
	public abstract TO onAdapt(FROM from);
}
