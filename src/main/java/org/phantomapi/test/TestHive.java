package org.phantomapi.test;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import phantom.command.PhantomCommand;
import phantom.data.cluster.DataCluster;
import phantom.entity.Players;
import phantom.hive.Hive;
import phantom.hive.IHive;
import phantom.lang.Callback;
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
@Name("TEST hive")
@UnitTest({"hive"})
public class TestHive extends PhantomCommand implements IUnitTest
{
	public TestHive()
	{
		super("hive");
	}

	@Override
	public String[] getTestNames()
	{
		return this.getClass().getDeclaredAnnotation(UnitTest.class).value();
	}

	@Override
	public void test(Callback<TestResult> callbackSet, String[] args)
	{
		Block b = Players.getAnyPlayer().getTargetBlock((Set<Material>) null, 8);

		IHive hive = Hive.getHive(b);
		DataCluster cc = hive.pull("phantom-test");
		cc.set("test-string", "tested & " + UUID.randomUUID().toString());
		cc.set("testing-numbers", Math.random() * 12);
		cc.set("testing-rng-" + UUID.randomUUID().toString(), false);
		hive.push(cc, "phantom-test");
		cc = hive.pull("phantom-test");

		for(String i : cc.k())
		{
			callbackSet.run(new TestResult(i + ": " + cc.get(i).toString(), false));
		}

		TestResult tr = new TestResult("Done Writing to hive block ", false);
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
