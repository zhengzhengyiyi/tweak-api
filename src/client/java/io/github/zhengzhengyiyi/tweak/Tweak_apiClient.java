package io.github.zhengzhengyiyi.tweak;

import net.fabricmc.api.ClientModInitializer;

import io.github.zhengzhengyiyi.tweak.api.event.GameOptionsChangedCallback;

public class Tweak_apiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		GameOptionsChangedCallback.EVENT.register((a, b) -> {
			System.out.println("aaa");
		});
	}
}
