package org.phantomapi.test;

import org.phantomapi.Phantom;
import org.phantomapi.service.ThreadPoolService;

import phantom.lang.Callback;
import phantom.lang.FinalInteger;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.sched.A;
import phantom.test.IUnitTest;
import phantom.test.TestResult;
import phantom.test.UnitTest;
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
public class TestAsyncTask implements IUnitTest
{
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
				callbackSet.run(new TestResult("Invalid integer argument: " + args[0]));
				return;
			}
		}

		for(int i = 0; i < 100; i++)
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

		Phantom.getService(ThreadPoolService.class).lock();
		callbackSet.run(new TestResult("Executed " + fi.get() + " of " + goal + " tasks", fi.get() != goal));
	}

	@Override
	public void test(Callback<TestResult> callbackSet)
	{
		test(callbackSet, new String[0]);
	}

}
