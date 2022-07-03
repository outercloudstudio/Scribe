package net.fabricmc.scribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scribe {
	public static final Logger LOGGER = LoggerFactory.getLogger("scribe");

	public void Debug() {
		LOGGER.info("Hello Fabric world!");
		LOGGER.info("Hello Fabric world! 2");
	}
}
