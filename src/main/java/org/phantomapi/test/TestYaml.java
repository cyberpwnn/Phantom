package org.phantomapi.test;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.phantomapi.Phantom;

import phantom.command.PhantomCommand;
import phantom.data.cluster.Cluster;
import phantom.data.cluster.DataCluster;
import phantom.data.ports.YamlDataPort;
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
@Name("TEST Yaml")
@UnitTest({"yaml"})
public class TestYaml extends PhantomCommand implements IUnitTest
{
	public TestYaml()
	{
		super("yaml");
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

		for(Class<?> i : Phantom.getAnchors("phantom-cluster"))
		{
			Class<?> type = i.getDeclaredAnnotation(Cluster.class).type();
			boolean supp = cc.supports(type);
			callbackSet.run(new TestResult("Support for " + type.getSimpleName() + ": " + supp, !supp));
		}

		callbackSet.run(new TestResult("Testing WRITE", false));

		cc.set("testing.string", "This is a string");
		cc.set("testing.integer", 44);
		cc.set("testing.double", 3.6734554444);
		cc.set("testing.float", 3.4f);
		cc.set("testing.long", M.ms());
		cc.set("testing.char", 'Z');
		cc.set("testing.short", (short) 4);
		cc.set("testing.byte", (byte) -15);

		callbackSet.run(new TestResult("Wrote all basic types", false));

		for(String i : cc.k())
		{
			callbackSet.run(new TestResult("K: " + i + " V: " + cc.get(i).toString() + " T: " + cc.getType(i).getSimpleName(), false));
		}

		TestResult tr = new TestResult("Done Writing cluster. Writing YAML", false);
		FileConfiguration fc = cc.write(new YamlDataPort());
		String yaml = fc.saveToString();

		for(String i : yaml.split("\n"))
		{
			TestResult t = new TestResult(i, false);
			callbackSet.run(t);
		}

		try
		{
			fc.save(new File(Phantom.getDataFolder(), "test-yaml.yml"));
			callbackSet.run(new TestResult("Saved yaml", false));
			DataCluster cn = new DataCluster();
			cn.read(new YamlDataPort(), fc);
			callbackSet.run(new TestResult("Read yaml to cc", false));

			for(String i : cc.k())
			{
				callbackSet.run(new TestResult("K: " + i + " V: " + cc.get(i).toString() + " T: " + cc.getType(i).getSimpleName(), false));
			}
		}

		catch(IOException e)
		{
			Phantom.kick(e);
		}

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
