package io.github.zhengzhengyiyi.tweak_api.debugger;

import io.github.zhengzhengyiyi.tweak_api.api.Debugger;
import io.github.zhengzhengyiyi.tweak_api.api.util.DetailedLogger;

import net.minecraft.server.MinecraftServer;

public class ServerDebuggerPrinter implements Debugger {
//	private static Logger LOGGER = LoggerFactory.getLogger(ServerDebuggerPrinter.class);
	private final DetailedLogger detailedLogger = new DetailedLogger("tweak");
	private int tickcount = 0;
	
	@Override
	public void tick(MinecraftServer server) {
//		LOGGER.info(server.getCurrentPlayerCount() + "");
		if (server != null && tickcount % 300 == 0) detailedLogger.logServerDetails(server);
		tickcount += 1;
	}
	
	public ServerDebuggerPrinter() {
		register();
	}
}
