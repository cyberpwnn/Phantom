package org.cyberpwn.phantom.statistics;

import org.cyberpwn.phantom.clust.Configurable;

/**
 * Represents an object which can contain statistics
 * 
 * @author cyberpwn
 * @param <T>
 *            the statistic holder
 */
public interface Analytical<T extends StatisticHolder> extends Configurable
{
	/**
	 * Get the statistics holder for this object
	 * 
	 * @return the statistic holder
	 */
	public T getStatistics();
}
