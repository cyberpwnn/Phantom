package org.phantomapi.core;

import org.phantomapi.async.A;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.construct.Ticked;
import org.phantomapi.kernel.Platform;
import org.phantomapi.lang.GList;
import org.phantomapi.statistics.Monitorable;
import org.phantomapi.util.C;
import org.phantomapi.util.F;

@Ticked(20)
public class KernelController extends Controller implements Monitorable
{
	private boolean running = false;
	private GList<Double> load;
	private Double loadAverage;
	private Double ploadAverage;
	private Integer mhz;
	private String model;
	
	public KernelController(Controllable parentController)
	{
		super(parentController);
		load = new GList<Double>();
		loadAverage = 0.0;
		ploadAverage = 0.0;
		mhz = null;
		model = null;
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
	
	@Override
	public void onTick()
	{
		sample();
	}
	
	private void sample()
	{
		if(!running)
		{
			running = true;
			
			new A()
			{
				@Override
				public void async()
				{
					if(model == null)
					{
						model = Platform.CPU.getProcessorModel();
					}
					
					if(mhz == null)
					{
						mhz = Platform.CPU.getCoreMhz(0);
					}
					
					loadAverage = Platform.CPU.getCPULoad();
					ploadAverage = Platform.CPU.getProcessCPULoad();
					
					GList<Double> load = KernelController.this.load.copy();
					
					if(load.isEmpty())
					{
						for(int i = 0; i < Platform.CPU.getAvailableProcessors(); i++)
						{
							load.add((double) i / 10.0);
						}
					}
					
					for(int i = 0; i < Platform.CPU.getAvailableProcessors(); i++)
					{
						load.set(i, Platform.CPU.getCoreLoad(i));
					}
					
					KernelController.this.load = load;
					
					running = false;
				}
			};
		}
	}
	
	@Override
	public String getMonitorableData()
	{
		String loadd = "";
		
		int v = 0;
		for(double i : load)
		{
			v++;
			loadd += cpu(i, v) + " ";
		}
		
		return C.LIGHT_PURPLE + F.pc(loadAverage) + " " + loadd;
	}
	
	public String cpu(double d, int id)
	{
		C c;
		
		if(d < 0.1)
		{
			c = C.GREEN;
		}
		
		else if(d < 0.3)
		{
			c = C.YELLOW;
		}
		
		else if(d < 0.6)
		{
			c = C.GOLD;
		}
		
		else if(d < 0.9)
		{
			c = C.RED;
		}
		
		else
		{
			c = C.DARK_RED;
		}
		
		return c + "#";
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public GList<Double> getLoad()
	{
		return load;
	}
	
	public Double getLoadAverage()
	{
		return loadAverage;
	}
	
	public Double getPloadAverage()
	{
		return ploadAverage;
	}
	
	public Integer getMhz()
	{
		return mhz;
	}
	
	public String getModel()
	{
		return model;
	}
}
