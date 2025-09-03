package io.github.zhengzhengyiyi.tweak_api.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.option.GameOptions;

/**
 * Call back will be call when the game options be changed
 * {@link net.minecraft.client.option.GameOptions}
 */
public class GameOptionsChangedCallback {
    public static Event<OnGameOptionsChange> EVENT = EventFactory.createArrayBacked(
    	OnGameOptionsChange.class,
        listeners -> (a, b) -> {
            for (OnGameOptionsChange listener : listeners) {
                listener.onChanged(a, b);
            }
        }
    );
    
    @FunctionalInterface
    public interface OnGameOptionsChange {
	    /**
	     * call back, when game options changes.
	     * @param options the current GamePotions
	     * @param old the GameOptions before the change.
	     */
	    void onChanged(GameOptions old, GameOptions options);
    }
}
