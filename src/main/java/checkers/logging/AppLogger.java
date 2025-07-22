package checkers.logging;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger
{
    public static class ConsoleHandlerFilter implements Filter
    {
        @Override
        public boolean isLoggable(LogRecord record)
        {
            Level level = record.getLevel();
            return     (level == Level.SEVERE  && AppLogger.errorLogs)
                    || (level == Level.WARNING && AppLogger.warnLogs)
                    || (level == Level.INFO    && AppLogger.infoLogs)
                    || (level == Level.FINE    && AppLogger.debugLogs)
                    || (level == Level.FINER   && AppLogger.gameLogs);
        }
    }

    public static class FileHandlerFilter implements Filter
    {
        @Override
        public boolean isLoggable(LogRecord record)
        {
            Level level = record.getLevel();
            return     (level == Level.SEVERE  && AppLogger.errorFile)
                    || (level == Level.WARNING && AppLogger.warnFile)
                    || (level == Level.INFO    && AppLogger.infoFile)
                    || (level == Level.FINE    && AppLogger.debugFile)
                    || (level == Level.FINER   && AppLogger.gameFile);
        }
    }


    private static FileHandler fileHandler;
    private final static String logFilename = "checkersLog.txt";

    private final Class<?> clazz;
    private final Logger logger;
    private final ConsoleHandler consoleHandler = new ConsoleHandler();

    private final static boolean errorLogs = true;
    private final static boolean warnLogs  = true;
    private final static boolean infoLogs  = true;
    private final static boolean debugLogs = true;
    private final static boolean gameLogs  = true;

    private final static boolean errorFile = true;
    private final static boolean warnFile  = true;
    private final static boolean infoFile  = true;
    private final static boolean debugFile = true;
    private final static boolean gameFile  = true;

    static
    {
        try
        {
            fileHandler = new FileHandler(logFilename);
            fileHandler.setFormatter(new LogFormatter());
            fileHandler.setFilter(new FileHandlerFilter());
        }
        catch(IOException e)
        {
            System.err.println("Failed to create file handler for logger!");
        }
    }


    public AppLogger(Class<?> clazz)
    {
        this.clazz = clazz;
        this.logger = Logger.getLogger(this.clazz.getName());

        removeLoggerHandlers();

        this.consoleHandler.setLevel(Level.ALL);
        this.consoleHandler.setFormatter(new LogFormatter());
        this.consoleHandler.setFilter(new ConsoleHandlerFilter());

        this.logger.setLevel(Level.ALL);
        this.logger.setUseParentHandlers(false);
        this.logger.addHandler(fileHandler);
        this.logger.addHandler(consoleHandler);
    }


    public void error(String message)
    {

    }
    public void error(String message, Object... args)
    {

    }

    public void warn(String message)
    {

    }
    public void warn(String message, Object... args)
    {

    }

    public void debug(String message)
    {

    }
    public void debug(String message, Object... args)
    {

    }

    public void info(String message)
    {
        String finalMessage = parseMessage(message, null);
        String[] metaData = getClassAndMethodData();
        logger.logp(Level.INFO, metaData[0], metaData[1], finalMessage);
    }
    public void info(String message, Object... args)
    {
        String finalMessage = parseMessage(message, args);
        String[] metaData = getClassAndMethodData();
        logger.logp(Level.INFO, metaData[0], metaData[1], finalMessage, args);
    }

    public void game(String message)
    {

    }
    public void game(String message, Object... args)
    {

    }


    private String parseMessage(String message, Object[] args)
    {
        StringBuilder result = new StringBuilder(message.length());
        int argCounter = 0;
        int argsLength = args == null ? 0: args.length;

        for(int i = 0; i < message.length(); i++)
        {
            int prev = i - 1;
            int next = i + 1;
            int next2 = i + 2;

            if( message.charAt(i) == '\\'
                && message.charAt(next) == '{'
                && message.charAt(next2) == '}')
            {
                continue;
            }
            if( (prev >= 0 && message.charAt(prev) != '\\' || i == 0)
                && message.charAt(i) == '{'
                && next < message.length() && message.charAt(next) == '}'
                && argCounter < argsLength)
            {
                result.append(args[argCounter++]);
                i++;
                continue;
            }

            result.append(message.charAt(i));
        }

        return result.toString();
    }

    private void removeLoggerHandlers()
    {
        for(Handler handler : logger.getHandlers())
        {
            logger.removeHandler(handler);
        }
    }

    private String[] getClassAndMethodData()
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String loggerClassName = this.getClass().getName();

        String[] result = new String[2];

        result[0] = clazz.getName();
        result[1] = "unknown";

        for (StackTraceElement element : stackTrace)
        {
            String className = element.getClassName();

            if (!className.equals(loggerClassName) && !className.startsWith("java.lang.Thread"))
            {
                result[0] = className;
                result[1] = element.getMethodName();
                return result;
            }
        }

        return result;
    }
}
