package com.volmit.phantom.rift;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;

import com.volmit.phantom.json.JSONException;
import com.volmit.phantom.json.JSONObject;
import com.volmit.phantom.lang.D;
import com.volmit.phantom.lang.GList;
import com.volmit.phantom.lang.GMap;
import com.volmit.phantom.lang.VIO;
import com.volmit.phantom.plugin.AR;
import com.volmit.phantom.plugin.Phantom;
import com.volmit.phantom.plugin.PhantomPlugin;
import com.volmit.phantom.plugin.R;
import com.volmit.phantom.plugin.SVC;
import com.volmit.phantom.plugin.Scaffold.Async;
import com.volmit.phantom.services.RiftSVC;
import com.volmit.phantom.stack.PhantomModule;
import com.volmit.phantom.time.M;

public class PhantomRift implements Rift, Listener
{
	private World world;
	private PhysicsEngine engine;
	private AR ar;
	private String name;
	private Class<? extends ChunkGenerator> generator;
	private Environment environment;
	private GameMode forced;
	private Difficulty difficulty;
	private GMap<String, String> rules = new GMap<>();
	private long seed;
	private int physicsDelay;
	private int secondsUnload;
	private double entityThrottle;
	private double tileThrottle;
	private boolean temporary;
	private int maxTntUpdates;
	private int animalActivation;
	private int miscActivation;
	private int monsterActivation;
	private int arrowDespawn;
	private int itemDespawn;
	private double xpMerge;
	private double itemMerge;
	private int viewDistance;
	private int hopperAmount;
	private int hopperCheck;
	private int hopperRate;
	private int hangingTick;
	private boolean nerfSpawners;
	private int playerTracking;
	private boolean randomLight;
	private boolean allowBosses;
	private long lastTickOccupied;
	private long lockTime;

	public PhantomRift(String name) throws RiftException
	{
		setName(name);
		setAllowBosses(false);
		setRandomLightUpdates(false);
		setPlayerTrackingRange(256);
		setUnloadWhenEmpty(-1);
		setNerfSpawnerMobs(true);
		setHangingTickRate(200);
		setHopperTransferAmount(64);
		setHopperCheckRate(20);
		setHopperTransferRate(20);
		setMaxTNTUpdatesPerTick(20);
		setAnimalActivationRange(9);
		setMiscActivationRange(9);
		setMonsterActivationRange(9);
		setArrowDespawnRate(5);
		setItemDespawnRate(1200);
		setXPMergeRadius(5);
		setItemMergeRadius(2.5);
		setViewDistance(M.iclip(Bukkit.getViewDistance(), 2, 10));
		setEnvironment(Environment.NORMAL);
		setTemporary(false);
		setGenerator(VoidGenerator.class);
		setSeed((long) (Long.MIN_VALUE + (Math.random() * Long.MAX_VALUE)));
		setEntityTickLimit(0.3);
		setTileTickLimit(0.7);
		setForcedGameMode(null);
		setPhysicsThrottle(5);
		setDifficulty(Difficulty.PEACEFUL);
		setLockTime(-1);
		setRule("announceAdvancements", "false");
		setRule("commandBlocksEnabled", "false");
		setRule("commandBlockOutput", "false");
		setRule("disableElytraMovementCheck", "true");
		setRule("maxEntityCramming", "1");
		setRule("mobGriefing", "false");
		setRule("pvp", "false");
		setRule("randomTickSpeed", "true");
		handleConfig();
	}

	private void handleConfig()
	{
		File config = new File(getWorldFolder(), "rift.json");

		if(config.exists())
		{
			try
			{
				fromJSON(new JSONObject(VIO.readAll(config)));
			}

			catch(ClassNotFoundException | JSONException | IOException e)
			{
				D.as("Rift").f("Failed to load settings for rift " + getName());
				e.printStackTrace();
			}
		}

		config.getParentFile().mkdirs();
		writeJSON();
	}

	private void writeJSON()
	{
		File config = new File(getWorldFolder(), "rift.json");
		config.getParentFile().mkdirs();

		try
		{
			VIO.writeAll(config, toJSON().toString(2));
		}

		catch(JSONException | IOException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void fromJSON(JSONObject j) throws ClassNotFoundException, JSONException
	{
		lockTime = j.getLong("lock-time");
		allowBosses = j.getBoolean("allow-bosses");
		randomLight = j.getBoolean("random-light-updates");
		playerTracking = j.getInt("player-tracking");
		nerfSpawners = j.getBoolean("nerf-spawners");
		hangingTick = j.getInt("hanging-tick");
		hopperRate = j.getInt("hopper-rate");
		hopperCheck = j.getInt("hopper-check");
		hopperAmount = j.getInt("hopper-amount");
		viewDistance = j.getInt("view-distance");
		itemMerge = j.getDouble("item-merge");
		xpMerge = j.getDouble("xp-merge");
		itemDespawn = j.getInt("item-despawn");
		arrowDespawn = j.getInt("arrow-despawn");
		miscActivation = j.getInt("misc-activation");
		monsterActivation = j.getInt("monster-activation");
		animalActivation = j.getInt("animal-activation");
		maxTntUpdates = j.getInt("max-tnt-updates");
		temporary = j.getBoolean("temporary");
		tileThrottle = j.getDouble("tile-throttle");
		entityThrottle = j.getDouble("entity-throttle");
		secondsUnload = j.getInt("seconds-unload");
		physicsDelay = j.getInt("physics-delay");
		name = j.getString("name");
		generator = (Class<? extends ChunkGenerator>) Class.forName(j.getString("generator"));
		environment = Environment.valueOf(j.getString("environment"));
		forced = j.getString("gamemode").equals("NULL") ? null : GameMode.valueOf(j.getString("gamemode"));
		difficulty = j.getString("difficulty").equals("NULL") ? null : Difficulty.valueOf(j.getString("difficulty"));
		seed = j.getLong("seed");
		rules = parseRules(j.getJSONObject("rules"));
	}

	@Override
	public JSONObject toJSON()
	{
		JSONObject j = new JSONObject();

		j.put("rules", ruleObject());
		j.put("allow-bosses", allowBosses);
		j.put("lock-time", lockTime);
		j.put("random-light-updates", randomLight);
		j.put("player-tracking", playerTracking);
		j.put("nerf-spawners", nerfSpawners);
		j.put("hanging-tick", hangingTick);
		j.put("hopper-rate", hopperRate);
		j.put("hopper-check", hopperCheck);
		j.put("hopper-amount", hopperAmount);
		j.put("view-distance", viewDistance);
		j.put("item-merge", itemMerge);
		j.put("xp-merge", xpMerge);
		j.put("item-despawn", itemDespawn);
		j.put("arrow-despawn", arrowDespawn);
		j.put("misc-activation", miscActivation);
		j.put("monster-activation", monsterActivation);
		j.put("animal-activation", animalActivation);
		j.put("max-tnt-updates", maxTntUpdates);
		j.put("temporary", temporary);
		j.put("tile-throttle", tileThrottle);
		j.put("entity-throttle", entityThrottle);
		j.put("seconds-unload", secondsUnload);
		j.put("physics-delay", physicsDelay);
		j.put("name", name);
		j.put("generator", generator.getCanonicalName());
		j.put("environment", environment.name());
		j.put("gamemode", forced == null ? "NULL" : forced.name());
		j.put("difficulty", difficulty == null ? "NULL" : difficulty.name());
		j.put("seed", seed);

		return j;
	}

	private GMap<String, String> parseRules(JSONObject j)
	{
		GMap<String, String> r = new GMap<>();

		for(String i : j.keySet())
		{
			r.put(i, j.getString(i));
		}

		return r;
	}

	private JSONObject ruleObject()
	{
		JSONObject j = new JSONObject();

		for(String i : getRules())
		{
			j.put(i, getRule(i));
		}

		return j;
	}

	@Async
	public void tick()
	{
		if(isLoaded())
		{
			getPhysicsEngine().update();

			if(M.interval(5))
			{
				if(isLockingTime() && getWorld().getTime() != getLockedTime())
				{
					getWorld().setTime(getLockedTime());
				}

				for(Player i : getWorld().getPlayers())
				{
					if(getForcedGameMode() != null)
					{
						if(!i.getGameMode().equals(getForcedGameMode()))
						{
							i.sendMessage(PhantomModule.instance.getTag("Rift") + " This rift is forcing the gamemode " + getForcedGameMode().name().toLowerCase());
							new R().sync(() -> i.setGameMode(getForcedGameMode())).start();
						}
					}

					lastTickOccupied = M.tick() + 2;
				}

				if(!areBossesAllowed())
				{
					for(EnderDragon i : getWorld().getEntitiesByClass(EnderDragon.class))
					{
						new R().sync(() -> i.remove()).start();
					}

					for(Wither i : getWorld().getEntitiesByClass(Wither.class))
					{
						new R().sync(() -> i.remove()).start();
					}
				}

				if(!getWorld().getDifficulty().equals(getDifficulty()))
				{
					new R().sync(() -> getWorld().setDifficulty(getDifficulty())).start();
				}

				if(getTicksWhenEmpty() >= 0)
				{
					if(lastTickOccupied <= M.tick())
					{
						if(M.tick() - lastTickOccupied > getTicksWhenEmpty())
						{
							new R().sync(() -> unload()).start();
							lastTickOccupied = M.tick() + 2;
						}
					}
				}
			}
		}
	}

	@Override
	public Rift send(Player p)
	{
		if(!SVC.get(RiftSVC.class).isInRift(p.getWorld()))
		{
			p.teleport(getSpawn());
		}

		return this;
	}

	@Override
	public Environment getEnvironment()
	{
		return environment;
	}

	@Override
	public Rift setEnvironment(Environment environment)
	{
		this.environment = environment;
		return this;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Rift setName(String name)
	{
		this.name = name;
		return this;
	}

	@Override
	public File getWorldFolder()
	{
		return new File(getName());
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public Rift destroy()
	{
		if(isLoaded())
		{
			unload();
		}

		SVC.get(RiftSVC.class).deleteRift(getName());
		return this;
	}

	@Override
	public Rift reload()
	{
		if(!isLoaded())
		{
			return this;
		}

		GList<Player> playersInRift = new GList<>();

		for(Player i : getWorld().getPlayers())
		{
			playersInRift.add(i);
		}

		unload();
		load();

		for(Player i : playersInRift)
		{
			i.sendMessage(PhantomModule.instance.getTag("Rift") + " The rift has been re-opened. Teleporting Back.");
			i.teleport(getWorld().getSpawnLocation());
		}

		return this;
	}

	@Override
	public Rift load()
	{
		if(isLoaded())
		{
			return this;
		}

		writeJSON();

		try
		{
			world = new WorldCreator(getName()).environment(getEnvironment()).seed(seed).generator(getGenerator().getConstructor().newInstance()).createWorld();

			for(String i : getRules())
			{
				try
				{
					if(!world.setGameRuleValue(i, getRule(i)))
					{
						D.as("Rift Service").w("Invalid Game Rule '" + i + " = " + getRule(i) + "'");
					}
				}

				catch(Throwable e)
				{
					D.as("Rift Service").w("Invalid Game Rule '" + i + " = " + getRule(i) + "'");
				}
			}

			ar = new AR(0)
			{
				@Override
				public void run()
				{
					try
					{
						tick();
					}

					catch(Throwable e)
					{
						e.printStackTrace();
					}
				}
			};
			Bukkit.getPluginManager().registerEvents(this, PhantomPlugin.plugin);
			engine = new PhysicsEngine(this);

			try
			{
				getPhysicsEngine().inject();
			}

			catch(IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e)
			{
				e.printStackTrace();
			}
		}

		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1)
		{
			e1.printStackTrace();
		}

		return this;
	}

	@Override
	public Rift unload()
	{
		if(!isLoaded())
		{
			return this;
		}

		writeJSON();

		try
		{
			ar.cancel();
		}

		catch(Throwable e)
		{

		}

		try
		{
			getPhysicsEngine().reset();
		}

		catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}

		HandlerList.unregisterAll();
		colapse();
		Bukkit.unloadWorld(getWorld(), !isTemporary());
		world = null;
		engine = null;
		return this;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Rift colapse()
	{
		if(!isLoaded())
		{
			return this;
		}

		for(Player i : getWorld().getPlayers())
		{
			i.teleport(Phantom.getDefaultWorld().getSpawnLocation());
			i.sendMessage(PhantomModule.instance.getTag("Rift") + " This Rift is colapsing! You were teleported out.");
		}

		for(Chunk i : getWorld().getLoadedChunks())
		{
			if(isTemporary())
			{
				i.unload(false, true);
			}

			else
			{
				i.unload(true, true);
			}
		}

		return this;
	}

	@Override
	public Rift save()
	{
		if(isLoaded() && !isTemporary())
		{
			getWorld().save();
		}

		return this;
	}

	@Override
	public Rift setTemporary(boolean temporary)
	{
		this.temporary = temporary;
		return this;
	}

	@Override
	public boolean isTemporary()
	{
		return temporary;
	}

	@Override
	public boolean isLoaded()
	{
		return world != null;
	}

	@Override
	public Rift setGenerator(Class<? extends ChunkGenerator> generator)
	{
		this.generator = generator;
		return this;
	}

	@Override
	public Class<? extends ChunkGenerator> getGenerator()
	{
		return generator;
	}

	@Override
	public long getSeed()
	{
		return seed;
	}

	@Override
	public Rift setSeed(long seed)
	{
		this.seed = seed;
		return this;
	}

	@Override
	public Rift setPhysicsThrottle(int delay)
	{
		physicsDelay = delay;
		return this;
	}

	@Override
	public int getPhysicsThrottle()
	{
		return physicsDelay;
	}

	@Override
	public Rift setEntityTickLimit(double ms)
	{
		entityThrottle = ms;
		return this;
	}

	@Override
	public Rift setTileTickLimit(double ms)
	{
		tileThrottle = ms;
		return this;
	}

	@Override
	public double getEntityTickTime()
	{
		return isLoaded() ? getPhysicsEngine().getEntityTime() : 0D;
	}

	@Override
	public double getTileTickTime()
	{
		return isLoaded() ? getPhysicsEngine().getTileTime() : 0D;
	}

	@Override
	public PhysicsEngine getPhysicsEngine()
	{
		return engine;
	}

	@Override
	public double getEntityTickLimit()
	{
		return entityThrottle;
	}

	@Override
	public double getTileTickLimit()
	{
		return tileThrottle;
	}

	@Override
	public int getMaxTNTUpdatesPerTick()
	{
		return maxTntUpdates;
	}

	@Override
	public Rift setMaxTNTUpdatesPerTick(int max)
	{
		maxTntUpdates = max;
		return this;
	}

	@Override
	public int getAnimalActivationRange()
	{
		return animalActivation;
	}

	@Override
	public Rift setAnimalActivationRange(int blocks)
	{
		animalActivation = blocks;
		return this;
	}

	@Override
	public int getMiscActivationRange()
	{
		return miscActivation;
	}

	@Override
	public Rift setMiscActivationRange(int blocks)
	{
		miscActivation = blocks;
		return this;
	}

	@Override
	public int getMonsterActivationRange()
	{
		return monsterActivation;
	}

	@Override
	public Rift setMonsterActivationRange(int blocks)
	{
		monsterActivation = blocks;
		return this;
	}

	@Override
	public int getArrowDespawnRate()
	{
		return arrowDespawn;
	}

	@Override
	public Rift setArrowDespawnRate(int ticks)
	{
		arrowDespawn = ticks;
		return this;
	}

	@Override
	public int getItemDespawnRate()
	{
		return itemDespawn;
	}

	@Override
	public Rift setItemDespawnRate(int ticks)
	{
		itemDespawn = ticks;
		return this;
	}

	@Override
	public double getXPMergeRadius()
	{
		return xpMerge;
	}

	@Override
	public Rift setXPMergeRadius(double radius)
	{
		xpMerge = radius;
		return this;
	}

	@Override
	public double getItemMergeRadius()
	{
		return itemMerge;
	}

	@Override
	public Rift setItemMergeRadius(double radius)
	{
		itemMerge = radius;
		return this;
	}

	@Override
	public int getViewDistance()
	{
		return viewDistance;
	}

	@Override
	public Rift setViewDistance(int viewDistance)
	{
		this.viewDistance = viewDistance;
		return this;
	}

	@Override
	public int getHopperTransferAmount()
	{
		return hopperAmount;
	}

	@Override
	public Rift setHopperTransferAmount(int amt)
	{
		hopperAmount = amt;
		return this;
	}

	@Override
	public int getHopperTransferRate()
	{
		return hopperRate;
	}

	@Override
	public Rift setHopperTransferRate(int ticks)
	{
		hopperRate = ticks;
		return this;
	}

	@Override
	public int getHopperCheckRate()
	{
		return hopperCheck;
	}

	@Override
	public Rift setHopperCheckRate(int ticks)
	{
		hopperCheck = ticks;
		return this;
	}

	@Override
	public int getHangingTickRate()
	{
		return hangingTick;
	}

	@Override
	public Rift setHangingTickRate(int ticks)
	{
		hangingTick = ticks;
		return this;
	}

	@Override
	public boolean isNerfSpawnerMobs()
	{
		return nerfSpawners;
	}

	@Override
	public Rift setNerfSpawnerMobs(boolean nerf)
	{
		nerfSpawners = nerf;
		return this;
	}

	@Override
	public int getPlayerTrackingRange()
	{
		return playerTracking;
	}

	@Override
	public Rift setPlayerTrackingRange(int range)
	{
		playerTracking = range;
		return this;
	}

	@Override
	public boolean isRandomLightUpdates()
	{
		return randomLight;
	}

	@Override
	public Rift setRandomLightUpdates(boolean b)
	{
		randomLight = b;
		return this;
	}

	@Override
	public Rift setSpawn(Location location)
	{
		getWorld().setSpawnLocation(location);
		return this;
	}

	@Override
	public Location getSpawn()
	{
		return getWorld().getSpawnLocation();
	}

	@Override
	public Rift setAllowBosses(boolean allowBosses)
	{
		this.allowBosses = allowBosses;
		return this;
	}

	@Override
	public boolean areBossesAllowed()
	{
		return allowBosses;
	}

	@Override
	public Rift setForcedGameMode(GameMode gm)
	{
		forced = gm;
		return this;
	}

	@Override
	public boolean isForcingGameMode()
	{
		return getForcedGameMode() != null;
	}

	@Override
	public GameMode getForcedGameMode()
	{
		return forced;
	}

	@Override
	public Difficulty getDifficulty()
	{
		return difficulty;
	}

	@Override
	public Rift setDifficulty(Difficulty difficulty)
	{
		this.difficulty = difficulty;
		return this;
	}

	@Override
	public Rift setUnloadWhenEmpty(int seconds)
	{
		secondsUnload = seconds;
		return this;
	}

	@Override
	public int getTicksWhenEmpty()
	{
		return secondsUnload * 20;
	}

	@Override
	public Rift setRule(String key, String value)
	{
		rules.put(key, value);
		return this;
	}

	@Override
	public String getRule(String key)
	{
		return rules.get(key);
	}

	@Override
	public Rift removeRule(String key)
	{
		rules.remove(key);
		return this;
	}

	@Override
	public GList<String> getRules()
	{
		return rules.k();
	}

	@Override
	public boolean isLockingTime()
	{
		return getLockedTime() != -1;
	}

	@Override
	public long getLockedTime()
	{
		return lockTime;
	}

	@Override
	public Rift setLockTime(long time)
	{
		lockTime = time;
		return this;
	}
}
