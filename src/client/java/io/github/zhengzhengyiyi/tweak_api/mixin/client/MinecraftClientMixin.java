package io.github.zhengzhengyiyi.tweak_api.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import org.jetbrains.annotations.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.zhengzhengyiyi.tweak_api.api.event.ScreenOpenCallback;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(at = @At("HEAD"), method = "run")
	private void init(CallbackInfo info) {
		// This code is injected into the start of MinecraftClient.run()V
	}
	
	@Inject(at = @At("HEAD"), method = "setScreen", remap = false)
	private void setScreen(@Nullable Screen screen, CallbackInfo ci) {
		if (screen == null) return;

        ScreenOpenCallback.EVENT
            .invoker()
            .open(screen);
	}
}
