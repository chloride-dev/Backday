package cn.backday.utils.log;

import org.apache.logging.log4j.LogManager;

public class Logger {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    public static void dbg(String s) {
        LOGGER.info("[Backday] {}", s);
    }

    public static void warn(String s) {
        LOGGER.warn("[Backday] {}", s);
    }

    public static void warn(String s, Throwable t) {
        LOGGER.warn("[Backday] {}", s, t);
    }

    public static void error(String s) {
        LOGGER.error("[Backday] {}", s);
    }

    public static void error(String s, Throwable t) {
        LOGGER.error("[Backday] {}", s, t);
    }

    public static void log(String s) {
        dbg(s);
    }
}
