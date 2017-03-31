package org.phantomapi.library;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryInstaller
{
	private List<Library> libraries;
	private File repository;
	private boolean log;
	
	public LibraryInstaller(File repository)
	{
		this(repository, false);
	}
	
	public LibraryInstaller(File repository, boolean log)
	{
		libraries = new ArrayList<Library>();
		this.repository = repository;
		this.log = log;
	}
	
	public LibraryInstaller add(Coordinate coord)
	{
		libraries.add(new WraithLibrary(coord));
		
		return this;
	}
	
	public List<Library> getLibraries()
	{
		return libraries;
	}
	
	public File getRepository()
	{
		return repository;
	}
	
	public void install()
	{
		if(log)
		{
			System.out.println("[======================== Installing packages ========================]");
		}
		
		LibraryManager.installBlocking(this);
		
		if(log)
		{
			System.out.println("[======================== =================== ========================]");
		}
	}
	
	public void install(Runnable onCompleted)
	{
		LibraryManager.installNonBlocking(this, onCompleted);
	}
}
