package org.phantomapi.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import org.bukkit.util.Vector;
import org.phantomapi.Phantom;
import org.phantomapi.async.A;
import org.phantomapi.blockmeta.BlockMeta;
import org.phantomapi.blockmeta.PMeta;
import org.phantomapi.chromatic.Chromatic;
import org.phantomapi.chromatic.ChromaticBlock;
import org.phantomapi.clust.DataCluster;
import org.phantomapi.clust.YAMLDataInput;
import org.phantomapi.clust.YAMLDataOutput;
import org.phantomapi.command.Command;
import org.phantomapi.command.CommandAlias;
import org.phantomapi.command.CommandFilter;
import org.phantomapi.command.CommandFilter.Permission;
import org.phantomapi.command.PhantomCommand;
import org.phantomapi.command.PhantomSender;
import org.phantomapi.construct.Controllable;
import org.phantomapi.construct.Controller;
import org.phantomapi.filesystem.Serializer;
import org.phantomapi.gui.Click;
import org.phantomapi.gui.Dialog;
import org.phantomapi.gui.Notification;
import org.phantomapi.gui.PhantomDialog;
import org.phantomapi.gui.PhantomElement;
import org.phantomapi.gui.PhantomWindow;
import org.phantomapi.gui.Slot;
import org.phantomapi.gui.Window;
import org.phantomapi.hud.ConfigurationUI;
import org.phantomapi.lang.GChunk;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GLocation;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSound;
import org.phantomapi.lang.Priority;
import org.phantomapi.lang.Title;
import org.phantomapi.nest.Nest;
import org.phantomapi.network.FileDownload;
import org.phantomapi.nms.NMSX;
import org.phantomapi.papyrus.Maps;
import org.phantomapi.papyrus.PaperColor;
import org.phantomapi.papyrus.PapyrusRenderer;
import org.phantomapi.papyrus.RenderFilter;
import org.phantomapi.schematic.Artifact;
import org.phantomapi.schematic.EdgeDistortion;
import org.phantomapi.schematic.Schematic;
import org.phantomapi.schematic.WorldArtifact;
import org.phantomapi.schematic.WorldStructure;
import org.phantomapi.sfx.Audible;
import org.phantomapi.sfx.MFADistortion;
import org.phantomapi.slate.PhantomSlate;
import org.phantomapi.slate.Slate;
import org.phantomapi.source.SourcePack;
import org.phantomapi.stack.Stack;
import org.phantomapi.stack.StackedPlayerInventory;
import org.phantomapi.sync.ExecutiveIterator;
import org.phantomapi.sync.S;
import org.phantomapi.sync.Task;
import org.phantomapi.sync.TaskLater;
import org.phantomapi.text.GBook;
import org.phantomapi.text.MessageBuilder;
import org.phantomapi.text.ParameterAdapter;
import org.phantomapi.text.Tabulator;
import org.phantomapi.transmit.Transmission;
import org.phantomapi.util.C;
import org.phantomapi.util.Chunks;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.F;
import org.phantomapi.util.Formula;
import org.phantomapi.util.Items;
import org.phantomapi.util.M;
import org.phantomapi.util.P;
import org.phantomapi.util.Players;
import org.phantomapi.util.T;
import org.phantomapi.util.Timer;
import org.phantomapi.vfx.ParticleEffect;
import org.phantomapi.vfx.PhantomEffect;
import org.phantomapi.vfx.SphereParticleManipulator;
import org.phantomapi.vfx.VisualEffect;
import org.phantomapi.world.Dimension;
import org.phantomapi.world.Direction;
import org.phantomapi.world.L;
import org.phantomapi.world.MaterialBlock;
import org.phantomapi.world.PE;
import org.phantomapi.world.W;
import org.phantomapi.world.WorldLock;
import org.phantomapi.wraith.PhantomWraith;
import org.phantomapi.wraith.Wraith;
import org.phantomapi.wraith.WraithEquipment;
import org.phantomapi.wraith.WraithTarget;
import com.boydti.fawe.object.RunnableVal;
import com.boydti.fawe.util.TaskManager;

/**
 * Runs tests on various functions of phantom
 * 
 * @author cyberpwn
 */
public class TestController extends Controller
{
	private GMap<String, Runnable> tests;
	private GList<Player> phups;
	
	public TestController(Controllable parentController)
	{
		super(parentController);
		
		phups = new GList<Player>();
		tests = new GMap<String, Runnable>();
		
		tests.put("channel-pool", new Runnable()
		{
			@Override
			public void run()
			{
				GList<String> k = new GList<String>();
				GList<String> v = new GList<String>();
				
				for(int i = 0; i < 10240008 * Math.random(); i++)
				{
					k.add(UUID.randomUUID().toString());
				}
				
				v.qadd("alpha").qadd("beta").qadd("charlie").qadd("delta");
				
				Phantom.schedule(new ExecutiveIterator<String>(k.copy())
				{
					@Override
					public void onIterate(String next)
					{
						while(Math.random() < 0.6)
						{
							UUID.randomUUID();
							Math.sqrt(Math.sqrt(Math.random()));
						}
						
						if(Math.random() < 0.001)
						{
							Phantom.schedule(v.pickRandom(), new ExecutiveIterator<String>(v)
							{
								@Override
								public void onIterate(String next)
								{
									
								}
							});
						}
					}
				});
			}
		});
		
		tests.put("inventory-thrash", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					StackedPlayerInventory inv = new StackedPlayerInventory(i.getInventory());
					inv.setStacks(inv.getStacks());
					inv.thrash();
				}
			}
		});
		
		tests.put("rex", new Runnable()
		{
			@Override
			public void run()
			{
				ExceptionUtil.writeIssues(new File(Phantom.instance().getDataFolder(), "report"));
			}
		});
		
		tests.put("block-meta", new Runnable()
		{
			@Override
			public void run()
			{
				Block block = P.targetBlock(Players.getAnyPlayer(), 128).getBlock();
				BlockMeta bm = PMeta.getBlock(block);
				bm.getConfiguration().set("test", "value");
				bm.getConfiguration().set("test2", "value2");
				bm.getConfiguration().set("test3", "value3");
				bm.write();
				
				s("Data: " + PMeta.getChunk(block.getChunk()).getConfiguration().toJSON().toString());
				
				try
				{
					s("RAW: " + new String(PMeta.getChunk(block.getChunk()).getConfiguration().compress()));
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		
		tests.put("crash", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : onlinePlayers())
				{
					NMSX.crash(i);
				}
			}
		});
		
		tests.put("tarmor", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : onlinePlayers())
				{
					i.sendMessage("0 = " + i.getInventory().getArmorContents()[0].getType().toString());
				}
			}
		});
		
		tests.put("chunkupdate", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					Chunks.update(i.getLocation().getChunk());
				}
			}
		});
		
		tests.put("papyrus", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					ItemStack is = new ItemStack(Material.MAP);
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(C.LIGHT_PURPLE + "Papyrus");
					is.setItemMeta(im);
					i.setItemInHand(is);
					
					new TaskLater()
					{
						@Override
						public void run()
						{
							MapView view = Maps.getView(i.getItemInHand());
							
							new PapyrusRenderer(view)
							{
								@Override
								public void render()
								{
									Block block = P.targetBlock(i, 64).getBlock();
									ChromaticBlock cb = Chromatic.getBlock(new MaterialBlock(block.getLocation()));
									clear(PaperColor.TRANSPARENT);
									
									if(cb != null)
									{
										clear(PaperColor.matchColor(Chromatic.getVisibleColor(i.getLocation().clone().add(0, 1.5, 0), i.getLocation().getDirection(), 64)));
									}
									
									else
									{
										filter(new RenderFilter()
										{
											@Override
											public byte onRender(int x, int y, byte currentColor)
											{
												if(M.r(0.3))
												{
													return PaperColor.RED;
												}
												
												return currentColor;
											}
										});
									}
								}
							};
						}
					};
				}
			}
		});
		
		tests.put("cluster-write", new Runnable()
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
					new YAMLDataOutput().save(dc, new File(getPlugin().getDataFolder(), "cluster-test.yml"));
				}
				
				catch(IOException e)
				{
					ExceptionUtil.print(e);
				}
			}
		});
		
		tests.put("nest-map", new Runnable()
		{
			@Override
			public void run()
			{
				Nest.giveMap(Players.getAnyPlayer());
			}
		});
		
		tests.put("inv-da", new Runnable()
		{
			@Override
			public void run()
			{
				StackedPlayerInventory si = new StackedPlayerInventory(Players.getAnyPlayer().getInventory());
				
				try
				{
					DataCluster cc = new DataCluster(si.toData());
					s("JSON: " + C.stripColor(cc.toJSON().toString()));
					w("RAW: " + new String(cc.compress()));
					
					si.clear();
					Players.getAnyPlayer().getInventory().clear();
					
					new TaskLater(20)
					{
						@Override
						public void run()
						{
							s("Re-creating");
							
							try
							{
								si.fromData(cc.compress());
								si.thrash();
							}
							
							catch(IOException e)
							{
								e.printStackTrace();
							}
						}
					};
				}
				
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		
		tests.put("download", new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					File download = new File(getPlugin().getDataFolder(), "test-download.zip");
					FileDownload.download(new URL("http://mirror.cc.columbia.edu/pub/software/eclipse/technology/epp/downloads/release/neon/1a/eclipse-jee-neon-1a-win32-x86_64.zip"), download);
				}
				
				catch(MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		});
		
		tests.put("cluster-overwrite", new Runnable()
		{
			@Override
			public void run()
			{
				DataCluster dc = new DataCluster();
				dc.set("test.overwrite.data", "New Data");
				dc.set("test.string", "New String Text (Overwrite)");
				
				try
				{
					new YAMLDataOutput().save(dc, new File(getPlugin().getDataFolder(), "cluster-test.yml"));
				}
				
				catch(IOException e)
				{
					ExceptionUtil.print(e);
				}
			}
		});
		
		tests.put("jframe", new Runnable()
		{
			@Override
			public void run()
			{
				DataCluster cc = new DataCluster();
				
				cc.set("entity.enabled", true, "Entity holds resources for use.");
				cc.set("entity.visible", true, "Entity visible to players and active.");
				cc.set("entity.name", "Dicksnout", "The entity name.");
				cc.set("entity.code-name", "dick-snout", "The entity code name.");
				cc.set("entity.max-health", 12.5, "The max health this entity can have");
				cc.set("entity.regen-delay", 7.5, "Delay in seconds before regenerating after a delay.");
				cc.set("entity.regen-interval", 1.65, "Regen interval in seconds");
				cc.set("entity.regen-amount", 0.85, "The Regen amount");
				cc.set("entity.max-instances", 128, "The maximum instances of this entity");
				cc.set("entity.context.voice", new GList<String>().qadd("This is something i would say").qadd("This is also what i say").qadd("Somethig else?"));
				
				ConfigurationUI cui = new ConfigurationUI(cc);
				cui.setVisible(true);
			}
		});
		
		tests.put("read-book", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : onlinePlayers())
				{
					for(String j : GBook.read(i.getItemInHand()))
					{
						i.sendMessage(j);
					}
				}
			}
		});
		
		tests.put("relock", new Runnable()
		{
			@Override
			public void run()
			{
				World world = Bukkit.getWorld("world");
				
				for(Player i : world.getPlayers())
				{
					P.showProgress(i, "Locking World");
					PE.BLINDNESS.a(63).d(12000).c(i);
				}
				
				new TaskLater(20)
				{
					@Override
					public void run()
					{
						for(Player i : world.getPlayers())
						{
							P.showProgress(i, "Locking World;Establishing Lock");
						}
						
						WorldLock lock = new WorldLock(world);
						
						new TaskLater(10)
						{
							@Override
							public void run()
							{
								for(Player i : world.getPlayers())
								{
									P.showProgress(i, "Locking World;Locking");
								}
								
								lock.lock();
								
								new TaskLater(15)
								{
									@Override
									public void run()
									{
										for(Player i : world.getPlayers())
										{
											P.showProgress(i, "Releasing World;Establishing Lock");
										}
										
										new TaskLater(10)
										{
											@Override
											public void run()
											{
												for(Player i : world.getPlayers())
												{
													P.showProgress(i, "Releasing World;Releasing");
												}
												
												lock.release();
												
												for(Player i : world.getPlayers())
												{
													P.clearProgress(i);
													P.clearEffects(i);
												}
											}
										};
									}
								};
							}
						};
					}
				};
			}
		});
		
		tests.put("progress", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					P.showProgress(i, "Please Wait");
					
					new TaskLater(100)
					{
						@Override
						public void run()
						{
							P.showProgress(i, "Getting Shit Done");
						}
					};
					
					new TaskLater(110)
					{
						@Override
						public void run()
						{
							P.showProgress(i, "Getting Shit Done;Handling Things");
						}
					};
					
					new TaskLater(200)
					{
						@Override
						public void run()
						{
							P.showProgress(i, "Getting Shit Done;Thrashing");
						}
					};
					
					new TaskLater(250)
					{
						@Override
						public void run()
						{
							P.showProgress(i, "Cleaning Up;Deleting Crap");
						}
					};
					
					new TaskLater(310)
					{
						@Override
						public void run()
						{
							P.clearProgress(i);
						}
					};
				}
			}
		});
		
		tests.put("state-credits", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					NMSX.showEnd(i);
				}
			}
		});
		
		tests.put("state-demo", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					NMSX.showDemo(i);
				}
			}
		});
		
		tests.put("item-da", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : onlinePlayers())
				{
					ItemStack is = i.getItemInHand();
					
					if(Items.is(is))
					{
						Stack s = new Stack(is);
						
						try
						{
							DataCluster cc = new DataCluster(s.toData());
							s("JSON: " + C.stripColor(cc.toJSON().toString()));
							w("RAW: " + new String(cc.compress()));
							
							s("Re-creating");
							
							Stack sc = new Stack(Material.GLASS);
							sc.fromData(cc.compress());
							i.getInventory().addItem(sc.toItemStack());
						}
						
						catch(IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		tests.put("state-controls", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					NMSX.showControls(i);
				}
			}
		});
		
		tests.put("state-rain", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					float[] k = new float[] {0};
					
					new Task(0)
					{
						@Override
						public void run()
						{
							k[0] += 0.1;
							NMSX.showWeather(i, k[0]);
							
							if(k[0] > 10)
							{
								cancel();
							}
						}
					};
				}
			}
		});
		
		tests.put("state-dry", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					float[] k = new float[] {10f};
					
					new Task(0)
					{
						@Override
						public void run()
						{
							k[0] -= 0.1;
							NMSX.showWeather(i, k[0]);
							
							if(k[0] < -10.1)
							{
								cancel();
							}
						}
					};
				}
			}
		});
		
		tests.put("block-broken", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					NMSX.showBlockBreakAnimation(i, i.getLocation().add(0, -1, 0), (int) (Math.random() * 9));
				}
			}
		});
		
		tests.put("state-freak", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					new Task(0)
					{
						@Override
						public void run()
						{
							NMSX.showBrightness(i, (float) (Math.random() * 10));
						}
					};
				}
			}
		});
		
		tests.put("state-dark", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					float[] k = new float[] {0};
					
					new Task(0)
					{
						@Override
						public void run()
						{
							k[0] += 0.1;
							NMSX.showBrightness(i, k[0]);
							
							if(k[0] > 5.5)
							{
								cancel();
							}
						}
					};
				}
			}
		});
		
		tests.put("state-light", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					float[] k = new float[] {5.5f};
					
					new Task(0)
					{
						@Override
						public void run()
						{
							k[0] -= 0.1;
							NMSX.showBrightness(i, k[0]);
							
							if(k[0] < 0.1)
							{
								cancel();
							}
						}
					};
				}
			}
		});
		
		tests.put("state-creep", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					float[] k = new float[] {0f};
					
					new Task(0)
					{
						@Override
						public void run()
						{
							k[0] += 0.1;
							NMSX.showBrightness(i, k[0]);
							
							if(k[0] > 10.1)
							{
								cancel();
							}
						}
					};
				}
			}
		});
		
		tests.put("break-particle", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					NMSX.breakParticles(i.getEyeLocation(), Material.REDSTONE_BLOCK, 24);
				}
			}
		});
		
		tests.put("sourcepack", new Runnable()
		{
			@Override
			public void run()
			{
				new A()
				{
					@Override
					public void async()
					{
						s("Looking for plugins/Phantom/sourcepack/");
						
						File f = new File(getPlugin().getDataFolder(), "sourcepack");
						
						if(f.exists() && f.isDirectory())
						{
							T t = new T()
							{
								@Override
								public void onStop(long nsTime, double msTime)
								{
									s("Done. " + C.AQUA + F.f(msTime, 3) + "ms");
								}
							};
							
							SourcePack s = new SourcePack(f);
							
							s("Packing... plugins/Phantom/source.zip");
							
							try
							{
								s.buildZip(new File(getPlugin().getDataFolder(), "source.zip"));
							}
							
							catch(IOException e)
							{
								f("FAILED: " + e.getClass().getSimpleName());
								ExceptionUtil.print(e);
							}
							
							t.stop();
						}
						
						else
						{
							f("No source");
						}
					}
				};
			}
		});
		
		tests.put("spread-particle", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					NMSX.spreadParticles(i.getEyeLocation(), Material.PORTAL, 24);
				}
			}
		});
		
		tests.put("wraith", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : onlinePlayers())
				{
					Long[] last = new Long[] {M.ms()};
					Long[] lastd = new Long[] {M.ms()};
					
					Wraith wraith = new PhantomWraith(C.LIGHT_PURPLE + "Wraith", TestController.this)
					{
						@Override
						public void onTick()
						{
							if(hasTarget() && getLocation().distance(getTarget().getTarget()) < 4 && M.ms() - last[0] > 6000)
							{
								clearTarget();
							}
							
							else if(!hasTarget() && getLocation().distance(i.getLocation()) > 9)
							{
								setTarget(i);
							}
						}
						
						@Override
						public void onDamage(Entity e, double damage)
						{
							setTarget(e);
							setFocus(e);
							setEquipment(WraithEquipment.HAND, new ItemStack(Material.IRON_SWORD));
							say("Bro, " + e.getName() + " what the fuck is your problem?", 12.4);
							
							new TaskLater(120)
							{
								@Override
								public void run()
								{
									if(M.ms() - lastd[0] > 6000)
									{
										say("Alright, I'm good. I can resume drinking.", 12.7);
										setEquipment(WraithEquipment.HAND, new ItemStack(Material.GLASS_BOTTLE));
									}
								}
							};
						}
						
						@Override
						public void onCollide(Entity e)
						{
							if(M.ms() - last[0] > 3000)
							{
								last[0] = M.ms();
								say(new GList<String>(new String[] {"What's your problem man?", "Back the fuck off dude.", "Bro, what is wrong with you?", "I said back off alright man?", "Get out of my face before I get in yours.", "You are way to close bro.", "Bruh, get out of my safe space", "Stop that, this is my space."}).pickRandom());
								
								setTarget(new WraithTarget(i.getLocation().add(new Vector(Math.random() * 6 - 3, 0, Math.random() * 6 - 3))));
							}
						}
						
						@Override
						public void onInteract(Player p)
						{
							say(Phantom.instance().getMsgx().pickRandom());
							
							setSneaking(true);
							
							int[] k = new int[] {0};
							
							new Task(2)
							{
								@Override
								public void run()
								{
									k[0]++;
									
									if(M.r(0.67))
									{
										setSneaking(!isSneaking());
									}
									
									if(k[0] > 20)
									{
										cancel();
										
										if(isSneaking())
										{
											setSneaking(false);
										}
									}
								}
							};
						}
					};
					
					wraith.spawn(i.getLocation());
					
					new TaskLater()
					{
						@Override
						public void run()
						{
							wraith.setProtected(false);
							wraith.setTarget(i);
							wraith.setFocus(i);
							wraith.setEquipment(WraithEquipment.HAND, new ItemStack(Material.GLASS_BOTTLE));
							wraith.setEquipment(WraithEquipment.LEGGINGS, new ItemStack(Material.LEATHER_LEGGINGS));
							wraith.setEquipment(WraithEquipment.CHESTPLATE, new ItemStack(Material.LEATHER_CHESTPLATE));
							wraith.say("What's up?", 12.8);
						}
					};
				}
			}
		});
		
		tests.put("tabulator", new Runnable()
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
					o("--------------------------------");
					
					for(String j : tab.getTab(i))
					{
						s("ITR: " + j);
					}
				}
			}
		});
		
		tests.put("show-chunks", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player p : Phantom.instance().onlinePlayers())
				{
					new Task(7)
					{
						@Override
						public void run()
						{
							Chunk c = p.getLocation().getChunk();
							
							new A()
							{
								@Override
								public void async()
								{
									for(int i = 0; i < 16; i++)
									{
										for(int j = 0; j < 256; j++)
										{
											for(int k = 0; k < 16; k++)
											{
												Location l = c.getBlock(i, j, k).getLocation();
												
												if(l.getBlockX() % 16 == 0 && l.getBlockY() % 16 == 0)
												{
													EditSessionController.queue(l, new MaterialBlock(Material.STAINED_GLASS, (byte) 14));
												}
												
												if(l.getBlockX() % 16 == 0 && l.getBlockZ() % 16 == 0)
												{
													EditSessionController.queue(l, new MaterialBlock(Material.STAINED_GLASS, (byte) 5));
												}
												
												if(l.getBlockZ() % 16 == 0 && l.getBlockY() % 16 == 0)
												{
													EditSessionController.queue(l, new MaterialBlock(Material.STAINED_GLASS, (byte) 11));
												}
											}
										}
									}
								}
							};
						}
					};
				}
			}
		});
		
		tests.put("hand", new Runnable()
		{
			@Override
			public void run()
			{
				new Task(0)
				{
					@Override
					public void run()
					{
						for(Player i : Phantom.instance().onlinePlayers())
						{
							if(Math.random() > 0.3)
							{
								if(i.getItemInHand() == null || i.getItemInHand().getType().equals(Material.AIR))
								{
									ParticleEffect.LAVA.display(0f, 1, P.getHand(i, -15, 10), i);
								}
								
								else
								{
									if(i.getItemInHand().getType().isBlock())
									{
										ParticleEffect.LAVA.display(0f, 1, P.getHand(i, -15, 20), i);
									}
									
									else
									{
										ParticleEffect.LAVA.display(0f, 1, P.getHand(i), i);
									}
								}
							}
						}
					}
				};
			}
		});
		
		tests.put("dispatcher", new Runnable()
		{
			@Override
			public void run()
			{
				i("Info!");
				s("Success!");
				w("Warning!");
				f("Failure!");
				v("Verbose!");
				o("Overbose!");
				
				new A()
				{
					@Override
					public void async()
					{
						i("Info!");
						s("Success!");
						w("Warning!");
						f("Failure!");
						v("Verbose!");
						o("Overbose!");
					}
				};
			}
		});
		
		tests.put("nest", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					Nest.getBlock(i.getLocation().getBlock()).set("test", M.ms());
					i.sendMessage(Nest.getBlock(i.getLocation().getBlock()).toJSON().toString());
					i.sendMessage("" + Nest.getChunk(i.getLocation().getChunk()).getBlocks().size());
				}
			}
		});
		
		tests.put("param", new Runnable()
		{
			@Override
			public void run()
			{
				s("Test: " + "This is %a% cool %param% %_$Dtest f%");
				s("Whas: " + "%a% = 1, %param% = 2, %_$Dtest f% = 3");
				
				for(String i : F.getParameters("This is %a% cool %param% %_$Dtest f%", '%'))
				{
					v("Found: " + i);
				}
				
				s(new ParameterAdapter()
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
		
		tests.put("async-sync", new Runnable()
		{
			@Override
			public void run()
			{
				new A()
				{
					@Override
					public void async()
					{
						Long air = 0l;
						Long all = 0l;
						World w = W.getAsyncWorld("world");
						
						for(Chunk i : w.getLoadedChunks())
						{
							for(int j = 0; j < 16; j++)
							{
								for(int k = 0; k < 16; k++)
								{
									for(int l = 0; l < 256; l++)
									{
										if(i.getBlock(j, l, k).getType().equals(Material.AIR))
										{
											air++;
										}
										
										all++;
									}
								}
							}
						}
						
						final String percent = "There is " + F.pc(air.doubleValue() / all.doubleValue()) + " air in " + F.f(w.getLoadedChunks().length) + " chunks";
						
						new S()
						{
							@Override
							public void sync()
							{
								for(Player i : Phantom.instance().onlinePlayers())
								{
									i.sendMessage(percent);
								}
							}
						};
					}
				};
			}
		});
		
		tests.put("transmission", new Runnable()
		{
			@Override
			public void run()
			{
				Timer ti = new Timer();
				
				Transmission t = new Transmission("test-packet")
				{
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onResponse(Transmission response)
					{
						ti.stop();
						s(response.getSource() + " < " + F.nsMs(ti.getTime(), 2) + "ms > " + response.getDestination());
					}
				};
				
				try
				{
					ti.start();
					t.transmit();
				}
				
				catch(IOException e)
				{
					ExceptionUtil.print(e);
				}
			}
		});
		
		tests.put("data-ser", new Runnable()
		{
			@Override
			public void run()
			{
				DataCluster cc = new DataCluster();
				cc.set("string", "string");
				cc.set("int", 43);
				
				File f = new File(getPlugin().getDataFolder(), "ser.ser");
				
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
		
		tests.put("slate", new Runnable()
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
		
		tests.put("mfa-cannon", new Runnable()
		{
			@Override
			public void run()
			{
				Audible a = new GSound(Sound.EXPLODE);
				a.setPitch(0.3f);
				a = new MFADistortion(4, 1.8f).distort(a);
				
				for(Player i : Phantom.instance().onlinePlayers())
				{
					a.play(i);
				}
			}
		});
		
		tests.put("mfa-flush", new Runnable()
		{
			@Override
			public void run()
			{
				Audible a = new GSound(Sound.SPLASH);
				a.setPitch(0.3f);
				a = new MFADistortion(8, 1.4f).distort(a);
				
				for(Player i : Phantom.instance().onlinePlayers())
				{
					a.play(i);
				}
			}
		});
		
		tests.put("mfa-twang", new Runnable()
		{
			@Override
			public void run()
			{
				Audible a = new GSound(Sound.ANVIL_LAND);
				a.setPitch(0.6f);
				a.setVolume(0.4f);
				a = new MFADistortion(18, 1.4f).distort(a);
				
				for(Player i : Phantom.instance().onlinePlayers())
				{
					a.play(i);
				}
			}
		});
		
		tests.put("mfa-gush", new Runnable()
		{
			@Override
			public void run()
			{
				Audible a = new GSound(Sound.SLIME_WALK2);
				a.setPitch(0.2f);
				a = new MFADistortion(12, 0.8f).distort(a);
				
				for(Player i : Phantom.instance().onlinePlayers())
				{
					a.play(i);
				}
			}
		});
		
		tests.put("mfa-beast", new Runnable()
		{
			@Override
			public void run()
			{
				float[] ix = new float[] {0};
				
				new Task(1)
				{
					@Override
					public void run()
					{
						Audible a = new GSound(Sound.FIREWORK_LARGE_BLAST);
						a.setPitch(0.1f + ix[0]);
						a.setVolume(ix[0]);
						a = new MFADistortion(12, 1.0f).distort(a);
						
						for(Player i : Phantom.instance().onlinePlayers())
						{
							a.play(i);
						}
						
						ix[0] += 0.04f;
						
						if(ix[0] >= 1.0f)
						{
							cancel();
							
							new TaskLater(2)
							{
								@Override
								public void run()
								{
									Audible a = new GSound(Sound.WITHER_DEATH);
									a.setPitch(0.7f);
									a = new MFADistortion(4, 1.8f).distort(a);
									
									for(Player i : Phantom.instance().onlinePlayers())
									{
										a.play(i);
									}
								}
							};
						}
					}
				};
			}
		});
		
		tests.put("title", new Runnable()
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
		
		tests.put("phantom", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					new Task(0)
					{
						@Override
						public void run()
						{
							NMSX.showPickup(i, i, i);
						}
					};
				}
			}
		});
		
		tests.put("notification", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					Title t = new Title(C.RED + "TITLE", C.GREEN + "SUBTITLE", C.BLUE + "ACTION", 5, 30, 5);
					Notification n = new Notification(t, Priority.HIGH);
					Phantom.queueNotification(i, n);
				}
			}
		});
		
		tests.put("fast-block", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					W.setBlockFast(i.getLocation().add(0, 3, 0), new MaterialBlock(Material.STONE));
				}
			}
		});
		
		tests.put("async-world", new Runnable()
		{
			@Override
			public void run()
			{
				GChunk gc = new GChunk(Bukkit.getPlayer("cyberpwn").getLocation());
				int[] s = new int[] {0, 0};
				
				TaskManager.IMP.async(new Runnable()
				{
					@Override
					public void run()
					{
						GMap<GLocation, MaterialBlock> blocks = W.getChunkBlocksAsync(gc);
						
						for(GLocation i : blocks.k())
						{
							if(blocks.get(i).getMaterial().equals(Material.AIR))
							{
								s[0]++;
							}
							
							s[1]++;
						}
						
						TaskManager.IMP.sync(new RunnableVal<Boolean>()
						{
							@Override
							public void run(Boolean value)
							{
								for(Player i : Bukkit.getOnlinePlayers())
								{
									i.sendMessage("The chunk " + gc.toString() + " has " + F.pc((double) s[0] / (double) s[1]) + " air");
								}
							}
						});
					}
				});
			}
		});
		
		tests.put("formula", new Runnable()
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
						s("Math.sin(" + d + ") / Math.cos(" + k + ") = " + f.evaluate(d, k));
					}
					
					catch(Exception e)
					{
						ExceptionUtil.print(e);
					}
				}
			}
		});
		
		tests.put("notification-multiple", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					Title t = new Title(C.RED + "TITLE", C.GREEN + "SUBTITLE", C.BLUE + "ACTION", 5, 10, 5);
					Notification n = new Notification(t, Priority.HIGH);
					Phantom.queueNotification(i, n);
					
					Title tt = new Title(C.GREEN + "TITLE 2", C.RED + "SUBTITLE 2", C.BLUE + "ACTION 2", 5, 100, 5);
					Notification nt = new Notification(tt, Priority.HIGH);
					Phantom.queueNotification(i, nt);
					
					Title ttt = new Title(C.RED + "TITLE 3", C.BLUE + "SUBTITLE 3", C.BLUE + "ACTION 3", 5, 30, 5);
					Notification ntt = new Notification(ttt, Priority.HIGH);
					Phantom.queueNotification(i, ntt);
				}
			}
		});
		
		tests.put("particle-sphere", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					GList<Location> queue = new GList<Location>();
					
					VisualEffect e = new PhantomEffect()
					{
						@Override
						public void play(Location l)
						{
							if(Math.sin(l.getX()) > Math.cos(l.getZ()))
							{
								queue.add(l);
							}
						}
					};
					
					SphereParticleManipulator s = new SphereParticleManipulator()
					{
						@Override
						public void play(Location l)
						{
							e.play(l);
						}
					};
					
					VisualEffect ex = new PhantomEffect()
					{
						@Override
						public void play(Location l)
						{
							s.play(l, 1 + 5.0, 300.0);
						}
					};
					
					new A()
					{
						@Override
						public void async()
						{
							ex.play(i.getLocation());
							
							new S()
							{
								@Override
								public void sync()
								{
									for(Location l : queue)
									{
										ParticleEffect.FIREWORKS_SPARK.display(0, 1, l, 32);
									}
								}
							};
						}
					};
				}
			}
		});
		
		tests.put("artifact", new Runnable()
		{
			
			@Override
			public void run()
			{
				for(Player i : Phantom.instance().onlinePlayers())
				{
					Schematic s = new Schematic(new Dimension(9, 9, 9));
					s.fill(new MaterialBlock(Material.AIR));
					s.setFaces(new MaterialBlock(Material.THIN_GLASS));
					s.setFace(new MaterialBlock(Material.ENDER_PORTAL), Direction.U);
					s.setFace(new MaterialBlock(Material.GLASS), Direction.D);
					s.distort(new EdgeDistortion(new MaterialBlock(Material.GLOWSTONE)));
					
					Schematic s2 = new Schematic(new Dimension(3, 9, 3));
					s2.fill(new MaterialBlock(Material.AIR));
					s2.setFaces(new MaterialBlock(Material.THIN_GLASS));
					s2.setFace(new MaterialBlock(Material.ENDER_PORTAL), Direction.U);
					s2.setFace(new MaterialBlock(Material.GLASS), Direction.D);
					s2.distort(new EdgeDistortion(new MaterialBlock(Material.GLOWSTONE)));
					
					Artifact a = new WorldArtifact(i.getLocation().add(-3, 6, -3), s);
					Artifact a1 = new WorldArtifact(i.getLocation().add(12, 6, -12), s2);
					Artifact a2 = new WorldArtifact(i.getLocation().add(12, 6, 12), s2);
					Artifact a3 = new WorldArtifact(i.getLocation().add(-12, 6, -12), s2);
					Artifact a4 = new WorldArtifact(i.getLocation().add(-12, 6, 12), s2);
					
					Artifact structure = new WorldStructure(i.getLocation().add(0, 6, 0));
					((WorldStructure) structure).add(a, new Vector(0, 0, 0));
					((WorldStructure) structure).add(a1, new Vector(12, 0, -12));
					((WorldStructure) structure).add(a2, new Vector(12, 0, 12));
					((WorldStructure) structure).add(a3, new Vector(-12, 0, -12));
					((WorldStructure) structure).add(a4, new Vector(-12, 0, 12));
					
					structure.build();
				}
			}
		});
		
		tests.put("gui", new Runnable()
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
				}
			}
		});
		
		tests.put("cluster-read", new Runnable()
		{
			@Override
			public void run()
			{
				DataCluster dc = new DataCluster();
				
				try
				{
					new YAMLDataInput().load(dc, new File(getPlugin().getDataFolder(), "cluster-test.yml"));
					
					for(String i : dc.getData().keySet())
					{
						System.out.println(i + ": " + dc.getAbstract(i));
					}
				}
				
				catch(IOException e)
				{
					ExceptionUtil.print(e);
				}
			}
		});
		
		tests.put("relight", new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : onlinePlayers())
				{
					L.relight(i.getWorld());
				}
			}
		});
	}
	
	public void execute(final CommandSender sender, String test)
	{
		if(!Phantom.instance().getDevelopmentController().tests)
		{
			new MessageBuilder(Phantom.instance()).message(sender, C.GRAY + "Tests Disabled");
		}
		
		for(String i : tests.k())
		{
			if(i.toLowerCase().contains(test.toLowerCase()))
			{
				Timer t = new Timer();
				t.start();
				tests.get(i).run();
				t.stop();
				new MessageBuilder(Phantom.instance()).message(sender, C.GRAY + "Ran Test: " + C.UNDERLINE + C.WHITE + test + C.GRAY + ". Took " + C.WHITE + F.nsMs(t.getTime(), 2));
				break;
			}
		}
	}
	
	@CommandFilter.Tag("Tag")
	@CommandFilter.TagHover("Tag Hover")
	@CommandFilter.PlayerOnly
	@CommandFilter.Permission("must.have.this")
	@CommandAlias("testingalias")
	@Command("testing")
	public void testCommand(PhantomSender sender, PhantomCommand cmd)
	{
		sender.sendMessage(C.WHITE + "Tested");
	}
	
	@Permission("phantom.god")
	@Command("phup")
	public void updateChecker(PhantomSender sender, PhantomCommand cmd)
	{
		if(phups.contains(sender.getPlayer()))
		{
			sender.sendMessage(C.RED + "Off");
			phups.remove(sender.getPlayer());
		}
		
		else
		{
			sender.sendMessage(C.GREEN + "On");
			phups.add(sender.getPlayer());
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(BlockPhysicsEvent e)
	{
		for(Player i : phups)
		{
			i.sendBlockChange(e.getBlock().getLocation(), Material.STAINED_GLASS, (byte) 1);
			
			new TaskLater(2)
			{
				@Override
				public void run()
				{
					i.sendBlockChange(e.getBlock().getLocation(), e.getBlock().getType(), e.getBlock().getData());
				}
			};
		}
	}
	
	public GMap<String, Runnable> getTests()
	{
		return tests;
	}
	
	@Override
	public void onStart()
	{
		
	}
	
	@Override
	public void onStop()
	{
		
	}
}
