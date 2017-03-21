package org.phantomapi.clust;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.phantomapi.Phantom;
import org.phantomapi.data.DBoolean;
import org.phantomapi.data.DDouble;
import org.phantomapi.data.DInteger;
import org.phantomapi.data.DLong;
import org.phantomapi.data.DString;
import org.phantomapi.data.DStringList;
import org.phantomapi.data.DataHandle;
import org.phantomapi.data.DataPack;
import org.phantomapi.lang.GList;
import org.phantomapi.lang.GMap;
import org.phantomapi.lang.GSet;
import org.phantomapi.util.ExceptionUtil;
import org.phantomapi.util.Paste;
import com.google.common.io.ByteStreams;

/**
 * The data cluster holds keyed values in paths ready to be written to files in
 * different ways
 * 
 * @author cyberpwn
 */
public class DataCluster implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GList<LinkedDataCluster> lcd;
	
	/**
	 * @author cyberpwn
	 */
	public enum ClusterDataType
	{
		INTEGER,
		DOUBLE,
		BOOLEAN,
		STRING,
		STRING_LIST,
		LONG;
	}
	
	private Map<String, Cluster> data;
	private Map<String, String> comments;
	public static long perm = 0;
	public static long permX = 0;
	public static long totalSize = 0;
	public static long totalNodes = 0;
	public static long totalClusters = 0;
	private long bytes;
	private long nodes;
	
	/**
	 * Initializes a new data cluster
	 */
	public DataCluster()
	{
		data = new HashMap<String, Cluster>();
		comments = new HashMap<String, String>();
		lcd = new GList<LinkedDataCluster>();
		perm++;
		totalClusters++;
		bytes = 0;
		nodes = 0;
	}
	
	/**
	 * Creates a new datacluster representing the json
	 * 
	 * @param js
	 *            the json object
	 */
	public DataCluster(JSONObject js)
	{
		this();
		
		addJson(js);
	}
	
	/**
	 * Put data from the datapack back into the cluster
	 * 
	 * @param pack
	 *            the pack
	 */
	public void fromDataPack(DataPack pack)
	{
		Iterator<DataHandle> h = pack.getEntities().iterator();
		
		while(h.hasNext())
		{
			DataHandle i = h.next();
			
			if(i instanceof DString)
			{
				String key = ((DString) i).get();
				
				if(h.hasNext())
				{
					DataHandle v = h.next();
					
					if(v instanceof DBoolean)
					{
						set(key, ((DBoolean) v).get());
					}
					
					else if(v instanceof DInteger)
					{
						set(key, ((DInteger) v).get());
					}
					
					else if(v instanceof DLong)
					{
						set(key, ((DLong) v).get());
					}
					
					else if(v instanceof DDouble)
					{
						set(key, ((DDouble) v).get());
					}
					
					else if(v instanceof DString)
					{
						set(key, ((DString) v).get());
					}
					
					else if(v instanceof DStringList)
					{
						set(key, ((DStringList) v).get());
					}
				}
			}
		}
	}
	
	/**
	 * Get the cluster data into a datapack
	 * 
	 * @return the datapack
	 */
	public DataPack toDataPack()
	{
		DataPack dp = new DataPack();
		
		for(String key : keys())
		{
			dp.put(new DString(key));
			
			if(getType(key).equals(ClusterDataType.BOOLEAN))
			{
				dp.put(new DBoolean(getBoolean(key)));
			}
			
			else if(getType(key).equals(ClusterDataType.INTEGER))
			{
				dp.put(new DInteger(getInt(key)));
			}
			
			else if(getType(key).equals(ClusterDataType.LONG))
			{
				dp.put(new DLong(getLong(key)));
			}
			
			else if(getType(key).equals(ClusterDataType.DOUBLE))
			{
				dp.put(new DDouble(getDouble(key)));
			}
			
			else if(getType(key).equals(ClusterDataType.STRING))
			{
				dp.put(new DString(getString(key)));
			}
			
			else if(getType(key).equals(ClusterDataType.STRING_LIST))
			{
				dp.put(new DStringList(new GList<String>(getStringList(key))));
			}
		}
		
		return dp;
	}
	
	/**
	 * Pull a copy of the given cache to this cluster
	 * 
	 * @param name
	 *            the name of the cache
	 */
	public void pullCache(String name)
	{
		try
		{
			setData(Phantom.instance().getCacheController().get(name).copy().getData());
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	/**
	 * Add a copy of this data cluster to a cache for later reading
	 * 
	 * @param name
	 */
	public void cache(String name)
	{
		Phantom.instance().getCacheController().update(name, this);
	}
	
	/**
	 * Creates a new data cluster representing the yaml
	 * 
	 * @param fc
	 *            the file configuration object
	 */
	public DataCluster(FileConfiguration fc)
	{
		this();
		
		addYaml(fc);
	}
	
	/**
	 * Convert compressed data into the cluster
	 * 
	 * @param data
	 *            the data
	 * @throws IOException
	 *             shit happens
	 */
	public DataCluster(byte[] data) throws IOException
	{
		this();
		
		addCompressed(data);
	}
	
	/**
	 * Crop a section out of the data cluster with a link for flushing data back
	 * into this data cluster
	 * 
	 * @param sect
	 *            the section to crop out
	 * @return the linked data cluster
	 */
	public LinkedDataCluster linkSplit(String sect)
	{
		DataCluster cc = crop(sect);
		LinkedDataCluster cl = new LinkedDataCluster(this, sect);
		cl.setData(cc.getData());
		lcd.add(cl);
		
		return cl;
	}
	
	/**
	 * Flush all linked data clusters into the parent (this)
	 */
	public void flushLinks()
	{
		for(LinkedDataCluster i : lcd)
		{
			i.flushParent();
		}
		
		lcd.clear();
	}
	
	/**
	 * Put yaml keys on the data cluster
	 * 
	 * @param fc
	 *            the file configuration
	 */
	public void addYaml(FileConfiguration fc)
	{
		for(String i : fc.getKeys(true))
		{
			trySet(i, fc.get(i));
		}
	}
	
	/**
	 * Pastes the yml to paste.phantomapi.org/
	 * 
	 * @return the url to access it
	 */
	public String paste()
	{
		try
		{
			return Paste.paste(toYaml().saveToString());
		}
		
		catch(Exception e)
		{
			ExceptionUtil.print(e);
		}
		
		return null;
	}
	
	/**
	 * Initializes a new data cluster
	 */
	public DataCluster(Map<String, Cluster> data)
	{
		this();
		
		this.data = data;
		comments = new HashMap<String, String>();
	}
	
	/**
	 * Add a comment to the key
	 * 
	 * @param key
	 *            the key
	 * @param comment
	 *            the comment
	 */
	public void comment(String key, String comment)
	{
		comments.put(key, comment);
	}
	
	/**
	 * Compress the data cluster into gz bytes
	 * 
	 * @return the bytes
	 * @throws IOException
	 *             shit happens
	 */
	public byte[] compress() throws IOException
	{
		String data = toJSON().toString();
		byte[] dat = data.getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		GZIPOutputStream gzo = new GZIPOutputStream(boas);
		gzo.write(dat);
		gzo.close();
		
		return boas.toByteArray();
	}
	
	/**
	 * Adds compressed byte data to the data cluster
	 * 
	 * @param data
	 *            the compressed data
	 * @throws IOException
	 *             shit happens
	 */
	public void addCompressed(byte[] data) throws IOException
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		GZIPInputStream gzi = new GZIPInputStream(bais);
		byte[] dat = ByteStreams.toByteArray(gzi);
		JSONObject js = new JSONObject(new String(dat, StandardCharsets.UTF_8));
		gzi.close();
		addJson(js);
	}
	
	/**
	 * Output all data to a json object
	 * 
	 * @return the json object
	 */
	public JSONObject toJSON()
	{
		JSONObject jso = new JSONObject();
		
		for(String i : getData().keySet())
		{
			jso.put(i, getAbstract(i));
		}
		
		return jso;
	}
	
	/**
	 * Flatten out the datacluster into roots
	 * 
	 * @return the rooted nodes
	 */
	public GMap<String, DataCluster> makeNodes()
	{
		GMap<String, DataCluster> nodes = new GMap<String, DataCluster>();
		
		for(String i : keys())
		{
			if(i.contains("."))
			{
				String[] f = i.split("\\.");
				String key = f[f.length - 1];
				GList<String> ff = new GList<String>(f);
				ff.remove(f.length - 1);
				String root = ff.toString(".");
				
				if(!nodes.contains(root))
				{
					nodes.put(root, new DataCluster());
				}
				
				nodes.get(root).getData().put(key, getData().get(i));
			}
			
			else
			{
				if(!nodes.contains(""))
				{
					nodes.put("", new DataCluster());
				}
				
				nodes.get("").getData().put(i, getData().get(i));
			}
		}
		
		return nodes;
	}
	
	/**
	 * Add the data within the json object to the datacluster
	 * 
	 * @param jso
	 */
	public void addJson(JSONObject jso)
	{
		for(String i : jso.keySet())
		{
			trySet(i, jso.get(i));
		}
	}
	
	/**
	 * Clears the data in this datacluster
	 */
	public void clear()
	{
		totalSize -= bytes;
		data.clear();
		bytes = byteSize();
		totalSize += bytes;
	}
	
	public long byteSize()
	{
		long byteSize = 8;
		
		for(String i : data.keySet())
		{
			byteSize += byteSize(i);
		}
		
		return byteSize;
	}
	
	public long byteSize(String key)
	{
		try
		{
			long k = 8 * (int) ((((key.length()) * 2) + 45) / 8);
			
			if(contains(key))
			{
				if(hasComment(key))
				{
					k *= 2;
					k += 8 * (int) ((((getComment(key).length()) * 2) + 45) / 8);
				}
				
				if(isString(key))
				{
					return k + 8 * (int) ((((getString(key).length()) * 2) + 45) / 8);
				}
				
				if(isStringList(key))
				{
					long kv = 0;
					
					for(String i : getStringList(key))
					{
						kv += 8 * (int) ((((i.length()) * 2) + 45) / 8);
					}
					
					return kv + k;
				}
				
				if(isBoolean(key) || isInteger(key) || isLong(key) || isDouble(key))
				{
					return k + 16;
				}
			}
		}
		
		catch(Exception e)
		{
			return 0;
		}
		
		return 0;
	}
	
	/**
	 * Has Comment?
	 * 
	 * @param key
	 *            the key
	 * @return true if the comment exists
	 */
	public boolean hasComment(String key)
	{
		return comments.containsKey(key);
	}
	
	/**
	 * Get the comment
	 * 
	 * @param key
	 *            the key
	 * @return the comment, or null
	 */
	public String getComment(String key)
	{
		return comments.get(key);
	}
	
	/**
	 * Get the comment in a list split from newline breaks
	 * 
	 * @param key
	 *            the key
	 * @return the list of comments, or empty if no comments
	 */
	public List<String> getComments(String key)
	{
		List<String> comments = new ArrayList<String>();
		
		if(hasComment(key))
		{
			if(getComment(key).contains("\n"))
			{
				for(String i : getComment(key).split("\n"))
				{
					comments.add(i);
				}
			}
			
			else
			{
				comments.add(getComment(key));
			}
		}
		
		return comments;
	}
	
	/**
	 * Get all roots
	 * 
	 * @return the roots
	 */
	public GSet<String> getRoots()
	{
		return getRoots(null);
	}
	
	/**
	 * Get the roots from the prefix. If the prefix is null, returns all roots
	 * 
	 * @param pre
	 *            the prefix
	 * @return the roots
	 */
	public GSet<String> getRoots(String pre)
	{
		GSet<String> roots = new GSet<String>();
		
		for(String i : keys())
		{
			if(pre == null)
			{
				if(i.contains("."))
				{
					roots.add(i.split("\\.")[0]);
				}
				
				else
				{
					roots.add(i);
				}
			}
			
			else
			{
				if(i.startsWith(pre + "."))
				{
					String mod = i.substring((pre + ".").length());
					
					if(mod.contains("."))
					{
						roots.add(mod.split("\\.")[0]);
					}
					
					else
					{
						roots.add(mod);
					}
				}
			}
		}
		
		return roots;
	}
	
	/**
	 * Crop the current data cluster with a copied cluster with the cropped
	 * information. For example if you have
	 * <br/>
	 * <br/>
	 * foo:
	 * <br/>
	 * . bar:
	 * <br/>
	 * . . foobar: 5
	 * <br/>
	 * . . foobar2:
	 * <br/>
	 * . . . foo:
	 * <br/>
	 * . . . . bard: 5
	 * <br/>
	 * <br/>
	 * and invoke DataCluster.crop("foo.bar"); it will return
	 * <br/>
	 * <br/>
	 * foobar: 5
	 * <br/>
	 * foobar2:
	 * <br/>
	 * . foo:
	 * <br/>
	 * . . bard: 5
	 * 
	 * @param root
	 * @return
	 */
	public DataCluster crop(String root)
	{
		DataCluster cc = new DataCluster();
		GMap<String, Cluster> data = new GMap<String, Cluster>();
		
		for(String i : getData().keySet())
		{
			if(i.startsWith(root))
			{
				String key = i.replaceFirst(root, "");
				
				if(key.startsWith("."))
				{
					key = key.substring(1);
				}
				
				data.put(key, getData().get(i));
			}
		}
		
		cc.setData(data);
		
		return cc;
	}
	
	/**
	 * Get the size of this datacluster
	 * 
	 * @return the size of the datacluster
	 */
	public int size()
	{
		return getData().size();
	}
	
	/**
	 * Get the keys in the data cluster
	 * 
	 * @return the keys
	 */
	public GList<String> keys()
	{
		return new GList<String>(getData().keySet());
	}
	
	/**
	 * Get the lines in yml format
	 * 
	 * @param comment
	 *            should we add comments?
	 * @return the list of lines
	 */
	public GList<String> toLines(boolean comment)
	{
		FileConfiguration fc = toYaml();
		GList<String> lines = new GList<String>();
		GList<String> main = new GList<String>();
		
		for(String i : fc.saveToString().split("\n"))
		{
			main.add(i);
		}
		
		for(String i : main)
		{
			String key = i.split(": ")[0].replaceAll(" ", "");
			
			for(String j : fc.getKeys(true))
			{
				if(j.endsWith("." + key))
				{
					if(comment)
					{
						if(hasComment(j))
						{
							lines.add(" ");
							
							for(String k : getComments(j))
							{
								int kx = i.split(": ")[0].split(" ").length - 1;
								lines.add(StringUtils.repeat(" ", kx) + "# " + k);
							}
						}
					}
				}
			}
			
			lines.add(i);
		}
		
		return lines;
	}
	
	/**
	 * Get a FileConfiguration object from this cluster
	 * 
	 * @return the yml object
	 */
	public FileConfiguration toYaml()
	{
		FileConfiguration fc = new YamlConfiguration();
		
		for(String i : data.keySet())
		{
			fc.set(i, getAbstract(i));
		}
		
		return fc;
	}
	
	/**
	 * Check if the given key is the correct type
	 * 
	 * @param key
	 *            the key in question
	 * @return returns true if the key exists and it is of the questioned type.
	 *         returns false if either the key does not exist or it has a
	 *         different type
	 */
	public boolean isString(String key)
	{
		perm++;
		return contains(key, ClusterDataType.STRING);
	}
	
	/**
	 * Check if the given key is the correct type
	 * 
	 * @param key
	 *            the key in question
	 * @return returns true if the key exists and it is of the questioned type.
	 *         returns false if either the key does not exist or it has a
	 *         different type
	 */
	public boolean isStringList(String key)
	{
		perm++;
		return contains(key, ClusterDataType.STRING_LIST);
	}
	
	/**
	 * Check if the given key is the correct type
	 * 
	 * @param key
	 *            the key in question
	 * @return returns true if the key exists and it is of the questioned type.
	 *         returns false if either the key does not exist or it has a
	 *         different type
	 */
	public boolean isInteger(String key)
	{
		perm++;
		return contains(key, ClusterDataType.INTEGER);
	}
	
	/**
	 * Check if the given key is the correct type
	 * 
	 * @param key
	 *            the key in question
	 * @return returns true if the key exists and it is of the questioned type.
	 *         returns false if either the key does not exist or it has a
	 *         different type
	 */
	public boolean isDouble(String key)
	{
		perm++;
		return contains(key, ClusterDataType.DOUBLE);
	}
	
	/**
	 * Check if the given key is the correct type
	 * 
	 * @param key
	 *            the key in question
	 * @return returns true if the key exists and it is of the questioned type.
	 *         returns false if either the key does not exist or it has a
	 *         different type
	 */
	public boolean isLong(String key)
	{
		perm++;
		return contains(key, ClusterDataType.LONG);
	}
	
	/**
	 * Check if the given key is the correct type
	 * 
	 * @param key
	 *            the key in question
	 * @return returns true if the key exists and it is of the questioned type.
	 *         returns false if either the key does not exist or it has a
	 *         different type
	 */
	public boolean isBoolean(String key)
	{
		perm++;
		return contains(key, ClusterDataType.BOOLEAN);
	}
	
	/**
	 * Check if the given key and type exist in the data cluster
	 * 
	 * @param key
	 *            the key
	 * @param t
	 *            the type
	 * @return true if the data cluster contains the key and that key's value is
	 *         of the type T
	 */
	public boolean contains(String key, ClusterDataType t)
	{
		perm++;
		return contains(key) && getType(key).equals(t);
	}
	
	/**
	 * Attempt to set a value to a path. Make sure it is a compatible type
	 * 
	 * @param key
	 *            the path
	 * @param o
	 *            the object
	 * @param comment
	 *            the comment
	 */
	public void trySet(String key, Object o, String comment)
	{
		perm++;
		trySet(key, o);
		comment(key, comment);
	}
	
	/**
	 * Attempt to set a value to a path. Make sure it is a compatible type
	 * 
	 * @param key
	 *            the path
	 * @param o
	 *            the object
	 */
	public void trySet(String key, Object o)
	{
		if(o == null)
		{
			return;
		}
		
		if(o instanceof Integer)
		{
			set(key, (Integer) o);
		}
		
		if(o instanceof Long)
		{
			set(key, (Long) o);
		}
		
		else if(o instanceof String)
		{
			set(key, (String) o);
		}
		
		else if(o instanceof Double)
		{
			set(key, (Double) o);
		}
		
		else if(o instanceof Boolean)
		{
			set(key, (Boolean) o);
		}
		
		else if(o instanceof List)
		{
			List<String> l = new ArrayList<String>();
			
			for(Object i : (List<?>) o)
			{
				l.add(i.toString());
			}
			
			set(key, l);
		}
		
		else if(o instanceof JSONArray)
		{
			List<String> l = new ArrayList<String>();
			
			for(Object i : ((JSONArray) o))
			{
				l.add(i.toString());
			}
			
			set(key, l);
		}
		
		perm++;
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, int value)
	{
		totalSize -= bytes;
		bytes -= byteSize(key);
		data.put(key, new ClusterInteger(value));
		perm++;
		bytes += byteSize(key);
		totalSize += bytes;
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, long value)
	{
		totalSize -= bytes;
		bytes -= byteSize(key);
		data.put(key, new ClusterLong(value));
		perm++;
		bytes += byteSize(key);
		totalSize += bytes;
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, double value)
	{
		totalSize -= bytes;
		bytes -= byteSize(key);
		data.put(key, new ClusterDouble(value));
		perm++;
		bytes += byteSize(key);
		totalSize += bytes;
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, boolean value)
	{
		totalSize -= bytes;
		bytes -= byteSize(key);
		data.put(key, new ClusterBoolean(value));
		perm++;
		bytes += byteSize(key);
		totalSize += bytes;
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, String value)
	{
		totalSize -= bytes;
		bytes -= byteSize(key);
		data.put(key, new ClusterString(value));
		perm++;
		bytes += byteSize(key);
		totalSize += bytes;
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 */
	public void set(String key, List<String> value)
	{
		totalSize -= bytes;
		bytes -= byteSize(key);
		data.put(key, new ClusterStringList(value));
		perm++;
		bytes += byteSize(key);
		totalSize += bytes;
	}
	
	/**
	 * Write a custom meta object to this data cluster
	 * 
	 * @param key
	 *            the key
	 * @param object
	 *            the meta object
	 */
	public void set(String key, MetaObject object)
	{
		object.write(this, key);
	}
	
	/**
	 * Get a meta object from the given key and type
	 * 
	 * @param key
	 *            the key
	 * @param clazz
	 *            the class of the meta object
	 * @return the meta object or null. Exceptions are printed.
	 */
	@SuppressWarnings("unchecked")
	public <T extends MetaObject> T getObject(String key, Class<T> clazz)
	{
		try
		{
			MetaObject v = clazz.newInstance();
			v.read(this, key);
			return (T) v;
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Set an enum
	 * 
	 * @param key
	 *            the key
	 * @param enumeration
	 *            the enum
	 */
	public void set(String key, Enum<?> enumeration)
	{
		set(key, enumeration.name());
	}
	
	/**
	 * Get an enum from the given class
	 * 
	 * @param key
	 *            the key
	 * @param clazz
	 *            the enum class
	 * @return the enum or null
	 */
	public <T extends Enum<?>> T getEnum(String key, Class<T> clazz)
	{
		for(T i : clazz.getEnumConstants())
		{
			if(i.name().equals(getString(key)))
			{
				return i;
			}
		}
		
		return null;
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, int value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, long value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, double value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, boolean value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, String value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Sets the value to the path key defined
	 * 
	 * @param key
	 *            the key used to access this value
	 * @param value
	 *            the value assigned to this key
	 * @param comment
	 *            the comment (multiline supported with the \n symbol)
	 */
	public void set(String key, List<String> value, String comment)
	{
		set(key, value);
		comment(key, comment);
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public Boolean getBoolean(String key)
	{
		perm++;
		if(contains(key) && getType(key).equals(ClusterDataType.BOOLEAN))
		{
			return ((ClusterBoolean) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Add all data from the given data cluster into this data cluster with a
	 * prefixed key adapted from the given data cluster
	 * 
	 * @param data
	 *            the data cluster to add to this data cluster
	 * @param prefix
	 *            the prefix to adapt the keys from the given data cluster
	 */
	public void add(DataCluster data, String prefix)
	{
		for(String i : data.getData().keySet())
		{
			getData().put(prefix + i, data.getData().get(i));
		}
	}
	
	/**
	 * Add all data from the given data cluster into this data cluster
	 * 
	 * @param data
	 *            the data cluster to add to this data cluster
	 */
	public void add(DataCluster data)
	{
		add(data, "");
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public Long getLong(String key)
	{
		perm++;
		if(contains(key) && getType(key).equals(ClusterDataType.LONG))
		{
			return ((ClusterLong) get(key)).get();
		}
		
		if(contains(key) && getType(key).equals(ClusterDataType.INTEGER))
		{
			return (long) ((ClusterInteger) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public Integer getInt(String key)
	{
		perm++;
		if(contains(key) && getType(key).equals(ClusterDataType.INTEGER))
		{
			return ((ClusterInteger) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public Double getDouble(String key)
	{
		perm++;
		
		if(contains(key) && getType(key).equals(ClusterDataType.DOUBLE))
		{
			return ((ClusterDouble) get(key)).get();
		}
		
		if(contains(key) && getType(key).equals(ClusterDataType.INTEGER))
		{
			return (double) ((ClusterInteger) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public String getString(String key)
	{
		perm++;
		if(contains(key) && getType(key).equals(ClusterDataType.STRING))
		{
			return ((ClusterString) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Get the object from the given path
	 * 
	 * @param key
	 *            the given path
	 * @return the object at the given path. Null if no path
	 */
	public List<String> getStringList(String key)
	{
		perm++;
		if(contains(key) && getType(key).equals(ClusterDataType.STRING_LIST))
		{
			return ((ClusterStringList) get(key)).get();
		}
		
		return null;
	}
	
	/**
	 * Check if the cluster contains the given key
	 * 
	 * @param key
	 *            the given key
	 * @return true if key exists, false if no such key
	 */
	public boolean contains(String key)
	{
		perm++;
		return data.containsKey(key) && data.get(key) != null;
	}
	
	/**
	 * Remove the key and assigned object from the cluster
	 * 
	 * @param key
	 *            the given key
	 */
	public void remove(String key)
	{
		perm++;
		data.remove(key);
	}
	
	/**
	 * Get an object from the key, does not require a known type for getting it.
	 * 
	 * @param key
	 *            the given key
	 * @return the object
	 */
	public Object getAbstract(String key)
	{
		perm++;
		if(getType(key).equals(ClusterDataType.BOOLEAN))
		{
			return getBoolean(key);
		}
		
		else if(getType(key).equals(ClusterDataType.INTEGER))
		{
			return getInt(key);
		}
		
		else if(getType(key).equals(ClusterDataType.LONG))
		{
			return getLong(key);
		}
		
		else if(getType(key).equals(ClusterDataType.DOUBLE))
		{
			return getDouble(key);
		}
		
		else if(getType(key).equals(ClusterDataType.STRING))
		{
			return getString(key);
		}
		
		else if(getType(key).equals(ClusterDataType.STRING_LIST))
		{
			return getStringList(key);
		}
		
		return null;
	}
	
	/**
	 * Get the raw cluster object for the key
	 * 
	 * @param key
	 *            the given key
	 * @return the cluster object
	 */
	public Cluster get(String key)
	{
		perm++;
		totalNodes -= nodes;
		nodes = size();
		totalNodes += nodes;
		return data.get(key);
	}
	
	/**
	 * Get the cluster type for the given key
	 * 
	 * @param key
	 *            the key
	 * @return the type
	 */
	public ClusterDataType getType(String key)
	{
		perm++;
		return get(key).getType();
	}
	
	/**
	 * Get the cluster set
	 * 
	 * @return the map
	 */
	public Map<String, Cluster> getData()
	{
		perm++;
		return data;
	}
	
	/**
	 * Set the data
	 * 
	 * @param data
	 *            the data
	 */
	public void setData(Map<String, Cluster> data)
	{
		totalSize -= bytes;
		perm++;
		this.data = data;
		bytes = byteSize();
		totalSize += bytes;
	}
	
	/**
	 * Clones the controller
	 * 
	 * @return the controller
	 */
	public DataCluster copy()
	{
		return new DataCluster(getData());
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bytes ^ (bytes >>> 32));
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + (int) (nodes ^ (nodes >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(getClass() != obj.getClass())
		{
			return false;
		}
		
		DataCluster other = (DataCluster) obj;
		
		if(bytes != other.bytes)
		{
			return false;
		}
		
		if(comments == null)
		{
			if(other.comments != null)
			{
				return false;
			}
		}
		
		else if(!comments.equals(other.comments))
		{
			return false;
		}
		
		if(data == null)
		{
			if(other.data != null)
			{
				return false;
			}
		}
		
		else if(!data.equals(other.data))
		{
			return false;
		}
		
		if(nodes != other.nodes)
		{
			return false;
		}
		
		return true;
	}
}
