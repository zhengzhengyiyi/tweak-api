package io.github.zhengzhengyiyi.tweak_api.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;

/**
 * A callback that is invoked when an entity is about to take damage.
 * This event is fired on the server side before the damage is applied.
 * 
 * You can use this event to:
 * <ul>
 *     <li>Listen to the amount of damage taken</li>
 *     <li>Completely cancel the damage (e.g., grant temporary invincibility).</li>
 *     <li>Listen for specific damage types without changing anything.</li>
 * </ul>
 */
public class EntityHurtCallback {
    /**
     * The event instance. Use this to register your listeners.
     * 
     * 
     */
    public static Event<Callback> EVENT = EventFactory.createArrayBacked(
    	Callback.class,
        listeners -> (entity, source, amount) -> {
            for (Callback listener : listeners) {
                listener.onHurt(entity, source, amount);
            }
        }
    );
    
    @FunctionalInterface
    public interface Callback {
	    /**
	     * call before the entity take damage
	     * 
	     * @param entity The entity that is going to take damage
	     * @param source The source that the damage come from, for example player attack, lava
	     * @param amount How much damage will be take to that entity.
	     */
	    void onHurt(Entity entity, DamageSource source, float amount);
    }
}
