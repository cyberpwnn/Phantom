package org.phantomapi.test;

import org.bukkit.command.CommandSender;
import org.phantomapi.Phantom;
import org.phantomapi.service.ThreadPoolSVC;

import phantom.command.PhantomCommand;
import phantom.lang.Callback;
import phantom.lang.FinalInteger;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.sched.A;
import phantom.sched.S;
import phantom.test.IUnitTest;
import phantom.test.TestResult;
import phantom.test.UnitTest;
import phantom.text.C;
import phantom.text.ITagProvider;
import phantom.text.TXT;
import phantom.util.metrics.Documented;

/**
 * Tests async tasks. Argument allowed [int] for the count of tasks to fire off
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Name("TEST Async Task")
@UnitTest({"async", "async-task"})
public class TestAsyncTask extends PhantomCommand implements IUnitTest
{
	public TestAsyncTask()
	{
		super("async");
		addAlias("async-task");
		addAlias("asy");
		setPrameterUsage("[i:task size]");
	}

	@Override
	public String[] getTestNames()
	{
		return this.getClass().getDeclaredAnnotation(UnitTest.class).value();
	}

	@Override
	public void test(Callback<TestResult> callbackSet, String[] args)
	{
		FinalInteger fi = new FinalInteger(0);
		int goal = 100;

		if(args.length > 0)
		{
			try
			{
				goal = Integer.valueOf(args[0]);
			}

			catch(NumberFormatException e)
			{
				callbackSet.run(new TestResult("Invalid integer argument: " + args[0], true));
				return;
			}
		}

		callbackSet.run(new TestResult("Queued " + goal + " tasks to be run."));

		for(int i = 0; i < goal; i++)
		{
			new A()
			{
				@Override
				public void run()
				{
					fi.add(1);
				}
			};
		}

		Phantom.getService(ThreadPoolSVC.class).lock();

		int gg = goal;

		new S(20)
		{
			@Override
			public void run()
			{
				TestResult tr = new TestResult("Executed " + fi.get() + " of " + gg + " tasks in 20 ticks", fi.get() != gg);
				tr.setFinisher(true);
				callbackSet.run(tr);
			}
		};
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
