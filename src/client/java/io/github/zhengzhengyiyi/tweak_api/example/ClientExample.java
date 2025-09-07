package io.github.zhengzhengyiyi.tweak_api.example;

import io.github.zhengzhengyiyi.tweak_api.api.event.GameOptionsChangedCallback;
import io.github.zhengzhengyiyi.tweak_api.api.event.ScreenOpenCallback;

/**
 * A example callback using class.
 * The class include all the callbacks in this api mod and usage.
 * This class do not include server callbacks
 * 
 * call this class for example
 */
public class ClientExample {
	/**
	 * The constructor, can just call by constructor.
	 * 
	 * example usage {@code
	 * 		new io.github.zhengzhengyiyi.example.ClientExample();
	 * }
	 * or 
	 * {@code
	 * 		io.github.zhengzhengyiyi.example.ClientExample.create();
	 * }
	 */
	public ClientExample() {
		GameOptionsChangedCallback.EVENT.register((a, b) -> {
			System.out.println(a);
			System.out.println(b);
		});
		ScreenOpenCallback.EVENT.register((screen) -> {
			System.out.println("opening the " + screen.toString());
		});
	}
	
	/**
	 * can be call in a static method
	 * @return a new {@link ClientExample()}
	 */
	public static ClientExample create() {
		return new ClientExample();
	}
}
