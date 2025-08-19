package checkers.logging;

import checkers.Settings;

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
        if(type.equals("FINE")) type = "DEBUG";

        if(Settings.logCodeInfo)
        {
            return String.format("[%s] %s: %s   (%s.%s:%d)[%d]%n", timestamp, type, message, shortClassName, methodName, lineNumber, thread);
        }
        else
        {
            return String.format("[%s] %s: %s\n", timestamp, type, message);
        }
    }
}
