package phantom.test;

import phantom.lang.Callback;
import phantom.pawn.IPawn;

public interface IUnitTest extends IPawn
{
	public String[] getTestNames();

	public void test(Callback<TestResult> callbackSet, String[] args);

	public void test(Callback<TestResult> callbackSet);
}
