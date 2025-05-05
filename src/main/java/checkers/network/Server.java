package checkers.network;

import checkers.game.GameSession;
import checkers.gui.outputs.ServerAlerts;
import javafx.application.Platform;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private final int port = 5000;
    private final ServerSocket serverSocket;
    private Socket clientSocket = null;

    private boolean isRunning = false;
    private boolean isBusy = false;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private final ServerAlerts serverAlerts = new ServerAlerts();

    public Server() throws IOException
    {
        this.serverAlerts.setOnCancelAction(this::close);

        this.serverSocket = new ServerSocket(port);
    }

    public void start()
    {
        System.out.println("Starting server");

        isRunning = true;

        new Thread(() ->
        {
            while(!serverSocket.isClosed() && isRunning)
            {
                try
                {
                    System.out.println("Waiting for client");
                    if(!isBusy) Platform.runLater(serverAlerts::showWaitAlert);

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

                    Platform.runLater(serverAlerts::hideWaitAlert);
                    System.out.println("Client connected");

                    synchronizeGameSession();

                    serverAlerts.setConnectedPlayerUsername(GameSession.getInstance().player2Username);
                    Platform.runLater(serverAlerts::showClientConnectedAlert);

                    handleClient();
                }
                catch(IOException e)
                {
                    close();
                }
            }
        }).start();
    }

    public boolean isBusy()
    {
        return isBusy;
    }

    private void handleClient()
    {
        new Thread(() ->
        {
            while(isBusy)
            {
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
            if(objectInputStream != null)   objectInputStream.close();
            if(objectOutputStream != null)  objectOutputStream.close();
            if(clientSocket != null)        clientSocket.close();
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
            getSynchronizationData();
            sendSynchronizationData();
            System.out.println("Data synchronized");
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.err.println("Failed to synchronize game session");
            closeClient();
        }
    }

    private void getSynchronizationData() throws IOException, ClassNotFoundException
    {
        GameSession clientData = (GameSession) objectInputStream.readObject();
        GameSession.getInstance().player2Username = clientData.player2Username;

        System.out.println(GameSession.getInstance().player1Username + " " +  GameSession.getInstance().player2Username + " " + GameSession.getInstance().turnTime);
    }

    private void sendSynchronizationData() throws IOException
    {
        objectOutputStream.writeObject(GameSession.getInstance());
    }
}
