package org.phantomapi.clust;

import java.sql.SQLException;

/**
 * Represents a data manager driver
 * 
 * @author cyberpwn
 */
public abstract class REM
{
	protected MySQL sql;
	
	/**
	 * Initialize the drivers
	 */
	public REM()
	{
		sql = MySQL.get();
	}
	
	/**
	 * Does the given configurable object exist in the base?
	 * 
	 * @param c
	 *            the configurable object
	 * @return true if it does
	 * @throws ClassNotFoundException
	 *             shit happens
	 * @throws SQLException
	 *             shit happens
	 */
	public abstract boolean exists(Configurable c) throws ClassNotFoundException, SQLException;
	
	/**
	 * Read the contents of the object from the base into the configurable
	 * object leaving behind defaults in the database
	 * 
	 * @param c
	 *            the configurable object
	 * @throws ClassNotFoundException
	 *             shit happens
	 * @throws SQLException
	 *             shit happens
	 */
	public abstract void read(Configurable c) throws ClassNotFoundException, SQLException;
	
	/**
	 * Write the contents of the configurable object into the base
	 * 
	 * @param c
	 *            the configurable object
	 * @throws ClassNotFoundException
	 *             shit happens
	 * @throws SQLException
	 *             shit happens
	 */
	public abstract void write(Configurable c) throws ClassNotFoundException, SQLException;
	
	/**
	 * Drop the data on the base represented by the given configurable object
	 * (delete)
	 * 
	 * @param c
	 *            the configurable object
	 * @throws ClassNotFoundException
	 *             shit happens
	 * @throws SQLException
	 *             shit happens
	 */
	public abstract void drop(Configurable c) throws ClassNotFoundException, SQLException;
	
	/**
	 * Get the sql driver
	 * 
	 * @return the sql driver
	 */
	public MySQL getSql()
	{
		return sql;
	}
}
