package checkers.network;

import checkers.exceptions.ServerConnectionException;
import checkers.game.GameSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client
{
    private final int port = 5000;
    private Socket socket = null;

    private boolean isConnected = false;
    private boolean isClosed = false;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Client(String ip) throws ServerConnectionException
    {
        try
        {
            System.out.println("Creating new socket");
            this.socket = new Socket(ip, port);
            System.out.println("Socket created");

            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectOutputStream.flush();
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());

            ServerState response = (ServerState) objectInputStream.readObject();
            System.out.println("Server response: " + response);
            if(response == ServerState.BUSY)
            {
                throw new ServerConnectionException();
            }

            isConnected = true;
            System.out.println("Connected to server");
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();

            System.out.println("Failed to create socket");
        }
    }

    public void start() throws ServerConnectionException
    {
        if(!isConnected) throw new ServerConnectionException("Failed to connect to server");

        new Thread(() ->
        {
            System.out.println("Starting client");

            synchronizeGameSession();
        }).start();
    }

    public void close()
    {
        if(!isClosed) isClosed = true;
        else return;

        System.out.println("Closing client");

        try
        {
            if(objectInputStream != null)   objectInputStream.close();
            if(objectOutputStream != null)  objectOutputStream.close();
            if(socket != null)              socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void synchronizeGameSession()
    {
        System.out.println("Synchronizing game session");

        try
        {
            sendSynchronizationData();
            getSynchronizationData();
            System.out.println("Data synchronized");
       }
        catch (IOException | ClassNotFoundException e)
        {
            System.err.println("Failed to synchronize game session");
            close();
        }
    }

    private void sendSynchronizationData() throws IOException
    {
        objectOutputStream.writeObject(GameSession.getInstance());
    }

    private void getSynchronizationData() throws IOException, ClassNotFoundException
    {
        GameSession clientData = (GameSession) objectInputStream.readObject();
        GameSession.getInstance().player1Username = clientData.player1Username;
        GameSession.getInstance().turnTime = clientData.turnTime;

        System.out.println(GameSession.getInstance().player1Username + " " +  GameSession.getInstance().player2Username + " " + GameSession.getInstance().turnTime);
    }
}
