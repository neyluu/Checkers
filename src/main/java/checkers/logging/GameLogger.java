package checkers.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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
        if(message.contains("{}"))
        {
            String result = message.replaceFirst("\\{}", arg.toString());
            logger.info(result);
        }
        else
        {
            logger.info(message);
        }
    }

    public void log(String message, Object... args)
    {

    }

    public void log(String message, Object arg1, Object arg2)
    {
        if(message.contains("{}"))
        {
            int bracketsCount = 0;
            List<Integer> bracketsIndexes = new ArrayList<>();

            for(int i = 1; i < message.length(); i++)
            {
                if(message.charAt(i - 1) == '{' && message.charAt(i) == '}')
                {
                    bracketsCount++;
                    bracketsIndexes.add(i - 1);
                }
            }

            System.out.println("brackets: " + bracketsCount);
            System.out.println("indexes: " + bracketsIndexes);

            if(bracketsCount == 1)
            {
                log(message, arg1);
            }
            else
            {
                Integer firstBracketIndex = bracketsIndexes.get(0);
                Integer secondBracketIndex = bracketsIndexes.get(1);
                String part1 = message.substring(0, firstBracketIndex);
                String part2 = arg1.toString();
                String part3 = message.substring(firstBracketIndex + 2, secondBracketIndex);
                String part4 = arg2.toString();
                String part5 = message.substring(secondBracketIndex + 2);

                logger.info("{}{}{}{}{}", part1, part2, part3, part4, part5);
            }
        }
        else
        {
            logger.info(message);
        }
    }

    public void log(String message, Throwable arg)
    {

    }
}
