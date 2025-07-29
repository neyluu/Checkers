package checkers.exceptions;

public class ReplayFileCorrupted extends Exception
{
    public ReplayFileCorrupted()
    {
        super("File corrupted!");
    }

    public ReplayFileCorrupted(String message)
    {
        super(message);
    }
}
