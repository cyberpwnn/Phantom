package org.cyberpwn.phantom.gui;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.Phantom;
import org.cyberpwn.phantom.lang.Priority;
import org.cyberpwn.phantom.lang.Title;
import org.cyberpwn.phantom.sfx.Audible;
import org.cyberpwn.phantom.sync.TaskLater;
import org.cyberpwn.phantom.vfx.VisualEffect;

/**
 * Notification instance
 * 
 * @author cyberpwn
 *
 */
public class Notification
{
	private Title title;
	private Priority priority;
	private Audible audible;
	private VisualEffect effect;
	private Boolean ongoing;
	private Integer audibleDelay;
	private Integer visualFxDelay;
	
	/**
	 * Create a notification with all possible data crammed into it
	 * 
	 * @param title
	 *            the title object
	 * @param priority
	 *            the priority
	 * @param audible
	 *            the audible object
	 * @param effect
	 *            the effect
	 * @param ongoing
	 *            is it ongoing?
	 */
	public Notification(Title title, Priority priority, Audible audible, VisualEffect effect, Boolean ongoing)
	{
		this.title = title;
		this.priority = priority;
		this.audible = audible;
		this.effect = effect;
		this.ongoing = ongoing;
		this.audibleDelay = 0;
		this.visualFxDelay = 0;
	}
	
	/**
	 * Make an empty notification and add what you need
	 */
	public Notification()
	{
		this(null, Priority.NORMAL, null, null, false);
	}
	
	/**
	 * A Basic notification for titles
	 * 
	 * @param title
	 *            the title
	 */
	public Notification(Title title)
	{
		this(title, Priority.NORMAL, null, null, false);
	}
	
	/**
	 * Define an ongoing notification. Ongoing notifications prevent any delay
	 * for the next title.
	 * 
	 * @param title
	 *            the title
	 * @param ongoing
	 *            true/false
	 */
	public Notification(Title title, boolean ongoing)
	{
		this(title, Priority.NORMAL, null, null, ongoing);
	}
	
	/**
	 * Define a title and priority. Higher gets played before any lower priority
	 * notifications. Usually normal is fine
	 * 
	 * @param title
	 *            the title
	 * @param priority
	 *            the priority
	 */
	public Notification(Title title, Priority priority)
	{
		this(title, priority, null, null, false);
	}
	
	/**
	 * Play the notification. However it is recommended you queue it instead.
	 * Its less work on your end, and is much more efficient when using a lot of
	 * notifications for lots of players.
	 * 
	 * Use Phantom.queue(Player p, Notification n);
	 * 
	 * @see Phantom
	 * 
	 * @param p
	 */
	public void play(Player p)
	{
		if(title != null)
		{
			title.send(p);
		}
		
		if(audible != null)
		{
			new TaskLater(audibleDelay)
			{
				public void run()
				{
					audible.play(p);
				}
			};
		}
		
		if(effect != null)
		{
			new TaskLater(visualFxDelay)
			{
				public void run()
				{
					effect.play(p.getLocation());
				}
			};
		}
	}
	
	public Title getTitle()
	{
		return title;
	}
	
	public Notification setTitle(Title title)
	{
		this.title = title;
		
		return this;
	}
	
	public Priority getPriority()
	{
		return priority;
	}
	
	public Notification setPriority(Priority priority)
	{
		this.priority = priority;
		
		return this;
	}
	
	public Audible getAudible()
	{
		return audible;
	}
	
	public Notification setAudible(Audible audible)
	{
		this.audible = audible;
		
		return this;
	}
	
	public VisualEffect getEffect()
	{
		return effect;
	}
	
	public Notification setEffect(VisualEffect effect)
	{
		this.effect = effect;
		
		return this;
	}
	
	public Boolean getOngoing()
	{
		return ongoing;
	}
	
	public Notification setOngoing(Boolean ongoing)
	{
		this.ongoing = ongoing;
		
		return this;
	}
	
	public Integer getAudibleDelay()
	{
		return audibleDelay;
	}
	
	public Notification setAudibleDelay(Integer audibleDelay)
	{
		this.audibleDelay = audibleDelay;
		
		return this;
	}
	
	public Integer getVisualFxDelay()
	{
		return visualFxDelay;
	}
	
	public Notification setVisualFxDelay(Integer visualFxDelay)
	{
		this.visualFxDelay = visualFxDelay;
		
		return this;
	}
}
