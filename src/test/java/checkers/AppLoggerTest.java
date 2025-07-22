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
    private final String infoPrefix = "INFO: ";

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
    public void info_empty()
    {
        testOutput(() -> l.info(""), infoPrefix + "");
    }

    @Test
    public void info_normalString()
    {
        testOutput(() -> l.info("abc"), infoPrefix + "abc");
    }

    @Test
    public void info_placeholderWithoutArgs()
    {
        testOutput(() -> l.info("{}"), infoPrefix + "{}");
    }

    @Test
    public void info_fewPlaceholdersWithoutArgs()
    {
        testOutput(() -> l.info("{} abc {} abc {}"), infoPrefix + "{} abc {} abc {}");
    }

    @Test
    public void info_escapePlaceholder()
    {
        testOutput(() -> l.info("\\{}"), infoPrefix + "{}");
    }

    @Test
    public void info_twoEscapePlaceholders()
    {
        testOutput(() -> l.info("\\{} \\{}"), infoPrefix + "{} {}");
    }

    @Test
    public void info_args_onePlaceholder()
    {
        testOutput(() -> l.info("{}", 1), infoPrefix + "1");
    }

    @Test
    public void info_args_fewPlaceholders()
    {
        testOutput(() -> l.info("{} abc {} abc {}", 1, "bbb", 2.1), infoPrefix + "1 abc bbb abc 2.1");
    }

    @Test
    public void info_args_noPlaceholder()
    {
        testOutput(() -> l.info("abc abc", 1, 2), infoPrefix + "abc abc");
    }

    @Test
    public void info_args_morePlaceholdersThanArgs()
    {
        testOutput(() -> l.info("{} a {} a {}", 2, "c"), infoPrefix + "2 a c a {}");
    }

    @Test
    public void info_args_moreArgsThanPlaceholders()
    {
        testOutput(() -> l.info("{} a {} a", 1, "cc", 2.1), infoPrefix + "1 a cc a");
    }

    @Test
    public void info_args_escapePlaceholder()
    {
        testOutput(() -> l.info("\\{}", 1), infoPrefix + "{}");
    }

    @Test
    public void info_args_escapePlaceholderAndNormal()
    {
        testOutput(() -> l.info("{} \\{}", 1, 2), infoPrefix + "1 {}");
    }


    private static void testOutput(Runnable function, String expectedOutput)
    {
        function.run();

        String output = scanner.nextLine();

        int startIndex = output.indexOf("] ");
        int endIndex = output.lastIndexOf("   (AppLoggerTest");

        if(startIndex == -1 || endIndex == -1) fail("Log message doesn't follow syntax!");
        String outputMessage = output.substring(startIndex + 2, endIndex);

        assertEquals(expectedOutput, outputMessage);
    }
}
