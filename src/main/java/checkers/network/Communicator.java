package checkers.network;

import checkers.game.GameSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class Communicator
{
    protected final int port = 5555;
    protected ObjectOutputStream objectOutputStream;
    protected ObjectInputStream objectInputStream;

    public abstract void close();

    public void sendMove(MovePacket move) throws IOException
    {
        objectOutputStream.writeObject(move);
        objectOutputStream.flush();
    }

    public void sendState(ServerState state) throws IOException
    {
        objectOutputStream.writeObject(state);
        objectOutputStream.flush();
    }

    public Object getObject() throws IOException, ClassNotFoundException
    {
        return objectInputStream.readObject();
    }

    protected void sendSynchronizationData() throws IOException
    {
        objectOutputStream.reset();
        objectOutputStream.writeObject(GameSession.getInstance());
        objectOutputStream.flush();
    }
}
