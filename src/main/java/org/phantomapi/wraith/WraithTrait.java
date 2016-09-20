package org.phantomapi.wraith;

import org.bukkit.entity.LivingEntity;
import net.citizensnpcs.api.ai.speech.SpeechContext;

/**
 * Represents a trait
 * 
 * @author cyberpwn
 */
public abstract class WraithTrait extends WrapperTrait
{
	public WraithTrait(String name)
	{
		super(name);
	}
	
	@Override
	public abstract void run();
	
	@Override
	public abstract void onAttach();
	
	@Override
	public abstract void onDespawn();
	
	@Override
	public abstract void onSpawn();
	
	@Override
	public abstract void onRemove();
	
	public void message(String message)
	{
		getNPC().getDefaultSpeechController().speak(new SpeechContext(message));
	}
	
	public void message(String message, LivingEntity entity)
	{
		getNPC().getDefaultSpeechController().speak(new SpeechContext(message, entity));
	}
}
