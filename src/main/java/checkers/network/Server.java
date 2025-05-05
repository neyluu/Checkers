package checkers.network;

import checkers.game.GameSession;

import java.io.DataOutputStream;
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
            while(!serverSocket.isClosed())
            {
                try
                {
                    System.out.println("Waiting for client");
                    Socket incomingSocket = serverSocket.accept();

                    ObjectOutputStream tmpOutput = new ObjectOutputStream(incomingSocket.getOutputStream());
                    tmpOutput.flush();
                    if(isBusy)
                    {
                        tmpOutput.writeObject(ServerState.BUSY);
                        tmpOutput.close();
                        continue;
                    }
                    tmpOutput.writeObject(ServerState.OK);

                    clientSocket = incomingSocket;
                    objectOutputStream = tmpOutput;
                    objectOutputStream.flush();
                    objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

                    isBusy = true;

                    System.out.println("Client connected");
                    synchronizeGameSession();
                    handleClient();
                }
                catch(IOException e)
                {
                    System.out.println(e.getMessage());
                    e.printStackTrace();

                    System.out.println("close1");
                    close();
                }
            }
        }).start();
    }

    private void handleClient()
    {
        new Thread(() ->
        {
            while(isBusy)
            {
//                System.out.println("x");
                //TODO tmp
                if(clientSocket.isClosed()) closeClient();
                try
                {
                    Object o = objectInputStream.readObject();
                }
                catch (IOException  | ClassNotFoundException e)
                {
                    closeClient();
                }
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
        if(!isBusy) return;

        System.out.println("Closing client");
        try
        {
            if(objectInputStream != null) objectInputStream.close();
            if(objectOutputStream != null) objectOutputStream.close();
            if(clientSocket != null) clientSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            isBusy = false;
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
