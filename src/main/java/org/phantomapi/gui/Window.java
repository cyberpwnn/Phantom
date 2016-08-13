package org.phantomapi.gui;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.phantomapi.lang.GList;

/**
 * A Window which holds element objects.
 * 
 * @author cyberpwn
 *
 */
public interface Window
{
	/**
	 * Open the window (builds and opens inventory)
	 * 
	 * @return this
	 */
	public Window open();
	
	/**
	 * Close the window (called when the player closes the window aswell)
	 * 
	 * @return this
	 */
	public Window close();
	
	/**
	 * Build all the elements into an actual inventory object and prepare it for
	 * opening
	 * 
	 * @return this
	 */
	public Window build();
	
	/**
	 * Is the given slot an element?
	 * 
	 * @param slot
	 *            the slot
	 * @return
	 */
	public boolean contains(Slot slot);
	
	/**
	 * Does the window contain the given element?
	 * 
	 * @param element
	 *            the element
	 * @return true if it does
	 */
	public boolean contains(Element element);
	
	/**
	 * Get the element at the desired slot
	 * 
	 * @param slot
	 *            the slot
	 * @return the element. Null if no element.
	 */
	public Element getElement(Slot slot);
	
	/**
	 * Add an element to the window
	 * 
	 * @param element
	 *            an element instance. The element must be unique (not the same
	 *            id) and cannot have a slot already being used in this window.
	 *            Else, nothing will happen
	 * @return this
	 */
	public Window addElement(Element element);
	
	/**
	 * Remove an element from the window
	 * 
	 * @param element
	 *            the element
	 * @return this
	 */
	public Window removeElement(Element element);
	
	/**
	 * Get the title
	 * 
	 * @return the title
	 */
	public String getTitle();
	
	/**
	 * Set the title
	 * 
	 * @param title
	 *            the title
	 * @return this
	 */
	public Window setTitle(String title);
	
	/**
	 * Get the elements
	 * 
	 * @return the elements
	 */
	public GList<Element> getElements();
	
	/**
	 * Set all elements
	 * 
	 * @param elements
	 *            the elements
	 * @return this
	 */
	public Window setElements(GList<Element> elements);
	
	/**
	 * Get the viewer
	 * 
	 * @return the player viewer
	 */
	public Player getViewer();
	
	/**
	 * Get the inventory
	 * 
	 * @return the inventory
	 */
	public Inventory getInventory();
	
	/**
	 * Get the id for this window
	 * 
	 * @return the window id
	 */
	public UUID getId();
	
	/**
	 * Get the background element
	 * 
	 * @return the element
	 */
	public Element getBackground();
	
	/**
	 * Set the background element for this window. Non-element slots will by
	 * default take up "air" elements. You can change it here
	 * 
	 * @param background
	 *            the element
	 * @return this
	 */
	public Window setBackground(Element background);
	
	/**
	 * Get the height viewport (6 is typical)
	 * 
	 * @return the height
	 */
	public Integer getViewport();
	
	/**
	 * Set the height of this window
	 * 
	 * @param viewport
	 *            must be between 1 and 6
	 * @return
	 */
	public Window setViewport(Integer viewport);
	
	/**
	 * Set the inventory
	 * 
	 * @param inventory
	 *            the inventory
	 * @return this
	 */
	public Window setInventory(Inventory inventory);
	
	/**
	 * Fired when an element is clicked
	 * 
	 * @param element
	 *            the clicked element
	 * @param p
	 *            the clicker
	 * @return if true is returned, the element event will be called. If false,
	 *         the element event wont be registered, effectivley cancelling it
	 */
	public boolean onClick(Element element, Player p);
	
	/**
	 * Is the inventory open?
	 * @return true if it is
	 */
	public boolean isOpen();
}
