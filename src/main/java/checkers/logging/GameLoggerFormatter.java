package checkers.logging;

import java.util.logging.LogRecord;

public class GameLoggerFormatter extends CustomFormatter
{
    @Override
    public String format(LogRecord record)
    {
        createLoggingData(record);
        String type = "GAME";

        return String.format("[%s] %s: %s   (%s.%s:%d)[%d]%n", timestamp, type, message, shortClassName, methodName, lineNumber, thread);
    }
}
