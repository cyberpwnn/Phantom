package phantom.data.ports;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.phantomapi.Phantom;
import org.phantomapi.service.ClusterService;

import phantom.data.cluster.DataCluster;
import phantom.data.cluster.ICluster;
import phantom.lang.GList;

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
			String finalKey = key + "--" + shortc;
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
				fc.set(finalKey, (Integer) ((Short) o).intValue());
			}

			else if(type.equals(String.class))
			{
				fc.set(finalKey, (String) o);
			}

			else if(type.equals(GList.class))
			{
				fc.set(finalKey, (GList<String>) o);
			}

			else
			{
				fc.set(finalKey, data.getCluster(i).asString());
			}
		}

		return fc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataCluster read(FileConfiguration source)
	{
		DataCluster cc = new DataCluster();
		ClusterService svc = Phantom.getService(ClusterService.class);

		for(String i : source.getKeys(true))
		{
			if(!i.contains("--"))
			{
				continue;
			}

			String finalKey = i;
			String shortCode = cc.getShortCodeFromKey(finalKey);
			String key = cc.getRealKeyFromShortcoded(finalKey);
			Class<?> type = svc.getType(shortCode);
			Class<? extends ICluster<?>> ctype = svc.getClusterType(shortCode);
			Object o = null;

			if(type.equals(Boolean.class))
			{
				o = Boolean.valueOf(source.getBoolean(finalKey));
				cc.set(key, o);
			}

			else if(type.equals(Character.class))
			{
				o = Character.valueOf(source.getString(finalKey).charAt(0));
				cc.set(key, o);
			}

			else if(type.equals(Double.class))
			{
				o = Double.valueOf(source.getDouble(finalKey));
				cc.set(key, o);
			}

			else if(type.equals(Float.class))
			{
				o = Float.valueOf((float) source.getDouble(finalKey));
				cc.set(key, o);
			}

			else if(type.equals(Integer.class))
			{
				o = Integer.valueOf(source.getInt(finalKey));
				cc.set(key, o);
			}

			else if(type.equals(Long.class))
			{
				o = Long.valueOf(source.getLong(finalKey));
				cc.set(key, o);
			}

			else if(type.equals(Short.class))
			{
				o = Short.valueOf((short) source.getInt(finalKey));
				cc.set(key, o);
			}

			else if(type.equals(String.class))
			{
				o = source.getString(finalKey);
				cc.set(key, o);
			}

			else if(type.equals(GList.class))
			{
				o = source.getStringList(finalKey);
				cc.set(key, new GList<String>((List<String>) o));
			}

			else
			{
				o = source.getString(finalKey);

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
