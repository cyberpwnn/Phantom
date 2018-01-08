package phantom.data.ports;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.phantomapi.Phantom;
import org.phantomapi.service.ClusterService;

import phantom.data.cluster.DataCluster;
import phantom.data.cluster.ICluster;

public class YamlDataPort implements IDataPort<FileConfiguration>
{
	@SuppressWarnings("unchecked")
	@Override
	public FileConfiguration write(DataCluster data)
	{
		FileConfiguration fc = new YamlConfiguration();
		ClusterService svc = Phantom.getService(ClusterService.class);

		for(String i : data.k())
		{
			String key = i;
			String shortc = svc.getShortcodeFor((Class<? extends ICluster<?>>) data.getCluster(key).getClass(), false);
			String finalKey = shortc + "--" + key;
			Object o = data.get(key);
			Class<?> type = data.getType(key);

			if(type.equals(Boolean.class))
			{
				fc.set(finalKey, (Boolean) o);
			}

			else if(type.equals(Character.class))
			{
				fc.set(finalKey, ((Character) o).toString());
			}

			else if(type.equals(Double.class))
			{
				fc.set(finalKey, (Double) o);
			}

			else if(type.equals(Float.class))
			{
				fc.set(finalKey, (Float) o);
			}

			else if(type.equals(Integer.class))
			{
				fc.set(finalKey, (Integer) o);
			}

			else if(type.equals(Long.class))
			{
				fc.set(finalKey, (Long) o);
			}

			else if(type.equals(Short.class))
			{
				fc.set(finalKey, (Integer) o);
			}

			else if(type.equals(String.class))
			{
				fc.set(finalKey, (String) o);
			}

			else
			{
				fc.set(finalKey, o.toString());
			}
		}

		return fc;
	}

	@Override
	public DataCluster read(FileConfiguration source)
	{
		DataCluster cc = new DataCluster();
		ClusterService svc = Phantom.getService(ClusterService.class);

		for(String i : source.getKeys(true))
		{
			String finalKey = i;
			String shortCode = cc.getShortCodeFromKey(finalKey);
			String key = cc.getRealKeyFromShortcoded(finalKey);
			Class<?> type = svc.getType(shortCode);
			Class<? extends ICluster<?>> ctype = svc.getClusterType(shortCode);
			Object o = null;

			if(type.equals(Boolean.class))
			{
				o = Boolean.valueOf(source.getBoolean(key));
				cc.set(key, o);
			}

			else if(type.equals(Character.class))
			{
				o = Character.valueOf(source.getString(key).charAt(0));
				cc.set(key, o);
			}

			else if(type.equals(Double.class))
			{
				o = Double.valueOf(source.getDouble(key));
				cc.set(key, o);
			}

			else if(type.equals(Float.class))
			{
				o = Float.valueOf((float) source.getDouble(key));
				cc.set(key, o);
			}

			else if(type.equals(Integer.class))
			{
				o = Integer.valueOf(source.getInt(key));
				cc.set(key, o);
			}

			else if(type.equals(Long.class))
			{
				o = Long.valueOf(source.getLong(key));
				cc.set(key, o);
			}

			else if(type.equals(Short.class))
			{
				o = Short.valueOf((short) source.getInt(key));
				cc.set(key, o);
			}

			else if(type.equals(String.class))
			{
				o = source.getString(key);
				cc.set(key, o);
			}

			else
			{
				o = source.getString(key);

				try
				{
					ICluster<?> cluster = ctype.getConstructor(type).newInstance(type.cast(null));
					cluster.fromString((String) o);
					cc.setCluster(key, cluster);
				}

				catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
				{
					Phantom.kick(e);
				}
			}
		}

		return cc;
	}

}
