package io.github.zhengzhengyiyi.tweak.mixin.client;

import io.github.zhengzhengyiyi.tweak.ModEventManager;
import io.github.zhengzhengyiyi.tweak.api.event.GameOptionsChangedCallback;
import net.minecraft.client.option.GameOptions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Inject(
        method = "write(Ljava/io/File;)V",
        at = @At("RETURN")
    )
    private void onWriteOptions(File optionsFile, CallbackInfo ci) {
        GameOptionsChangedCallback.EVENT
            .invoker()
            .onChanged(ModEventManager.old_gameoptions, (GameOptions) (Object) this);
        ModEventManager.old_gameoptions = (GameOptions) (Object) this;
    }
}
