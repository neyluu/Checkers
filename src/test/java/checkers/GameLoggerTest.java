package checkers;

import checkers.logging.GameLogger;
import checkers.logging.GameLoggerFormatter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class GameLoggerTest
{
    private final GameLogger logger = new GameLogger(GameLoggerTest.class);

    @Test
    public void noArgs_normalInput()
    {
        testConsoleOutput(() -> logger.log("abc"), "abc");
    }

    @Test
    public void noArgs_emptyInput()
    {
        testConsoleOutput(() -> logger.log(""), "");
    }

    @Test
    public void noArgs_haveArgumentPlaceholder()
    {
        testConsoleOutput(() -> logger.log("abc {}"), "abc {}");
    }


    /**
     * Function checks if message written to console by GameLogger is the same as expected output
     * Only message is checked, without additional logging info
     * Current format: [dd-mm-yyyy hh:mm:ss] GAME: [message]   (class.method:line)[thread]
     * In case of changing format this function may not work properly
     *
     * @param function       GameLogger.log() call
     * @param expectedOutput message that should be written in console
     **/
    private void testConsoleOutput(Runnable function, String expectedOutput)
    {

        Logger julLogger = Logger.getLogger("GameLogicLogger" + GameLoggerTest.class.getName());

        List<Handler> originalHandlers = new ArrayList<>();
        for(Handler handler : julLogger.getHandlers())
        {
            originalHandlers.add(handler);
            julLogger.removeHandler(handler);
        }
        Level originalLevel = julLogger.getLevel();

        CapturingHandler capturingHandler = new CapturingHandler();
        capturingHandler.setLevel(Level.ALL);

        julLogger.addHandler(capturingHandler);
        julLogger.setLevel(Level.ALL);
        julLogger.setUseParentHandlers(false);

        try
        {
            function.run();
        }
        finally
        {
            julLogger.removeHandler(capturingHandler);
            for (Handler handler : originalHandlers)
            {
                julLogger.addHandler(handler);
            }
            julLogger.setLevel(originalLevel);
            julLogger.setUseParentHandlers(true);
        }

        String output = capturingHandler.getCapturedMessage();

        int startIndex = output.indexOf("GAME: ");
        int endIndex = output.lastIndexOf("   (");

        if(startIndex == -1 || endIndex == -1) fail("Log message doesn't follow syntax!");
        String outputMessage = output.substring(startIndex + 6, endIndex);

        assertEquals(expectedOutput, outputMessage);
    }

    private static class CapturingHandler extends Handler
    {
        private String capturedMessage;

        public CapturingHandler()
        {
            setFormatter(new GameLoggerFormatter());
        }

        @Override
        public void publish(LogRecord record)
        {
            if(isLoggable(record)) capturedMessage = getFormatter().format(record);
        }

        public String getCapturedMessage()
        {
            return capturedMessage;
        }

        @Override
        public void flush() { }

        @Override
        public void close() throws SecurityException { }
    }
}
