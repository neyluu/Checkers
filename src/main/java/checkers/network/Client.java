package checkers.network;

import checkers.game.GameSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client
{
    private final int port = 5000;
    private Socket socket = null;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Client(String ip)
    {
        try
        {
            this.socket = new Socket(ip, port);

            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectOutputStream.flush();
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {

        }
    }

    public void start()
    {
        new Thread(() ->
        {
            System.out.println("Starting client");

            synchronizeGameSession();
        }).start();
    }

    private void close()
    {
        try
        {
            if(socket != null) socket.close();
        }
        catch (IOException e)
        {

        }
    }

    private void synchronizeGameSession()
    {
        System.out.println("Synchronizing game session");

        try
        {
            objectOutputStream.writeObject(GameSession.getInstance());
        }
        catch (IOException  e)
        {

        }

        try
        {
            GameSession clientData = (GameSession) objectInputStream.readObject();
            GameSession.getInstance().player1Username = clientData.player1Username;
            GameSession.getInstance().turnTime = clientData.turnTime;

            System.out.println(GameSession.getInstance().player1Username + " " +  GameSession.getInstance().player2Username + " " + GameSession.getInstance().turnTime);
        }
        catch (IOException | ClassNotFoundException e)
        {

        }
    }
}
