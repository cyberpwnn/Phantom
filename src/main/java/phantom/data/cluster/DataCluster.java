package phantom.data.cluster;

import java.lang.reflect.InvocationTargetException;

import org.phantomapi.Phantom;
import org.phantomapi.service.ClusterService;

import phantom.data.ports.IDataPort;
import phantom.dispatch.PD;
import phantom.lang.GList;
import phantom.lang.GMap;
import phantom.util.metrics.Documented;

/**
 * Represents a datacluster
 *
 * @author cyberpwn
 *
 */
@Documented
public class DataCluster implements IDataCluster, IGenericTyped, IShortcodeKeyed
{
	private GMap<String, ICluster<?>> data;

	/**
	 * Create a new datacluster
	 */
	public DataCluster()
	{
		this(new GMap<String, ICluster<?>>());
	}

	private DataCluster(GMap<String, ICluster<?>> data)
	{
		this.data = data;
	}

	@Override
	public boolean contains(String key)
	{
		return data.containsKey(key);
	}

	@Override
	public void setCluster(String key, ICluster<?> cluster)
	{
		data.put(key, cluster);
	}

	@Override
	public ICluster<?> getCluster(String key)
	{
		return data.get(key);
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return Phantom.getService(ClusterService.class).supports(clazz);
	}

	@Override
	public void set(String key, Object object)
	{
		if(key == null)
		{
			PD.f("Key is null");
			return;
		}

		if(object == null)
		{
			PD.f("Object is null for " + key);
			return;
		}

		if(supports(object.getClass()))
		{
			try
			{
				Class<?> aty = Phantom.getService(ClusterService.class).getType(object.getClass()).getDeclaredAnnotation(Cluster.class).type();
				data.put(key, Phantom.getService(ClusterService.class).getType(object.getClass()).getConstructor(aty).newInstance(aty.cast(object)));
			}

			catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				Phantom.kick(e);
			}
		}
	}

	@Override
	public Object get(String key)
	{
		if(!contains(key))
		{
			return null;
		}

		return getCluster(key).get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<?> of)
	{
		return (T) getCluster(key).get();
	}

	@Override
	public Class<?> getType(String key)
	{
		Object o = get(key);

		if(o != null)
		{
			return o.getClass();
		}

		return null;
	}

	@Override
	public DataCluster copy()
	{
		return new DataCluster(data.copy());
	}

	@Override
	public GList<String> k()
	{
		return data.k();
	}

	@Override
	public String getString(String key)
	{
		return get(key, String.class);
	}

	@Override
	public Double getDouble(String key)
	{
		return get(key, Double.class);
	}

	@Override
	public Float getFloat(String key)
	{
		return get(key, Float.class);
	}

	@Override
	public Integer getInteger(String key)
	{
		return get(key, Integer.class);
	}

	@Override
	public Short getShort(String key)
	{
		return get(key, Short.class);
	}

	@Override
	public Long getLong(String key)
	{
		return get(key, Long.class);
	}

	@Override
	public Character getCharacter(String key)
	{
		return get(key, Character.class);
	}

	@Override
	public Boolean getBoolean(String key)
	{
		return get(key, Boolean.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getShortcodedKey(String key)
	{
		return Phantom.getService(ClusterService.class).getShortcodeFor((Class<? extends ICluster<?>>) getCluster(key).getClass(), false);
	}

	@Override
	public String getRealKeyFromShortcoded(String shortcodedKey)
	{
		return shortcodedKey.split("--")[0];
	}

	@Override
	public String getShortCodeFromKey(String shortcodedKey)
	{
		return shortcodedKey.split("--")[1];
	}

	@Override
	public <T> void read(IDataPort<T> port, T source)
	{
		add(port.read(source));
	}

	@Override
	public <T> T write(IDataPort<T> port)
	{
		return port.write(this);
	}

	@Override
	public void add(IDataCluster cluster)
	{
		add(null, cluster);
	}

	@Override
	public void add(String prefix, IDataCluster cluster)
	{
		String pre = prefix == null ? "" : prefix.endsWith(".") ? prefix : prefix + ".";

		for(String i : cluster.k())
		{
			setCluster(pre + i, cluster.getCluster(i));
		}
	}
}
