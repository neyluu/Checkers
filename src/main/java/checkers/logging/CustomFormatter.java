package checkers.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public abstract class CustomFormatter extends Formatter
{
    protected String timestamp;
    protected String message;
    protected String className;
    protected String shortClassName;
    protected String methodName;
    protected int lineNumber;
    protected long thread;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    protected CustomFormatter() {}

    abstract public String format(LogRecord record);

    protected int getLineNumber(LogRecord record)
    {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        String className = record.getSourceClassName();
        String methodName = record.getSourceMethodName();

        for (StackTraceElement element : stack)
        {
            if (element.getClassName().equals(className) && element.getMethodName().equals(methodName))
            {
                return element.getLineNumber();
            }
        }
        return -1;
    }

    protected String getShortClassName(String className)
    {
        return className != null && className.contains(".")
               ? className.substring(className.lastIndexOf('.') + 1)
               : className;
    }

    protected void createLoggingData(LogRecord record)
    {
        timestamp = simpleDateFormat.format(new Date(record.getMillis()));
        message = formatMessage(record);
        className = record.getSourceClassName();
        shortClassName = getShortClassName(className);
        methodName = record.getSourceMethodName();
        lineNumber = getLineNumber(record);
        thread = record.getLongThreadID();
    }
}
