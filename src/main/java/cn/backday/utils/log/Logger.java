package cn.backday.utils.log;

import org.apache.logging.log4j.LogManager;

public class Logger {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    public static void dbg(String s) {
        LOGGER.info("{}", s);
    }

    public static void warn(String s) {
        LOGGER.warn("{}", s);
    }

    public static void warn(String s, Throwable t) {
        LOGGER.warn("{}", s, t);
    }

    public static void error(String s) {
        LOGGER.error("{}", s);
    }

    public static void error(String s, Throwable t) {
        LOGGER.error("{}", s, t);
    }

    public static void log(String s) {
        dbg(s);
    }
}
