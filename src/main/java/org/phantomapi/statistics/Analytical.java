package org.phantomapi.statistics;

import org.phantomapi.clust.Configurable;

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
