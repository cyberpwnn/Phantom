package org.phantomapi.clust;

import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;

/**
 * Represents a configurable controller
 * 
 * @author cyberpwn
 */
public abstract class ConfigurableController extends Controller implements Configurable
{
	private String codeName;
	private DataCluster cc;
	
	/**
	 * Create a configurable controller
	 * 
	 * @param parentController
	 *            the parent controller
	 * @param codeName
	 *            the code name for the configurable object
	 */
	public ConfigurableController(Controllable parentController, String codeName)
	{
		super(parentController);
		
		this.codeName = codeName;
		this.cc = new DataCluster();
	}
	
	@Override
	public void onNewConfig()
	{
		
	}
	
	@Override
	public void onReadConfig()
	{
		
	}
	
	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}
	
	@Override
	public String getCodeName()
	{
		return codeName;
	}
	
	@Override
	public abstract void onStart();
	
	@Override
	public abstract void onStop();
}
