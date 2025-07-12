package checkers.logging;

import java.util.logging.LogRecord;

public class LogFormatter extends CustomFormatter
{
    @Override
    public String format(LogRecord record)
    {
        createLoggingData(record);
        String type = record.getLevel().getName();
        if(type.equals("SEVERE")) type = "ERROR";

        return String.format("[%s] %s: %s   (%s.%s:%d)[%d]%n", timestamp, type, message, shortClassName, methodName, lineNumber, thread);
    }
}
