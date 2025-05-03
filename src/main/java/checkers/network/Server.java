package checkers.network;

import checkers.game.Game;
import checkers.game.GameSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private final int port = 5000;
    private ServerSocket serverSocket;
    private Socket clientSocket = null;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Server() throws IOException
    {
        this.serverSocket = new ServerSocket(port);
    }

    public void start()
    {
        System.out.println("Starting server");

        new Thread(() ->
        {
            try
            {
                while(!serverSocket.isClosed())
                {
                    clientSocket = serverSocket.accept();
                    objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.flush();

                    synchronizeGameSession();
                    handleClient();
                }
            }
            catch(IOException e)
            {

            }
        }).start();
    }

    private void handleClient()
    {
        new Thread(() ->
        {
            System.out.println("Client connected!");

        }).start();
    }

    private void close()
    {
        System.out.println("Closing server");
        try
        {
            if(serverSocket != null) serverSocket.close();
            if(clientSocket != null) clientSocket.close();
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
            GameSession clientData = (GameSession) objectInputStream.readObject();
            GameSession.getInstance().player2Username = clientData.player2Username;

            System.out.println(GameSession.getInstance().player1Username + " " +  GameSession.getInstance().player2Username + " " + GameSession.getInstance().turnTime);
        }
        catch (IOException | ClassNotFoundException e)
        {

        }

        try
        {
            objectOutputStream.writeObject(GameSession.getInstance());
        }
        catch (IOException  e)
        {

        }
    }
}
