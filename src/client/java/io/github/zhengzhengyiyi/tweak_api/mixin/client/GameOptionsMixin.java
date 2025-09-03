package io.github.zhengzhengyiyi.tweak_api.mixin.client;

import io.github.zhengzhengyiyi.tweak_api.ModEventManager;
import io.github.zhengzhengyiyi.tweak_api.api.event.GameOptionsChangedCallback;
import net.minecraft.client.option.GameOptions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Inject(
        method = "write()V",
        at = @At("RETURN")
    )
    private void onWriteOptions(CallbackInfo ci) {
        GameOptionsChangedCallback.EVENT
            .invoker()
            .onChanged(ModEventManager.old_gameoptions, (GameOptions) (Object) this);
        ModEventManager.old_gameoptions = (GameOptions) (Object) this;
    }
}
