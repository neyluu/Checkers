package checkers.logging;

import java.util.logging.LogRecord;

public class LogFormatter extends CustomFormatter
{
    @Override
    public String format(LogRecord record)
    {
        createLoggingData(record);

        // java.util.logging set type of error() to severe, this change it back to error
        String type = record.getLevel().getName();
        if(type.equals("SEVERE")) type = "ERROR";
        if(type.equals("FINER")) type = "GAME";

        return String.format("[%s] %s: %s   (%s.%s:%d)[%d]%n", timestamp, type, message, shortClassName, methodName, lineNumber, thread);
    }
}
