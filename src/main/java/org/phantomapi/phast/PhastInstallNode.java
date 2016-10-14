package org.phantomapi.phast;

import java.io.File;
import java.net.URL;
import org.phantomapi.Phantom;
import org.phantomapi.lang.GTime;
import org.phantomapi.network.FileDownload;
import org.phantomapi.sync.Task;
import org.phantomapi.util.C;
import org.phantomapi.util.D;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.util.PluginUtil;

/**
 * A Node for shutting down the server
 * 
 * @author cyberpwn
 */
public class PhastInstallNode extends PhastNode
{
	public PhastInstallNode()
	{
		super("install");
	}
	
	@Override
	public String phastHelp()
	{
		return "install [url][name] - Installs a plugin";
	}
	
	@Override
	public void on(String[] args)
	{
		if(args.length == 2)
		{
			try
			{
				D d = new D("Phast Installer");
				URL url = new URL(args[0]);
				File dest = new File(Phantom.instance().getDataFolder().getParentFile(), args[1]);
				FileDownload fd = new FileDownload(url, dest)
				{
					@Override
					public void onCompleted()
					{
						d.s("Download Finished for " + C.YELLOW + args[1]);
						
						if(hasFailed())
						{
							d.f("Download has failed.");
						}
						
						else
						{
							d.w("Installing " + args[1]);
							
							try
							{
								PluginUtil.load(dest.getName().replaceAll(".jar", ""));
								d.s("Install Complete for " + args[1]);
							}
							
							catch(Exception e)
							{
								d.f("Failed");
								ExceptionUtil.print(e);
							}
						}
					}
				};
				
				fd.start();
				d.s("Downloading " + C.YELLOW + url.toString());
				
				new Task(0)
				{
					@Override
					public void run()
					{
						if(fd.isComplete())
						{
							cancel();
							return;
						}
						
						if(fd.isDownloading())
						{
							d.s("Downloading: " + C.LIGHT_PURPLE + F.fileSize((long) fd.getBytesPerSecond()) + "/s " + C.AQUA + F.pc(fd.getPercent()) + C.YELLOW + " " + new GTime((long) fd.getRemainingTime()).to(" left"));
						}
					}
				};
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
