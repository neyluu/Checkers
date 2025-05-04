package checkers.network;

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

    private boolean isRunning = false;
    private boolean isBusy = false;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Server() throws IOException
    {
        this.serverSocket = new ServerSocket(port);
    }

    public void start()
    {
        System.out.println("Starting server");

        isRunning = true;

        new Thread(() ->
        {
            try
            {
                while(!serverSocket.isClosed() && !isBusy)
                {
                    System.out.println("Waiting for client");
                    clientSocket = serverSocket.accept();
                    serverSocket.close();
                    System.out.println("Client connected");

                    objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.flush();
                    objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

                    isBusy = true;

                    synchronizeGameSession();
                    handleClient();
                }
            }
            catch(IOException e)
            {
                close();
            }
        }).start();
    }

    private void handleClient()
    {
        new Thread(() ->
        {
            while(true)
            {
                //TODO tmp
                if(clientSocket.isClosed()) closeClient();
//                try
//                {
//                }
//                catch (IOException e)
//                {
//                    closeClient();
//                }
            }
        }).start();
    }

    public void close()
    {
        if(isRunning) isRunning = false;
        else return;

        System.out.println("Closing server");

        try
        {
            closeClient();
            if(serverSocket != null) serverSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void closeClient()
    {
        System.out.println("Closing client");
        try
        {
            if(objectInputStream != null) objectInputStream.close();
            if(objectOutputStream != null) objectOutputStream.close();
            if(clientSocket != null) clientSocket.close();
            isBusy = false;
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
