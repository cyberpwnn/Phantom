package org.phantomapi.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.YAMLDataInput;
import org.phantomapi.clust.YAMLDataOutput;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.filesystem.Serializer;
import org.phantomapi.gui.Click;
import org.phantomapi.gui.Dialog;
import org.phantomapi.gui.PhantomDialog;
import org.phantomapi.gui.PhantomElement;
import org.phantomapi.gui.PhantomWindow;
import org.phantomapi.gui.Slot;
import org.phantomapi.gui.Window;
import org.phantomapi.kernel.Platform;
import org.phantomapi.lang.Audible;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.MFADistortion;
import org.phantomapi.lang.Title;
import org.phantomapi.nms.NMSX;
import org.phantomapi.slate.PhantomSlate;
import org.phantomapi.slate.Slate;
import org.phantomapi.sync.ExecutiveRunnable;
import org.phantomapi.sync.ExecutiveTask;
import org.phantomapi.sync.S;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.text.ParameterAdapter;
import org.phantomapi.text.Tabulator;
import org.phantomapi.text.TagProvider;
import org.phantomapi.world.L;

public class APITest implements TagProvider
{
	private PhantomSender mb;
	private GList<AIT> tests;
	
	public APITest(PhantomSender mb)
	{
		this.mb = mb;
		mb.setMessageBuilder(new MessageBuilder(this));
		tests = new GList<AIT>();
		build();
		run();
	}
	
	public void build()
	{
		l("Building Tests... Please wait.");
		
		add(new AIT("controller:pull")
		{
			@Override
			public void run()
			{
				if(Phantom.instance().getAllControllers().isEmpty())
				{
					fail();
				}
				
				if(Phantom.instance().getBindings().isEmpty())
				{
					fail();
				}
			}
		});
		
		add(new AIT("task:async")
		{
			@Override
			public void run()
			{
				new A()
				{
					@Override
					public void async()
					{
						
					}
				};
			}
		});
		
		add(new AIT("task:sync")
		{
			@Override
			public void run()
			{
				new TaskLater()
				{
					@Override
					public void run()
					{
						
					}
				};
			}
		});
		
		add(new AIT("task:sync:handover")
		{
			@Override
			public void run()
			{
				new S()
				{
					@Override
					public void sync()
					{
						
					}
				};
			}
		});
		
		add(new AIT("task")
		{
			@Override
			public void run()
			{
				if(!Phantom.check())
				{
					fail();
				}
			}
		});
		
		add(new AIT("plat:profile:cpu")
		{
			@Override
			public void run()
			{
				if(Platform.CPU.getCPULoad() < 0.00001)
				{
					fail();
				}
				
				if(Platform.CPU.getProcessCPULoad() < 0.00001)
				{
					fail();
				}
			}
		});
		
		add(new AIT("plat:profile:memory:physical")
		{
			@Override
			public void run()
			{
				if(Platform.MEMORY.PHYSICAL.getUsedMemory() < 1)
				{
					fail();
				}
			}
		});
		
		add(new AIT("plat:profile:memory:virtual")
		{
			@Override
			public void run()
			{
				if(Platform.MEMORY.VIRTUAL.getUsedMemory() < 1)
				{
					fail();
				}
			}
		});
		
		add(new AIT("plat:profile:disk:roots")
		{
			@Override
			public void run()
			{
				if(Platform.STORAGE.getRoots().length < 1)
				{
					fail();
				}
			}
		});
		
		add(new AIT("plat:profile:disk:capacity")
		{
			@Override
			public void run()
			{
				if(Platform.STORAGE.getAbsoluteTotalSpace() < 1)
				{
					fail();
				}
			}
		});
		
		add(new AIT("datacluster:write")
		{
			@Override
			public void run()
			{
				DataCluster dc = new DataCluster();
				dc.set("test.string", "Stringd");
				dc.set("test.double", -0.65743345);
				dc.set("test.int", 234);
				dc.set("test.list", new GList<String>().qadd("test").qadd("list").qadd("test"));
				dc.set("test.boolean", true);
				try
				{
					new YAMLDataOutput().save(dc, new File(Phantom.instance().getDataFolder(), "cluster-test.yml"));
				}
				
				catch(IOException e)
				{
					fail();
				}
			}
		});
		
		add(new AIT("datacluster:overwrite")
		{
			@Override
			public void run()
			{
				DataCluster dc = new DataCluster();
				dc.set("test.overwrite.data", "New Data");
				dc.set("test.string", "New String Text (Overwrite)");
				
				try
				{
					new YAMLDataOutput().save(dc, new File(Phantom.instance().getDataFolder(), "cluster-test.yml"));
				}
				
				catch(IOException e)
				{
					fail();
				}
			}
		});
		
		add(new AIT("text:tabulate")
		{
			@Override
			public void run()
			{
				Tabulator<String> tab = new Tabulator<String>(10);
				
				for(int i = 0; i < 35; i++)
				{
					tab.add(UUID.randomUUID().toString());
				}
				
				for(int i = 0; i < tab.getTabCount(); i++)
				{
					Phantom.instance().getDispatcher().s("--------------------------------");
					
					for(String j : tab.getTab(i))
					{
						Phantom.instance().getDispatcher().s("ITR: " + j);
					}
				}
			}
		});
		
		add(new AIT("out:d")
		{
			@Override
			public void run()
			{
				D d = new D("Test D");
				
				d.i("Info!");
				d.s("Success!");
				d.w("Warning!");
				d.f("Failure!");
				d.v("Verbose!");
				d.o("Overbose!");
				
				new A()
				{
					@Override
					public void async()
					{
						d.i("Info!");
						d.s("Success!");
						d.w("Warning!");
						d.f("Failure!");
						d.v("Verbose!");
						d.o("Overbose!");
					}
				};
			}
		});
		
		add(new AIT("text:param")
		{
			@Override
			public void run()
			{
				Phantom.instance().getDispatcher().s("Test: " + "This is %a% cool %param% %_$Dtest f%");
				Phantom.instance().getDispatcher().s("Whas: " + "%a% = 1, %param% = 2, %_$Dtest f% = 3");
				
				for(String i : F.getParameters("This is %a% cool %param% %_$Dtest f%", '%'))
				{
					Phantom.instance().getDispatcher().v("Found: " + i);
				}
				
				Phantom.instance().getDispatcher().s(new ParameterAdapter()
				{
					@Override
					public String onParameterRequested(String parameter)
					{
						if(parameter.equalsIgnoreCase("a"))
						{
							return "1";
						}
						
						if(parameter.equalsIgnoreCase("param"))
						{
							return "2";
						}
						
						if(parameter.equalsIgnoreCase("_$Dtest f"))
						{
							return "3";
						}
						
						return "%parameter%";
					}
				}.adapt("This is %a% cool %param% %_$Dtest f%"));
			}
		});
		
		add(new AIT("data:outuser")
		{
			@Override
			public void run()
			{
				DataCluster cc = new DataCluster();
				cc.set("string", "string");
				cc.set("int", 43);
				
				File f = new File(Phantom.instance().getDataFolder(), "ser.ser");
				
				if(f.exists())
				{
					f.delete();
				}
				
				try
				{
					f.createNewFile();
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
				try
				{
					D d = Phantom.instance().getDispatcher();
					byte[] data = Serializer.serialize(cc);
					d.s("Bytes: " + data);
					Serializer.serializeToFile(cc, f);
					
					d.s("Reading file");
					DataCluster dc = (DataCluster) Serializer.deserializeFromFile(f);
					d.s("data: " + dc.toJSON().toString());
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
				catch(ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		});
		
		add(new AIT("slate:dotest")
		{
			@Override
			public void run()
			{
				Slate s = new PhantomSlate(C.LIGHT_PURPLE + "Phantom");
				
				for(Player i : Phantom.instance().onlinePlayers())
				{
					s.addViewer(i);
				}
				
				s.update();
				
				new TaskLater(5)
				{
					@Override
					public void run()
					{
						s.addLine(C.RED + "RED");
						s.update();
					}
				};
				
				new TaskLater(10)
				{
					@Override
					public void run()
					{
						s.addLine(C.GREEN + "GREEN");
						s.update();
					}
				};
				
				new TaskLater(15)
				{
					@Override
					public void run()
					{
						s.addLine(C.AQUA + "BLUE");
						s.update();
					}
				};
				
				new TaskLater(30)
				{
					@Override
					public void run()
					{
						for(Player i : Phantom.instance().onlinePlayers())
						{
							s.removeViewer(i);
						}
					}
				};
			}
		});
		
		add(new AIT("mfa:boom")
		{
			@Override
			public void run()
			{
				Audible a = new GSound(Sound.ENTITY_GENERIC_EXPLODE);
				a.setPitch(0.3f);
				a = new MFADistortion(4, 1.8f).distort(a);
				
				for(Player i : Phantom.instance().onlinePlayers())
				{
					a.play(i);
				}
			}
		});
		
		add(new AIT("nms:firework")
		{
			@Override
			public void run()
			{
				for(Player i : P.onlinePlayers())
				{
					NMSX.launchFirework(i.getEyeLocation(), Color.WHITE, Color.WHITE, Type.BALL);
				}
			}
		});
		
		add(new AIT("nms:title")
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					Title t = new Title(C.RED + "TITLE", C.GREEN + "SUBTITLE", C.BLUE + "ACTION", 5, 30, 5);
					t.send(i);
				}
			}
		});
		
		add(new AIT("math:jsformula")
		{
			@Override
			public void run()
			{
				Formula f = new Formula("Math.sin($0) / Math.cos($1)");
				
				for(int i = 0; i < 4; i++)
				{
					double d = Math.random();
					double k = Math.random();
					
					try
					{
						Phantom.instance().getDispatcher().s("Math.sin(" + d + ") / Math.cos(" + k + ") = " + f.evaluate(d, k));
					}
					
					catch(Exception e)
					{
						ExceptionUtil.print(e);
					}
				}
			}
		});
		
		add(new AIT("gui:maintest")
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					Window test = new PhantomWindow("Test", i);
					Window main = new PhantomWindow(C.AQUA + "Main", i);
					Window dialog = new PhantomDialog(ChatColor.DARK_RED + "Are you SURE?", i, true)
					{
						@Override
						public void onCancelled(Player p, Window w, Dialog d)
						{
							test.open();
						}
					};
					
					dialog.addElement(new PhantomElement(Material.SLIME_BALL, new Slot(0, 2), C.GREEN + "YES")
					{
						@Override
						public void onClick(Player p, Click c, Window w)
						{
							main.open();
						}
					});
					
					dialog.addElement(new PhantomElement(Material.BARRIER, new Slot(0, 3), C.RED + "NO")
					{
						@Override
						public void onClick(Player p, Click c, Window w)
						{
							test.open();
						}
					});
					
					main.addElement(new PhantomElement(Material.SLIME_BALL, new Slot(0, 2), C.RED + "Colored")
					{
						@Override
						public void onClick(Player p, Click c, Window w)
						{
							test.open();
						}
					});
					
					test.addElement(new PhantomElement(Material.BARRIER, new Slot(-1, 3), C.RED + "Close")
					{
						@Override
						public void onClick(Player p, Click c, Window w)
						{
							test.close();
						}
					});
					
					test.addElement(new PhantomElement(Material.CARROT_STICK, new Slot(0, 3), C.RED + "Do something")
					{
						@Override
						public void onClick(Player p, Click c, Window w)
						{
							dialog.open();
						}
					});
					
					test.addElement(new PhantomElement(Material.MAGMA_CREAM, new Slot(1, 3), C.RED + "Back")
					{
						@Override
						public void onClick(Player p, Click c, Window w)
						{
							main.open();
						}
					});
					
					main.setBackground(new PhantomElement(Material.STAINED_GLASS_PANE, (byte) 1, new Slot(0, 1), " ")
					{
						@Override
						public void onClick(Player p, Click c, Window w)
						{
							
						}
					});
					test.setBackground(new PhantomElement(Material.STAINED_GLASS_PANE, (byte) 2, new Slot(0, 1), " ")
					{
						@Override
						public void onClick(Player p, Click c, Window w)
						{
							
						}
					});
					dialog.setBackground(new PhantomElement(Material.STAINED_GLASS_PANE, (byte) 3, new Slot(0, 1), " ")
					{
						@Override
						public void onClick(Player p, Click c, Window w)
						{
							
						}
					});
					
					main.open();
					
					new TaskLater(5)
					{
						@Override
						public void run()
						{
							i.closeInventory();
						}
					};
				}
			}
		});
		
		add(new AIT("datacluster:read")
		{
			@Override
			public void run()
			{
				DataCluster dc = new DataCluster();
				
				try
				{
					new YAMLDataInput().load(dc, new File(Phantom.instance().getDataFolder(), "cluster-test.yml"));
					
					for(String i : dc.getData().keySet())
					{
						System.out.println(i + ": " + dc.getAbstract(i));
					}
				}
				
				catch(IOException e)
				{
					fail();
				}
			}
		});
		
		add(new AIT("world:photon:relightasync")
		{
			@Override
			public void run()
			{
				for(Player i : P.onlinePlayers())
				{
					L.relight(i.getLocation().getChunk());
				}
			}
		});
	}
	
	public void fail()
	{
		throw new NullPointerException();
	}
	
	public void add(AIT ait)
	{
		tests.add(ait);
	}
	
	public void run()
	{
		l("Starting test...");
		l("Testing " + tests.size() + " tests");
		
		int max = tests.size();
		FinalInteger c = new FinalInteger(0);
		FinalInteger p = new FinalInteger(0);
		
		new ExecutiveTask<AIT>(tests.iterator(new ExecutiveRunnable<AIT>()
		{
			public void run()
			{
				try
				{
					c.add(1);
					next().run();
					p.add(1);
					l(C.WHITE + "(" + c.get() + "/" + max + ") " + C.GRAY + next().getName() + ": " + C.GREEN + "PASS");
				}
				
				catch(Exception e)
				{
					l(C.WHITE + "(" + c.get() + "/" + max + ") " + C.GRAY + next().getName() + ": " + C.RED + "FAIL");
				}
			}
		}), 0.01, 10, new Runnable()
		{
			@Override
			public void run()
			{
				l("--- Test Results ---");
				i("Pass: " + p.get() + "/" + max + C.GREEN + " (" + F.pc(p.get() / max) + ")");
				i("Fail: " + (max - p.get()) + "/" + max + C.RED + " (" + F.pc((max - p.get()) / max) + ")");
				w("TEST COMPLETE.");
			}
		});
	}
	
	public void l(String s)
	{
		mb.sendMessage(C.GRAY + s);
	}
	
	public void f(String s)
	{
		mb.sendMessage(C.RED + s);
	}
	
	public void w(String s)
	{
		mb.sendMessage(C.GOLD + s);
	}
	
	public void i(String s)
	{
		mb.sendMessage(C.WHITE + s);
	}
	
	public void s(String s)
	{
		mb.sendMessage(C.GREEN + s);
	}
	
	@Override
	public String getChatTag()
	{
		return C.LIGHT_PURPLE + "[" + C.DARK_GRAY + "Phantom " + C.LIGHT_PURPLE + "| " + C.RED + "API Test" + C.LIGHT_PURPLE + "]: " + C.GRAY;
	}
	
	@Override
	public String getChatTagHover()
	{
		return C.LIGHT_PURPLE + "Phantom APITEST";
	}
}
