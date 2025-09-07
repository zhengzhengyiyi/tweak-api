package io.github.zhengzhengyiyi.tweak_api.debug;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.zhengzhengyiyi.tweak_api.Tweak_apiClient;

/**
 * The debug printer.
 * Used to print debug information during runtime, such as key presses.
 * This class is intended for internal use only.
 * Do not call register() manually — it is managed by the debug system.
 */
public class DebugPrinter {
	/**
	 * The logger for DebugPrinter.
	 */
	private static Logger LOGGER = LoggerFactory.getLogger(DebugPrinter.class);

    /**
     * Initializes the debug printer.
     *
     * Do not call this method directly. Debug state is controlled through Tweak_apiClient.
     * This method may be used in the future to register debug hooks.
     */
    public static void register() {
    	
    }

    private static int frameCounter = 0;
    private static final java.util.Set<String> pressedKeysThisInterval = new java.util.HashSet<>();

    /**
     * Called every tick to check and print currently pressed keys.
     *
     * This method logs all keys currently being pressed on the keyboard,
     * regardless of whether they are bound to a game action or not.
     * It captures raw keyboard input using GLFW.
     * Only prints if debug mode is enabled.
     * Prints every 20 ticks, showing all keys pressed during that period.
     */
    public void tick() {
        if (!Tweak_apiClient.debug(null)) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getWindow() == null) return;

        for (int keyCode = 32; keyCode < 350; keyCode++) {
            if (GLFW.glfwGetKey(client.getWindow().getHandle(), keyCode) == GLFW.GLFW_PRESS) {
                String keyName = GLFW.glfwGetKeyName(keyCode, -1);
                if (keyName != null) {
                    pressedKeysThisInterval.add(keyName);
                } else {
                    pressedKeysThisInterval.add("key_" + keyCode);
                }
            }
        }

        frameCounter++;

        if (frameCounter >= 20) {
            if (!pressedKeysThisInterval.isEmpty()) {
                LOGGER.info("Keys pressed in last 20 ticks: {}", String.join(", ", pressedKeysThisInterval));
            } else {
                LOGGER.info("No keys were pressed in last 20 ticks.");
            }
            pressedKeysThisInterval.clear();
            frameCounter = 0;
        }
    }
    
    /**
     * the id
     */
    public Identifier getId() {
    	return Identifier.of("zhengzhengyiyi", "printer");
    }
}
