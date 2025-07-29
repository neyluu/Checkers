package checkers;

public class Settings
{
    public static class LoggingConsoleFlags
    {
        public final boolean error = true;
        public final boolean warn  = true;
        public final boolean info  = true;
        public final boolean debug = true;
        public final boolean game  = true;
    }
    public static class LoggingFileFlags
    {
        public final boolean error = true;
        public final boolean warn  = true;
        public final boolean info  = true;
        public final boolean debug = true;
        public final boolean game  = true;
    }

    public static final int screenWidth = 1280;
    public static final int screenHeight = 720;

    public static String iconPath = "/assets/icon.png";
    public static String replaysPath = "replays";

    public static boolean logCodeInfo = true;

    public static LoggingConsoleFlags loggingConsole = new LoggingConsoleFlags();
    public static LoggingFileFlags loggingFile = new LoggingFileFlags();
}
