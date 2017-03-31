package org.phantomapi.library;

import java.io.File;

public class WraithAPI
{
	public static String GLOBAL_VERSION = "${project.version}";
	public static final String REPO_CENTRAL = "https://repo1.maven.org/maven2/";
	public static final String REPO_VOLMIT = "http://get.volmit.com/";
	public static final File REPO_LOCAL = new File(new File("wraith"), "repository");
}
