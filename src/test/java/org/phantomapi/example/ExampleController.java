package org.phantomapi.example;

import org.phantomapi.clust.Comment;
import org.phantomapi.clust.Configurable;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.Keyed;
import org.phantomapi.clust.Tabled;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;

@Tabled("table_name")
@Ticked(0)
public class ExampleController extends Controller implements Configurable
{
	//Define it as PUBLIC and fill in the value
	@Keyed("some.name")
	public String someName = "Some Value";
	
	//Comment your fields
	@Comment("This is a \nMultiline Comment")
	@Keyed("some.value")
	public int someVal = 665;
	
	@Comment("Wrappers can be used aswell")
	@Keyed("some.wrapper")
	public Double someWrapper = 3.54543452;
	
	private DataCluster cc;
	
	public ExampleController(Controllable parentController)
	{
		super(parentController);
		
		cc = new DataCluster();
	}
	
	@Override
	public void onStart()
	{
		//Load via SQL
		loadMysql(this);
		
		//Or Save it
		saveMysql(this);
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public void onTick()
	{
		
	}

	@Override
	public void onNewConfig()
	{
		// Dynamic (not using it)
	}

	@Override
	public void onReadConfig()
	{
		// Dynamic (not using it)
	}

	@Override
	public DataCluster getConfiguration()
	{
		return cc;
	}

	@Override
	public String getCodeName()
	{
		return "config";
	}
}
