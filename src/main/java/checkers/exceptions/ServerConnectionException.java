package checkers.exceptions;

public class ServerConnectionException extends RuntimeException
{
    public ServerConnectionException()
    {
        super("Failed to connect to server");
    }
    public ServerConnectionException(String message)
    {
        super(message);
    }
}
