package org.phantomapi.command;

import java.util.Set;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.phantomapi.text.MessageBuilder;

/**
 * Command sender wrapper
 * 
 * @author cyberpwn
 */
public class PhantomSender implements PhantomCommandSender
{
	private CommandSender sender;
	private MessageBuilder builder;
	
	/**
	 * Wrap the sender in a phantom sender
	 * 
	 * @param sender
	 *            the sender
	 */
	public PhantomSender(CommandSender sender)
	{
		this.sender = sender;
		builder = null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin arg0)
	{
		return sender.addAttachment(arg0);
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin arg0, int arg1)
	{
		return sender.addAttachment(arg0, arg1);
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2)
	{
		return sender.addAttachment(arg0, arg1, arg2);
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3)
	{
		return sender.addAttachment(arg0, arg1, arg2, arg3);
	}
	
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions()
	{
		return sender.getEffectivePermissions();
	}
	
	@Override
	public boolean hasPermission(String arg0)
	{
		return sender.hasPermission(arg0);
	}
	
	@Override
	public boolean hasPermission(Permission arg0)
	{
		return sender.hasPermission(arg0);
	}
	
	@Override
	public boolean isPermissionSet(String arg0)
	{
		return sender.isPermissionSet(arg0);
	}
	
	@Override
	public boolean isPermissionSet(Permission arg0)
	{
		return sender.isPermissionSet(arg0);
	}
	
	@Override
	public void recalculatePermissions()
	{
		sender.recalculatePermissions();
	}
	
	@Override
	public void removeAttachment(PermissionAttachment arg0)
	{
		sender.removeAttachment(arg0);
	}
	
	@Override
	public boolean isOp()
	{
		return sender.isOp();
	}
	
	@Override
	public void setOp(boolean arg0)
	{
		sender.setOp(arg0);
	}
	
	@Override
	public String getName()
	{
		return sender.getName();
	}
	
	@Override
	public Server getServer()
	{
		return sender.getServer();
	}
	
	@Override
	public void sendMessage(String arg0)
	{
		if(builder != null)
		{
			builder.message(sender, arg0);
		}
		
		else
		{
			sender.sendMessage(arg0);
		}
	}
	
	@Override
	public void sendMessage(String[] arg0)
	{
		for(String i : arg0)
		{
			sendMessage(i);
		}
	}
	
	@Override
	public boolean isPlayer()
	{
		return (sender instanceof Player);
	}
	
	@Override
	public Player getPlayer()
	{
		return isPlayer() ? ((Player) sender) : null;
	}
	
	@Override
	public boolean isConsole()
	{
		return !isPlayer();
	}
	
	@Override
	public void setMessageBuilder(MessageBuilder messageBuilder)
	{
		builder = messageBuilder;
	}
	
	@Override
	public MessageBuilder getMessageBuilder()
	{
		return builder;
	}
	
	@Override
	public Spigot spigot()
	{
		return sender.spigot();
	}
}
