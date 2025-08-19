package checkers;

import checkers.logging.AppLogger;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AppLoggerTest
{

    private final static File file = new File("checkersLog.txt");
    private final static Scanner scanner;

    private final AppLogger l = new AppLogger(AppLoggerTest.class);

    private final String errorPrefix = "ERROR: ";
    private final String warnPrefix = "WARNING: ";
    private final String infoPrefix = "INFO: ";
    private final String debugPrefix = "DEBUG: ";
    private final String gamePrefix = "GAME: ";

    static
    {
        try
        {
            scanner = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void emptyMessage()
    {
        testAllLevels("", "");
    }

    @Test
    public void normalString()
    {
        testAllLevels("abc", "abc");
    }

    @Test
    public void placeholderWithoutArgs()
    {
        testAllLevels("{}", "{}");
    }

    @Test
    public void fewPlaceholdersWithoutArgs()
    {
        testAllLevels("{} abc {} abc {}", "{} abc {} abc {}");
    }

    @Test
    public void escapePlaceholder()
    {
        testAllLevels("{}", "\\{}");
    }

    @Test
    public void twoEscapePlaceholders()
    {
        testAllLevels("{} {}", "\\{} \\{}");
    }

    @Test
    public void backslashMessage()
    {
        testAllLevels("Path: C:\\\\Program Files", "Path: C:\\\\Program Files");
    }

    @Test
    public void args_onePlaceholder()
    {
        testAllLevels("1", "{}", 1);
    }

    @Test
    public void args_fewPlaceholders()
    {
        testAllLevels("1 abc bbb abc 2.1", "{} abc {} abc {}", 1, "bbb", 2.1);
    }

    @Test
    public void args_noPlaceholder()
    {
        testAllLevels("abc abc", "abc abc", 1, 2);
    }

    @Test
    public void args_morePlaceholdersThanArgs()
    {
        testAllLevels("2 a c a {}", "{} a {} a {}", 2, "c");
    }

    @Test
    public void args_moreArgsThanPlaceholders()
    {
        testAllLevels("1 a cc a", "{} a {} a", 1, "cc", 2.1);
    }

    @Test
    public void args_escapePlaceholder()
    {
        testAllLevels("{}", "\\{}", 1);
    }

    @Test
    public void args_escapePlaceholderAndNormal()
    {
        testAllLevels("1 {}", "{} \\{}", 1, 2);
    }


    private void testAllLevels(String expectedOutput, String message)
    {
        testOutput(() -> l.error(message), errorPrefix + expectedOutput);
        testOutput(() -> l.warn(message), warnPrefix + expectedOutput);
        testOutput(() -> l.info(message), infoPrefix + expectedOutput);
        testOutput(() -> l.debug(message), debugPrefix + expectedOutput);
        testOutput(() -> l.game(message), gamePrefix + expectedOutput);
    }

    private void testAllLevels(String expectedOutput, String message, Object... args)
    {
        testOutput(() -> l.error(message, args), errorPrefix + expectedOutput);
        testOutput(() -> l.warn(message, args), warnPrefix + expectedOutput);
        testOutput(() -> l.info(message, args), infoPrefix + expectedOutput);
        testOutput(() -> l.debug(message, args), debugPrefix + expectedOutput);
        testOutput(() -> l.game(message, args), gamePrefix + expectedOutput);
    }

    /**
     * Function checks if log print proper message, method should print to both console and file,
     * checking is based on file output.
     * Only message and class name is checked, without additional logging info
     * Current format: [dd-mm-yyyy hh:mm:ss] GAME: [message]   (class.method:line)[thread]
     * In case of changing format this function may not work properly
     *
     * @param function       log method call
     * @param expectedOutput message that should be written by log method
     **/
    private static void testOutput(Runnable function, String expectedOutput)
    {
        function.run();

        String output = scanner.nextLine();

        int startIndex = output.indexOf("] ");
        int endIndex = Settings.logCodeInfo ? output.lastIndexOf("   (AppLoggerTest") : output.length();

        if(startIndex == -1 || endIndex == -1) fail("Log message doesn't follow syntax!");
        String outputMessage = output.substring(startIndex + 2, endIndex);

        assertEquals(expectedOutput, outputMessage);
    }
}
