package com.volmit.phantom.util;

import org.bukkit.Material;
import org.bukkit.Sound;

public enum RecordType
{
	MUSIC_DISC_13(Material.GOLD_RECORD),
	MUSIC_DISC_CAT(Material.GREEN_RECORD),
	MUSIC_DISC_BLOCKS(Material.RECORD_3),
	MUSIC_DISC_CHIRP(Material.RECORD_4),
	MUSIC_DISC_FAR(Material.RECORD_5),
	MUSIC_DISC_MALL(Material.RECORD_6),
	MUSIC_DISC_MELLOHI(Material.RECORD_7),
	MUSIC_DISC_STAL(Material.RECORD_8),
	MUSIC_DISC_STRAD(Material.RECORD_9),
	MUSIC_DISC_WARD(Material.RECORD_10),
	MUSIC_DISC_11(Material.RECORD_11),
	MUSIC_DISC_WAIT(Material.RECORD_12),
	STOP_PLAYING(Material.AIR);

	private Material mat;

	private RecordType(Material mat)
	{
		this.mat = mat;
	}

	public Material material()
	{
		return mat;
	}

	public String getRecordId()
	{
		return name().toLowerCase();
	}

	public static RecordType from(Sound currentMusic)
	{
		switch(currentMusic)
		{
			case RECORD_11:
				return RecordType.MUSIC_DISC_11;
			case RECORD_13:
				return RecordType.MUSIC_DISC_13;
			case RECORD_BLOCKS:
				return RecordType.MUSIC_DISC_BLOCKS;
			case RECORD_CAT:
				return RecordType.MUSIC_DISC_CAT;
			case RECORD_CHIRP:
				return RecordType.MUSIC_DISC_CHIRP;
			case RECORD_FAR:
				return RecordType.MUSIC_DISC_FAR;
			case RECORD_MALL:
				return RecordType.MUSIC_DISC_MALL;
			case RECORD_MELLOHI:
				return RecordType.MUSIC_DISC_MELLOHI;
			case RECORD_STAL:
				return RecordType.MUSIC_DISC_STAL;
			case RECORD_STRAD:
				return RecordType.MUSIC_DISC_STRAD;
			case RECORD_WAIT:
				return RecordType.MUSIC_DISC_WAIT;
			case RECORD_WARD:
				return RecordType.MUSIC_DISC_WARD;
			default:
				break;
		}

		return null;
	}
}
