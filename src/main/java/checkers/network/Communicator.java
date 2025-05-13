package checkers.network;

import checkers.game.GameSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Communicator
{
    protected final int port = 5555;
    protected ObjectOutputStream objectOutputStream;
    protected ObjectInputStream objectInputStream;

    public void sendMove(MovePacket move)
    {
        try
        {
            objectOutputStream.writeObject(move);
            objectOutputStream.flush();
        }
        catch (IOException e)
        {
            System.err.println("Failed to send move packet!");
            e.printStackTrace();
        }
    }

    public MovePacket getMove()
    {
        try
        {
            return (MovePacket) objectInputStream.readObject();
        }
        catch(IOException | ClassNotFoundException e)
        {
            System.err.println("Failed to get move packet!");
        }
        return null;
    }

    public void sendState(ServerState state)
    {
        try
        {
            objectOutputStream.writeObject(state);
            objectOutputStream.flush();
        }
        catch(IOException e)
        {
            System.err.println("Failed to send state!");
            e.printStackTrace();
        }
    }

    public ServerState getState()
    {
        try
        {
            return (ServerState) objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.err.println("Failed to get state!");
            e.printStackTrace();
        }
        return null;
    }

    protected void sendSynchronizationData() throws IOException
    {
        objectOutputStream.reset();
        objectOutputStream.writeObject(GameSession.getInstance());
        objectOutputStream.flush();
    }
}
