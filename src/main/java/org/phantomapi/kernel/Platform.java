package org.phantomapi.kernel;

import java.io.File;
import java.lang.management.ManagementFactory;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.phantomapi.Phantom;
import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class Platform
{
	public static boolean ENABLE = true;
	
	public static class ENVIRONMENT
	{
		public static boolean canRunBatch()
		{
			return getSystem().getName().toLowerCase().contains("windows");
		}
		
		public static long getProcessId()
		{
			if(Phantom.instance().hasReloaded())
			{
				return -1;
			}
			
			Sigar s = new Sigar();
			long r = -1;
			r = s.getPid();
			s.close();
			return r;
		}
		
		public static long startTime()
		{
			return new File("server.properties").lastModified();
		}
		
		public static String getJavaHome()
		{
			if(!ENABLE)
			{
				return "";
			}
			
			return System.getProperty("java.home");
		}
		
		public static String getJavaVendor()
		{
			if(!ENABLE)
			{
				return "";
			}
			
			return System.getProperty("java.vendor");
		}
		
		public static String getJavaVersion()
		{
			if(!ENABLE)
			{
				return "";
			}
			
			return System.getProperty("java.version");
		}
	}
	
	public static class STORAGE
	{
		public static long getAbsoluteTotalSpace()
		{
			long t = 0;
			
			for(File i : getRoots())
			{
				t += getTotalSpace(i);
			}
			
			return t;
		}
		
		public static long getTotalSpace()
		{
			return getTotalSpace(new File("."));
		}
		
		public static long getTotalSpace(File root)
		{
			return root.getTotalSpace();
		}
		
		public static long getAbsoluteFreeSpace()
		{
			long t = 0;
			
			for(File i : getRoots())
			{
				t += getFreeSpace(i);
			}
			
			return t;
		}
		
		public static long getFreeSpace()
		{
			return getFreeSpace(new File("."));
		}
		
		public static long getFreeSpace(File root)
		{
			return root.getFreeSpace();
		}
		
		public static long getUsedSpace()
		{
			return getTotalSpace() - getFreeSpace();
		}
		
		public static long getUsedSpace(File root)
		{
			return getTotalSpace(root) - getFreeSpace(root);
		}
		
		public static long getAbsoluteUsedSpace()
		{
			return getAbsoluteTotalSpace() - getAbsoluteFreeSpace();
		}
		
		public static File[] getRoots()
		{
			return File.listRoots();
		}
	}
	
	public static class MEMORY
	{
		public static class PHYSICAL
		{
			public static long getTotalMemory()
			{
				if(!ENABLE)
				{
					return 0;
				}
				
				return getSystem().getTotalPhysicalMemorySize();
			}
			
			public static long getFreeMemory()
			{
				if(!ENABLE)
				{
					return 0;
				}
				
				return getSystem().getFreePhysicalMemorySize();
			}
			
			public static long getUsedMemory()
			{
				if(!ENABLE)
				{
					return 0;
				}
				
				return getTotalMemory() - getFreeMemory();
			}
		}
		
		public static class VIRTUAL
		{
			public static long getTotalMemory()
			{
				if(!ENABLE)
				{
					return 0;
				}
				
				return getSystem().getTotalSwapSpaceSize();
			}
			
			public static long getFreeMemory()
			{
				if(!ENABLE)
				{
					return 0;
				}
				
				return getSystem().getFreeSwapSpaceSize();
			}
			
			public static long getUsedMemory()
			{
				if(!ENABLE)
				{
					return 0;
				}
				
				return getTotalMemory() - getFreeMemory();
			}
			
			public static long getCommittedVirtualMemory()
			{
				if(!ENABLE)
				{
					return 0;
				}
				
				return getSystem().getCommittedVirtualMemorySize();
			}
		}
	}
	
	public static class CPU
	{
		public static double getCoreLoad(int core)
		{
			if(Phantom.instance().hasReloaded())
			{
				return getCPULoad();
			}
			
			Sigar s = new Sigar();
			double r = -1;
			
			try
			{
				r = s.getCpuPercList()[core].getUser();
			}
			
			catch(SigarException e)
			{
				e.printStackTrace();
			}
			
			s.close();
			return r;
		}
		
		public static int getCoreMhz(int core)
		{
			if(Phantom.instance().hasReloaded())
			{
				return 1337;
			}
			
			Sigar s = new Sigar();
			int r = -1;
			
			try
			{
				r = s.getCpuInfoList()[core].getMhz();
			}
			
			catch(SigarException e)
			{
				e.printStackTrace();
			}
			
			s.close();
			return r;
		}
		
		public static String getProcessorModel()
		{
			String r = "UNKNOWN";
			
			if(Phantom.instance().hasReloaded())
			{
				return r + " (Phantom was reloaded)";
			}
			
			Sigar s = new Sigar();
			
			try
			{
				r = s.getCpuInfoList()[0].getVendor() + " " + s.getCpuInfoList()[0].getModel();
			}
			
			catch(SigarException e)
			{
				e.printStackTrace();
			}
			
			s.close();
			
			return r;
		}
		
		public static int getAvailableProcessors()
		{
			if(!ENABLE)
			{
				return Runtime.getRuntime().availableProcessors();
			}
			
			return getSystem().getAvailableProcessors();
		}
		
		public static double getCPULoad()
		{
			if(!ENABLE)
			{
				return 0;
			}
			
			return getSystem().getSystemCpuLoad();
		}
		
		public static double getProcessCPULoad()
		{
			if(!ENABLE)
			{
				return 0;
			}
			
			return getSystem().getProcessCpuLoad();
		}
		
		public static String getArchitecture()
		{
			if(!ENABLE)
			{
				return "?";
			}
			
			return getSystem().getArch();
		}
	}
	
	public static String getVersion()
	{
		if(!ENABLE)
		{
			return "?";
		}
		
		return getSystem().getVersion();
	}
	
	public static String getName()
	{
		if(!ENABLE)
		{
			return "?";
		}
		
		return getSystem().getName();
	}
	
	private static OperatingSystemMXBean getSystem()
	{
		return (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	}
}
