package checkers.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public class GameLogger
{
    private Logger logger;

    public GameLogger(Class<?> clazz)
    {
        String loggerName = "GameLogicLogger" + clazz.getName();

        this.logger = LoggerFactory.getLogger(loggerName);
        java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(loggerName);
        julLogger.setUseParentHandlers(false);

        for (Handler handler : julLogger.getHandlers())
        {
            julLogger.removeHandler(handler);
        }

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new GameLoggerFormatter());
        handler.setLevel(Level.ALL);
        julLogger.setLevel(Level.ALL);
        julLogger.addHandler(handler);
    }

    public void log(String message)
    {
        logger.info(message);
    }

    public void log(String message, Object arg)
    {
        logger.info(message, arg);
    }

    public void log(String message, Object... args)
    {
        logger.info(message, args);
    }

    public void log(String message, Object arg1, Object arg2)
    {
        logger.info(message, arg1, arg2);
    }

    public void log(String message, Throwable arg)
    {
        logger.info(message, arg);
    }
}
