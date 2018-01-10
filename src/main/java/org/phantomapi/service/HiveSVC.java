package org.phantomapi.service;

import phantom.pawn.Name;
import phantom.pawn.Register;
import phantom.pawn.Singular;
import phantom.pawn.Start;
import phantom.pawn.Stop;
import phantom.service.IService;
import phantom.util.metrics.Documented;

/**
 * Hive service for storing data on blocks, entities, chunks, and worlds
 *
 * @author cyberpwn
 */
@Documented
@Register
@Name("SVC Hive")
@Singular
public class HiveSVC implements IService
{
	@Start
	public void start()
	{

	}

	@Stop
	public void stop()
	{

	}
}
