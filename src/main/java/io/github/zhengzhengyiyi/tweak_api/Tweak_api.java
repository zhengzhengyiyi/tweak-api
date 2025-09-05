package io.github.zhengzhengyiyi.tweak_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
//import net.minecraft.util.ActionResult;

/**
 * The server entry point
 */
public class Tweak_api implements ModInitializer {
	public static final String MOD_ID = "tweak";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final EventManager MANAGER = new EventManager();
	
	/**
	 * The {@code onInitialize} is empty, do not need to do any thing.
	 */
	@Override
	public void onInitialize() {

	}
}
