package io.github.zhengzhengyiyi.tweak_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import io.github.zhengzhengyiyi.tweak_api.api.event.CommandExecuteCallback;
import net.fabricmc.api.ModInitializer;
//import net.minecraft.util.ActionResult;

public class Tweak_api implements ModInitializer {
	public static final String MOD_ID = "tweak";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final EventManager MANAGER = new EventManager();

	@Override
	public void onInitialize() {
//		CommandExecuteCallback.EVENT.register((b) -> {
//			System.out.println(b + " executed");
//			
//			return ActionResult.SUCCESS;
//		});
	}
}
