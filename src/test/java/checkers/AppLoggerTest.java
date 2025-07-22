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
    private final AppLogger l = new AppLogger(AppLoggerTest.class);

    @Test
    public void info_noArgs()
    {
        testOutput(() -> l.info("abc"), "INFO: abc");
    }

    private void testOutput(Runnable function, String expectedOutput)
    {
        function.run();

        try
        {
            File file = new File("checkersLog.txt");
            Scanner scanner = new Scanner(file);

            String output = scanner.nextLine();
            scanner.close();

            System.out.println(output);

            int startIndex = output.indexOf("] ");
            int endIndex = output.lastIndexOf("   (AppLoggerTest");

            if(startIndex == -1 || endIndex == -1) fail("Log message doesn't follow syntax!");
            String outputMessage = output.substring(startIndex + 2, endIndex);

            assertEquals(expectedOutput, outputMessage);
        }
        catch (FileNotFoundException e)
        {
            fail("File error!");
        }
    }

}
