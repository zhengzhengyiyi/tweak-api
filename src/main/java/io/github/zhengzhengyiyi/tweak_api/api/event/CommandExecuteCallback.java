package io.github.zhengzhengyiyi.tweak_api.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public class CommandExecuteCallback {
    public static final Event<Callback> EVENT = EventFactory.createArrayBacked(
        Callback.class,
        listeners -> (command) -> {
            for (Callback listener : listeners) {
                ActionResult result = listener.onCommandExecute(command);
                if (result != ActionResult.PASS) {
                    return result;
                }
            }
            return ActionResult.PASS;
        }
    );
    
    @FunctionalInterface
    public interface Callback {
    	/**
    	 * the command callback, you can cancel the command and get it before command execute.
    	 * @param command the comment that is running
    	 * @return a ActionResult which can cancel the command execution
    	 */
        ActionResult onCommandExecute(String command);
    }
}
