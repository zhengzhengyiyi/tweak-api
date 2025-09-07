package io.github.zhengzhengyiyi.tweak_api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import io.github.zhengzhengyiyi.tweak_api.api.configure.JsonConfigManager;

//import io.github.zhengzhengyiyi.tweak_api.api.debuggers.ProfilingDebugger;

import io.github.zhengzhengyiyi.tweak_api.api.util.RegisterHelper;
import io.github.zhengzhengyiyi.tweak_api.configure.ApiConfigure;
import io.github.zhengzhengyiyi.tweak_api.debugger.ServerDebuggerPrinter;
//import net.minecraft.util.ActionResult;

/**
 * The server entry point
 */
public class Tweak_api implements ModInitializer {
	public static final String MOD_ID = "tweak";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final EventManager MANAGER = new EventManager();
	
	/**
	 * The {@code onInitialize} entry point
	 */
	@Override
	public void onInitialize() {
		DebugHelper.debug(true);
		RegisterHelper.debugger(new ServerDebuggerPrinter());
		
		JsonConfigManager<ApiConfigure> configManager = new JsonConfigManager<ApiConfigure>(MOD_ID, ApiConfigure.class);
		
		JsonConfigManager.registerConfigModificationCheck(configManager);
		
		ApiConfigure apiConfig = configManager.loadConfig();

		apiConfig.debugMode = true;
		configManager.saveConfig(apiConfig);
	}
}
