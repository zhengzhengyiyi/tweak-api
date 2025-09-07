package io.github.zhengzhengyiyi.tweak_api;

//import io.github.zhengzhengyiyi.tweak_api.example.Example;
import net.fabricmc.api.ClientModInitializer;

import org.jetbrains.annotations.Nullable;

/**
 * Client initialization entry point.
 */
public class Tweak_apiClient implements ClientModInitializer {
	/**
	 * private value, can be change using setter and get with getter. enable debug mode
	 */
	private static boolean _debug = false;
	
	@Override
	public void onInitializeClient() {
//		Example.create();
	}
	
	/**
	 * Sets the debug mode if the provided value is not null, and returns the current debug state.
	 *
	 * @param v the new debug state to set, or {@code null} to leave the current state unchanged
	 * @return the current debug state ({@code true} for enabled, {@code false} for disabled)
	 */
	public static boolean debug(@Nullable Boolean v) {
		if (v) {
			_debug = v;
		}
		
		return _debug;
	}
}
