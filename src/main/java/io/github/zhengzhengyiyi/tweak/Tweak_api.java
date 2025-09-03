package io.github.zhengzhengyiyi.tweak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class Tweak_api implements ModInitializer {
	public static final String MOD_ID = "tweak";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final EventManager MANAGER = new EventManager();

	@Override
	public void onInitialize() {

	}
}
