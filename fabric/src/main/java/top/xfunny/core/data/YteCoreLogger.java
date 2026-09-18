package top.xfunny.core.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class YteCoreLogger {
    public static final Logger LOGGER = LogManager.getLogger("YTE_CORE");
    private YteCoreLogger() {}

    public static void error(String msg, Exception e) {
        LOGGER.error(msg, e);
    }

    public static void info(String msg, Object... args) {
        LOGGER.info(msg, args);
    }

    public static void debug(String msg, Object... args) {
        LOGGER.debug(msg, args);
    }
}
