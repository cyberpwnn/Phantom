package org.phantomapi.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;
import org.phantomapi.Phantom;

import phantom.command.PhantomCommand;
import phantom.data.cluster.Cluster;
import phantom.data.cluster.DataCluster;
import phantom.data.ports.CompressedDataPort;
import phantom.lang.Callback;
import phantom.lang.GList;
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
@Name("TEST Compressed DATA PORT")
@UnitTest({"compressed"})
public class TestCompressedDataPort extends PhantomCommand implements IUnitTest
{
	public TestCompressedDataPort()
	{
		super("compressed");
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
		cc.set("testing.vector", Vector.getRandom().subtract(Vector.getRandom()).clone().normalize());
		cc.set("testing.world", Phantom.getWorld("world"));
		cc.set("testing.location", Phantom.getWorld("world").getSpawnLocation().clone().add(Vector.getRandom().normalize()));
		cc.set("testing.uuid", UUID.randomUUID());
		cc.set("testing.stringlist", new GList<String>().qadd("alpha").qadd("bravo").qadd("charlie"));

		callbackSet.run(new TestResult("Wrote all basic types", false));

		for(String i : cc.k())
		{
			callbackSet.run(new TestResult("K: " + i + " V: " + cc.get(i).toString() + " T: " + cc.getType(i).getSimpleName(), false));
		}

		TestResult tr = new TestResult("Done Writing cluster. Writing COMPRESSED", false);
		ByteBuffer fc;

		try
		{
			fc = cc.write(new CompressedDataPort());
			File fx = new File(Phantom.getDataFolder(), "test-compressed.cdc");
			FileOutputStream fos = new FileOutputStream(fx);
			fos.write(fc.array());
			fos.flush();
			fos.close();
		}

		catch(IOException e1)
		{
			Phantom.kick(e1);
			TestResult t = new TestResult("Failed to write", true);
			t.setFinisher(true);
			callbackSet.run(t);
			return;
		}

		callbackSet.run(new TestResult("Wrote " + fc.array().length + " bytes of data.", false));
		callbackSet.run(new TestResult("======== Testing READ COMPRESSED ========", false));

		try
		{
			byte[] data = fc.array();
			DataCluster dc = new DataCluster();
			dc.read(new CompressedDataPort(), ByteBuffer.wrap(data));

			for(String i : dc.k())
			{
				callbackSet.run(new TestResult("K: " + i + " V: " + cc.get(i).toString() + " T: " + cc.getType(i).getSimpleName(), false));
			}
		}

		catch(IOException e1)
		{
			Phantom.kick(e1);
			TestResult t = new TestResult("Failed to read", true);
			t.setFinisher(true);
			callbackSet.run(t);
			return;
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
