package org.cyberpwn.phantom.lang;

import org.bukkit.entity.Player;
import org.cyberpwn.phantom.nms.NMSX;

/**
 * Its a title object for title messages to players
 * 
 * @author cyberpwn
 *
 */
public class Title
{
	private String title;
	private String subTitle;
	private String action;
	private Integer fadeIn;
	private Integer fadeOut;
	private Integer stayTime;
	
	/**
	 * Make it empty
	 */
	public Title()
	{
		this.fadeIn = 0;
		this.fadeOut = 0;
		this.stayTime = 5;
	}
	
	/**
	 * Make it full
	 * 
	 * @param title
	 *            the title
	 * @param subTitle
	 *            the subtitle
	 * @param action
	 *            the action bar text
	 * @param fadeIn
	 *            the fade in
	 * @param fadeOut
	 *            the fade out
	 * @param stayTime
	 *            the stay time
	 */
	public Title(String title, String subTitle, String action, Integer fadeIn, Integer fadeOut, Integer stayTime)
	{
		this.title = title;
		this.subTitle = subTitle;
		this.action = action;
		this.fadeIn = fadeIn;
		this.fadeOut = fadeOut;
		this.stayTime = stayTime;
	}
	
	/**
	 * Make a title
	 * 
	 * @param title
	 *            the title
	 * @param subTitle
	 *            the subtitle
	 * @param fadeIn
	 *            the fade in
	 * @param fadeOut
	 *            the fade out
	 * @param stayTime
	 *            the stay time
	 */
	public Title(String title, String subTitle, Integer fadeIn, Integer fadeOut, Integer stayTime)
	{
		this.title = title;
		this.subTitle = subTitle;
		this.fadeIn = fadeIn;
		this.fadeOut = fadeOut;
		this.stayTime = stayTime;
	}
	
	/**
	 * Title
	 * 
	 * @param action
	 *            action bar text
	 * @param fadeIn
	 *            fade in
	 * @param fadeOut
	 *            fade out
	 * @param stayTime
	 *            stay time
	 */
	public Title(String action, Integer fadeIn, Integer fadeOut, Integer stayTime)
	{
		this.action = action;
		this.fadeIn = fadeIn;
		this.fadeOut = fadeOut;
		this.stayTime = stayTime;
	}
	
	/**
	 * Send the title
	 * 
	 * @param p
	 *            the player
	 */
	public void send(Player p)
	{
		try
		{
			NMSX.sendTitle(p, fadeIn, stayTime, fadeOut, title, subTitle);
			NMSX.sendActionBar(p, action);
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Title setTitle(String title)
	{
		this.title = title;
		
		return this;
	}
	
	public String getSubTitle()
	{
		return subTitle;
	}
	
	public Title setSubTitle(String subTitle)
	{
		this.subTitle = subTitle;
		
		return this;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public Title setAction(String action)
	{
		this.action = action;
		
		return this;
	}
	
	public Integer getFadeIn()
	{
		return fadeIn;
	}
	
	public void setFadeIn(Integer fadeIn)
	{
		this.fadeIn = fadeIn;
	}
	
	public Integer getFadeOut()
	{
		return fadeOut;
	}
	
	public void setFadeOut(Integer fadeOut)
	{
		this.fadeOut = fadeOut;
	}
	
	public Integer getStayTime()
	{
		return stayTime;
	}
	
	public void setStayTime(Integer stayTime)
	{
		this.stayTime = stayTime;
	}

	public Integer totalTime()
	{
		return fadeIn + fadeOut + stayTime;
	}
}
