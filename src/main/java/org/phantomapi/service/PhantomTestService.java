package org.phantomapi.service;

import org.phantomapi.Phantom;

import phantom.dispatch.PD;
import phantom.lang.Callback;
import phantom.lang.GList;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantomapi.test.IUnitTest;
import phantomapi.test.TestResult;
import phantomapi.test.UnitTest;

/**
 * Test service used for running tests on the api
 *
 * @author cyberpwn
 */
@Name("SVC Phantom Test")
@Singular
public class PhantomTestService implements IService
{
	@Start
	public void start()
	{

	}

	@Stop
	public void stop()
	{

	}

	/**
	 * Run a test
	 *
	 * @param test
	 *            the test name or alias
	 * @param args
	 *            the arguments for the test
	 * @param result
	 *            the result callback (multiple will fire), if the TestResult object
	 *            fired has a isFinisher() as true, it is the last callback.
	 */
	public void runTest(String test, String[] args, Callback<TestResult> result)
	{
		for(Class<?> i : Phantom.getAnchors("phantom-test"))
		{
			if(new GList<String>(i.getDeclaredAnnotation(UnitTest.class).value()).contains(test.toLowerCase()))
			{
				try
				{
					IUnitTest u = (IUnitTest) i.getConstructor().newInstance();

					if(Phantom.isActive(u))
					{
						result.run(new TestResult("Phantom Test SVC: Test already running", true));
						return;
					}

					Phantom.activate(u);

					u.test(new Callback<TestResult>()
					{
						@Override
						public void run()
						{
							result.run(get());

							if(get().isFinisher())
							{
								Phantom.deactivate(u);
							}
						}
					});
				}

				catch(Throwable e)
				{
					PD.f("Failed to run test " + test);
					Phantom.kick(e);
					result.run(new TestResult("Phantom Test SVC: Failed to run test. Check console", true));
				}

				return;
			}
		}
	}

	/**
	 * Run a test with no arguments
	 *
	 * @param test
	 *            the test name or alias
	 * @param result
	 *            the result callback (multiple will fire), if the TestResult object
	 *            fired has a isFinisher() as true, it is the last callback.
	 */
	public void runTest(String test, Callback<TestResult> result)
	{
		runTest(test, new String[0], result);
	}
}
