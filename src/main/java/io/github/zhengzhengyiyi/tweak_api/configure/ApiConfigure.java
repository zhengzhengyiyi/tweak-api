package io.github.zhengzhengyiyi.tweak_api.configure;

import io.github.zhengzhengyiyi.tweak_api.api.configure.JsonConfigManager;
import io.github.zhengzhengyiyi.tweak_api.DebugHelper;

/**
 * Internal API configuration for debug settings.
 * This class is for internal use only, not exposed to end users.
 */
public class ApiConfigure implements JsonConfigManager.ConfigSchema {
    /**
     * Enable or disable debug mode for the API.
     * When enabled, additional debug information will be logged.
     */
    public boolean debugMode = false;
    
    /**
     * on load
     */
    @Override
    public void onConfigLoaded() {
    	DebugHelper.debug(debugMode);
    }
    
    /**
     * Called before configuration is saved to validate values.
     */
    @Override
    public void onConfigSaving() {
        onConfigLoaded();
    }
    
    /**
     * Returns a string representation of the configuration.
     */
    @Override
    public String toString() {
        return String.format(
            "ApiConfig{debugMode=%s}",
            debugMode
        );
    }
}
