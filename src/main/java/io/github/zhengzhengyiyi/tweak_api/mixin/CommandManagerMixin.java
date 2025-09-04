package io.github.zhengzhengyiyi.tweak_api.mixin;

import com.mojang.brigadier.ParseResults;
import io.github.zhengzhengyiyi.tweak_api.api.event.CommandExecuteCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ActionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandManagerMixin {

    @Inject(
        method = "execute(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;)I",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onExecute(ParseResults<ServerCommandSource> commandContext, String commandString, CallbackInfo ci) {
        ActionResult result = CommandExecuteCallback.EVENT
            .invoker()
            .onCommandExecute(commandString);

        if (result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
