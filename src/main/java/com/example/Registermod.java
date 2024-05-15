package com.example;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Registermod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("register-mod");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}