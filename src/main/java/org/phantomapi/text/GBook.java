package org.phantomapi.text;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.phantomapi.lang.GList;

/**
 * Create books
 * 
 * @author cyberpwn
 */
public class GBook
{
	private GList<GPage> pages;
	private String title;
	private String author;
	
	/**
	 * Make a new book
	 * 
	 * @param title
	 *            the book title
	 * @param author
	 *            the author
	 */
	public GBook(String title, String author)
	{
		this.pages = new GList<GPage>();
		this.title = title;
		this.author = author;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getAuthor()
	{
		return author;
	}
	
	public void setAuthor(String author)
	{
		this.author = author;
	}
	
	public void setPages(GList<GPage> pages)
	{
		this.pages = pages;
	}
	
	public GBook addPage(GPage page)
	{
		pages.add(page);
		
		return this;
	}
	
	public GList<GPage> getPages()
	{
		return pages;
	}
	
	public GList<String> toPages()
	{
		GList<String> pages = new GList<String>();
		
		for(GPage i : this.pages)
		{
			String p = "";
			
			for(String j : i.getElements().keySet())
			{
				p = p + ChatColor.DARK_AQUA + ChatColor.UNDERLINE + "" + ChatColor.BOLD + j + ChatColor.RESET + "\n\n" + i.getElements().get(j) + "\n\n" + ChatColor.RESET;
			}
			
			pages.add(p);
		}
		
		return pages;
	}
	
	/**
	 * Compile the abstracted objects into an actual book
	 * 
	 * @return the ItemStack book
	 */
	public ItemStack toBook()
	{
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) book.getItemMeta();
		
		bookMeta.setTitle(getTitle());
		bookMeta.setAuthor(getAuthor());
		bookMeta.setPages(toPages());
		book.setItemMeta(bookMeta);
		return book;
	}
	
	/**
	 * Read a books text
	 * 
	 * @param is
	 *            the item stack
	 * @return the text
	 */
	public static GList<String> read(ItemStack is)
	{
		GList<String> text = new GList<String>();
		
		if(is.getType().equals(Material.WRITTEN_BOOK))
		{
			BookMeta bookMeta = (BookMeta) is.getItemMeta();
			
			for(String i : bookMeta.getPages())
			{
				text.add(i);
			}
		}
		
		return text;
	}
}
