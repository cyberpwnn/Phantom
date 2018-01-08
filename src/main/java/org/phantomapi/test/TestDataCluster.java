package org.phantomapi.test;

import org.bukkit.command.CommandSender;
import org.phantomapi.Phantom;

import phantom.command.PhantomCommand;
import phantom.data.cluster.Cluster;
import phantom.data.cluster.DataCluster;
import phantom.lang.Callback;
import phantom.math.M;
import phantom.pawn.Name;
import phantom.pawn.Singular;
import phantom.test.IUnitTest;
import phantom.test.TestResult;
import phantom.test.UnitTest;
import phantom.text.C;
import phantom.text.ITagProvider;
import phantom.text.TXT;
import phantom.util.metrics.Documented;

/**
 * Tests data clusters
 *
 * @author cyberpwn
 */
@Documented
@Singular
@Name("TEST Data Cluster")
@UnitTest({"datacluster", "clust"})
public class TestDataCluster extends PhantomCommand implements IUnitTest
{
	public TestDataCluster()
	{
		super("clust");
		addAlias("datacluster");
	}

	@Override
	public String[] getTestNames()
	{
		return this.getClass().getDeclaredAnnotation(UnitTest.class).value();
	}

	@Override
	public void test(Callback<TestResult> callbackSet, String[] args)
	{
		DataCluster cc = new DataCluster();
		boolean fail = false;

		for(Class<?> i : Phantom.getAnchors("phantom-cluster"))
		{
			Class<?> type = i.getDeclaredAnnotation(Cluster.class).value();
			boolean supp = cc.supports(type);
			callbackSet.run(new TestResult("Support for " + type.getSimpleName() + ": " + supp, !supp));

			if(!supp)
			{
				fail = true;
			}
		}

		callbackSet.run(new TestResult("Testing WRITE", false));

		cc.set("string", "This is a string");
		cc.set("integer", 44);
		cc.set("double", 3.6734554444);
		cc.set("float", 3.4f);
		cc.set("long", M.ms());
		cc.set("char", 'Z');
		cc.set("short", (short) 4);
		cc.set("byte", (byte) -15);

		callbackSet.run(new TestResult("Wrote all basic types", false));

		for(String i : cc.k())
		{
			callbackSet.run(new TestResult("K: " + i + " V: " + cc.get(i).toString() + " T: " + cc.getType(i).getSimpleName(), false));
		}

		TestResult tr = new TestResult("Done", fail);
		tr.setFinisher(true);
		callbackSet.run(tr);
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
