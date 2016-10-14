package org.phantomapi.registry;

public class GlobalRegistry
{
	private final ArtifactRegistry artifactRegistry;
	private final AudioEffectRegistry audioEffectRegistry;
	private final VisualEffectRegistry visualEffectRegistry;
	private final SchematicRegistry schematicRegistry;
	
	public GlobalRegistry()
	{
		this.artifactRegistry = new ArtifactRegistry();
		this.audioEffectRegistry = new AudioEffectRegistry();
		this.visualEffectRegistry = new VisualEffectRegistry();
		this.schematicRegistry = new SchematicRegistry();
	}

	public AudioEffectRegistry audio()
	{
		return audioEffectRegistry;
	}

	public VisualEffectRegistry visual()
	{
		return visualEffectRegistry;
	}

	public SchematicRegistry schematic()
	{
		return schematicRegistry;
	}
	
	public ArtifactRegistry artifact()
	{
		return artifactRegistry;
	}
}
