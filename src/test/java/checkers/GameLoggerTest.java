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


    @Test
    public void oneArg_withoutPlaceholder()
    {
        testConsoleOutput(() -> logger.log("abc", 2), "abc");
    }

    @Test
    public void oneArg_OnlyPlaceholder()
    {
        testConsoleOutput(() -> logger.log("{}", 1), "1");
    }

    @Test
    public void oneArg_onePlaceholderUsed_intParameter()
    {
        testConsoleOutput(() -> logger.log("abc {}", 1), "abc 1");
    }

    @Test
    public void oneArg_onePlaceholderUsed_stringParameter()
    {
        testConsoleOutput(() -> logger.log("abc {}", "aaa"), "abc aaa");
    }

    @Test
    public void oneArg_twoPlaceholders_oneUsed_intParameter()
    {
        testConsoleOutput(() -> logger.log("abc {} {}", 3), "abc 3 {}");
    }

    @Test
    public void oneArg_twoPlaceholders_oneUsed_stringParameter()
    {
        testConsoleOutput(() -> logger.log("abc {} {}", "bbb"), "abc bbb {}");
    }

    @Test
    public void oneArg_placeholderInsideArgument()
    {
        testConsoleOutput(() -> logger.log("abc {} abc", "abc {} abc"), "abc abc {} abc abc");
    }

    @Test
    public void oneArg_escapePlaceholder()
    {
        testConsoleOutput(() -> logger.log("abc \\{} abc", 1), "abc {} abc");
    }

    @Test
    public void oneArg_onlyEscapePlaceholder()
    {
        testConsoleOutput(() -> logger.log("\\{}", 1), "{}");
    }

    @Test
    public void oneArg_escapePlaceholderAndNormal()
    {
        testConsoleOutput(() -> logger.log("aaa \\{} aa {}", 1), "aaa {} aa 1");
    }


    @Test
    public void twoArgs_withoutPlaceholder()
    {
        testConsoleOutput(() -> logger.log("abc", 1, 2), "abc");
    }

    @Test
    public void twoArgs_onlyPlaceholder()
    {
        testConsoleOutput(() -> logger.log("{}", 1, 2), "1");
    }

    @Test
    public void twoArgs_onePlaceholder_intParameter()
    {
        testConsoleOutput(() -> logger.log("abc {} abc", 1, 2), "abc 1 abc");
    }

    @Test
    public void twoArgs_onePlaceholder_stringParameter()
    {
        testConsoleOutput(() -> logger.log("abc {} abc", "aaa", 2), "abc aaa abc");
    }

    @Test
    public void twoArgs_twoPlaceholders_intParameters()
    {
        testConsoleOutput(() -> logger.log("abc {} {} abc", 1, 2), "abc 1 2 abc");
    }

    @Test
    public void twoArgs_twoPlaceholders_stringParameters()
    {
        testConsoleOutput(() -> logger.log("abc {} {} abc", "dd", "gg"), "abc dd gg abc");

    }

    @Test
    public void twoArgs_threePlaceholders_oneUnused()
    {
        testConsoleOutput(() -> logger.log("abc {} {} {} abc", 1, 2), "abc 1 2 {} abc");
    }

    @Test
    public void twoArgs_placeholderInsideArgument()
    {
        testConsoleOutput(() -> logger.log("a {} {} a", "a {} a", "b {c} b"), "a a {} a b {c} b a");
    }

    @Test
    public void twoArgs_escapePlaceholder()
    {
        testConsoleOutput(() -> logger.log("a \\{} {} {} a", 1, 2), "a {} 1 2 a");
    }


    @Test
    public void moreArgs_withoutPlaceholder()
    {
        testConsoleOutput(() -> logger.log("abc", 1, 2, 3), "abc");
    }

    @Test
    public void moreArgs_onlyPlaceholder()
    {
        testConsoleOutput(() -> logger.log("{}", 1, 2, 3), "1");
    }

    @Test
    public void moreArgs_onePlaceholder()
    {
        testConsoleOutput(() -> logger.log("abc {} abc", 1, 2, 3), "abc 1 abc");
    }

    @Test
    public void moreArgs_twoPlaceholders()
    {
        testConsoleOutput(() -> logger.log("abc {} {} abc", 1, 2, 3), "abc 1 2 abc");
    }

    @Test
    public void moreArgs_threePlaceholders()
    {
        testConsoleOutput(() -> logger.log("abc {} {} a {} abc", 1, 2, 3), "abc 1 2 a 3 abc");
    }

    @Test
    public void moreArgs_fourPlaceholders()
    {
        testConsoleOutput(() -> logger.log("abc {} {} abc {} {} abc", 1, 2, 3, 4), "abc 1 2 abc 3 4 abc");
    }

    @Test
    public void moreArgs_fourPlaceholders_threeArguments()
    {
        testConsoleOutput(() -> logger.log("abc {} {} abc {} {} abc", 1, 2, 3), "abc 1 2 abc 3 {} abc");
    }

    @Test
    public void moreArgs_threePlaceholders_fourArguments()
    {
        testConsoleOutput(() -> logger.log("abc {} {} abc {} abc", 1, 2, 3, 4), "abc 1 2 abc 3 abc");
    }

    @Test
    public void moreArgs_placeholderInsideArgument()
    {
        testConsoleOutput(() -> logger.log("a {} {} {} c", "a {} a", "b {c} b", 3), "a a {} a b {c} b 3 c");
    }

    @Test
    public void moreArgs_escapeCharacter()
    {
        testConsoleOutput(() -> logger.log("a {} \\{} {} {}", 1, 2, 3), "a 1 {} 2 3");
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
