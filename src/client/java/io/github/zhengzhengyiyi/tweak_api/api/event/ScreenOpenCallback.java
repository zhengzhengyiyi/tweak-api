package io.github.zhengzhengyiyi.tweak_api.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screen.Screen;

/**
 * The callback before any screen open.
 * @see net.minecraft.util.ActionResult
 */
public class ScreenOpenCallback {
    public static Event<OnScreenOpen> EVENT = EventFactory.createArrayBacked(
    	OnScreenOpen.class,
        listeners -> (a) -> {
            for (OnScreenOpen listener : listeners) {
                listener.open(a);
            }
        }
    );
    
    @FunctionalInterface
    public interface OnScreenOpen {
	    /**
	     * callback before any screen open.
	     * @param screen The screen is going to open
	     */
	    void open(Screen screen);
    }
}
