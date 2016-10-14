package org.phantomapi.construct;

/**
 * Represents a controller messenger
 * 
 * @author cyberpwn
 */
public interface ControllerMessenger
{
	/**
	 * Fired when a controller message is received
	 * 
	 * @param the
	 *            controller message
	 * @return the response controller message (return the message without
	 *         editing for no response)
	 */
	public ControllerMessage onControllerMessageRecieved(ControllerMessage message);
	
	/**
	 * Send a controller message
	 * 
	 * @param controller
	 *            the target controller
	 * @param message
	 *            the message
	 * @return the message response
	 */
	public ControllerMessage sendMessage(Controllable controller, ControllerMessage message);
	
	/**
	 * Send a controller message
	 * 
	 * @param controller
	 *            the target controller
	 * @param message
	 *            the message
	 * @return the message response
	 */
	public ControllerMessage sendMessage(String controller, ControllerMessage message);
}
