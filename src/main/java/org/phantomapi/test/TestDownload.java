package org.phantomapi.test;

import java.io.File;
import java.net.URL;

import org.bukkit.command.CommandSender;
import org.phantomapi.Phantom;

import phantom.command.PhantomCommand;
import phantom.lang.Callback;
import phantom.lang.format.F;
import phantom.net.DownloadQueue;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.sched.Task;
import phantom.test.IUnitTest;
import phantom.test.TestResult;
import phantom.test.UnitTest;
import phantom.text.C;
import phantom.text.ITagProvider;
import phantom.text.TXT;
import phantom.util.metrics.Documented;

/**
 * Tests downloads
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Name("TEST Download")
@UnitTest({"download", "dl"})
public class TestDownload extends PhantomCommand implements IUnitTest
{
	public TestDownload()
	{
		super("download");
	}

	@Override
	public String[] getTestNames()
	{
		return this.getClass().getDeclaredAnnotation(UnitTest.class).value();
	}

	@Override
	public void test(Callback<TestResult> callbackSet, String[] args)
	{
		try
		{
			URL url = new URL("http://cdn.volmit.com/test/512MB.zip");
			File f = new File("test-download.zip");
			DownloadQueue q = new DownloadQueue();
			q.add(url, f);
			q.add(url, f);
			q.add(url, f);
			q.add(url, f);
			q.start();

			new Task(0)
			{
				@Override
				public void run()
				{
					if(q.isRunning())
					{
						callbackSet.run(new TestResult("Downloading " + q.getDownloads().size() + " files @ " + F.fileSize((long) q.getBps()) + "/s " + F.fileSize(q.getDownloaded()) + " of " + F.fileSize(q.getSize()) + " (" + F.pc(q.getProgress()) + ")"));
					}

					else
					{
						cancel();
					}
				}
			};
		}

		catch(Exception e)
		{
			TestResult tr = new TestResult("Failed to download. Check console.", true);
			tr.setFinisher(true);
			callbackSet.run(tr);
			Phantom.kick(e);
		}
	}

	@Override
	public void test(Callback<TestResult> callbackSet)
	{
		test(callbackSet, new String[0]);
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] a)
	{
		ITagProvider tag = this;

		test(new Callback<TestResult>()
		{
			@Override
			public void run()
			{
				get().sendResults(sender, tag);
			}
		}, a);

		return true;
	}

	@Override
	public String getTag(CommandSender sender)
	{
		return TXT.makeTag(C.LIGHT_PURPLE, C.DARK_GRAY, C.LIGHT_PURPLE, C.GRAY, "Phantom" + C.WHITE + " Test");
	}

}
