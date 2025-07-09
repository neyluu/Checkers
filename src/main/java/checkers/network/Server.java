package checkers.network;

import checkers.game.utils.GameSession;
import checkers.gui.popups.PopupAlert;
import checkers.gui.popups.PopupAlertButton;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Communicator
{
    private final ServerSocket serverSocket;
    private Socket clientSocket = null;

    private boolean isRunning = false;
    private boolean isBusy = false;

    private final PopupAlert waitForClientAlert = new PopupAlert("Waiting for second player...");
    private final PopupAlert clientConnectedAlert = new PopupAlert("Unknown connected!");
    private final PopupAlert clientDisconnectedAlert = new PopupAlert("Client disconnected!");


    public Server() throws IOException
    {
        initAlerts();
        this.serverSocket = new ServerSocket(port);
    }

    private void initAlerts()
    {
        PopupAlertButton cancelButton = new PopupAlertButton("Cancel");
        cancelButton.setOnAction(e ->
        {
            waitForClientAlert.hide();
            close();
        });
        waitForClientAlert.addButton(cancelButton);

        PopupAlertButton startGameButton = new PopupAlertButton("Start game!");
        startGameButton.setOnAction(e ->
        {
            startGame();
        });
        clientConnectedAlert.addButton(startGameButton);

        PopupAlertButton okButton = new PopupAlertButton("OK");
        okButton.setOnAction(e ->
        {
            clientDisconnectedAlert.hide();
        });
        clientDisconnectedAlert.addButton(okButton);
    }

    public void start()
    {
        System.out.println("Starting server");

        isRunning = true;

        Thread startThread = new Thread(() ->
        {
            while(!serverSocket.isClosed() && isRunning)
            {
                try
                {
                    System.out.println("Waiting for client");
                    if(!isBusy) waitForClientAlert.show();

                    Socket incomingSocket = serverSocket.accept();

                    ObjectOutputStream tmpOutput = new ObjectOutputStream(incomingSocket.getOutputStream());
                    tmpOutput.flush();
                    if(isBusy)
                    {
                        tmpOutput.writeObject(ServerState.BUSY);
                        tmpOutput.flush();
                        tmpOutput.close();
                        incomingSocket.close();
                        continue;
                    }
                    tmpOutput.writeObject(ServerState.OK);

                    clientSocket = incomingSocket;
                    objectOutputStream = tmpOutput;
                    objectOutputStream.flush();
                    objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

                    isBusy = true;

                    waitForClientAlert.hide();
                    System.out.println("Client connected");

                    synchronizeGameSession();

                    System.out.println(GameSession.getInstance().player2Username);
                    clientConnectedAlert.setTitle(GameSession.getInstance().player2Username + " connected!");
                    clientConnectedAlert.show();
                }
                catch(IOException e)
                {
                    close();
                }
            }
        });

        startThread.setDaemon(true);
        startThread.start();
    }

    @Override
    public void close()
    {
        if(isRunning) isRunning = false;
        else return;

        System.out.println("Closing server");

        try
        {
            try
            {
                if(objectOutputStream != null)
                {
                    GlobalCommunication.communicator.sendState(null);
                }
            }
            catch (IOException e2) { }

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
            e.printStackTrace();
            closeClient();
        }
    }

    private void getSynchronizationData() throws IOException, ClassNotFoundException
    {
        GameSession clientData = (GameSession) objectInputStream.readObject();
        GameSession.getInstance().player2Username = clientData.player2Username;

        System.out.println(GameSession.getInstance().player1Username + " " +  GameSession.getInstance().player2Username + " " + GameSession.getInstance().turnTime);
    }

    public void startGame()
    {
        try
        {
            objectOutputStream.writeObject(ServerState.GAME_START);
            objectOutputStream.flush();
        }
        catch (IOException e)
        {
            clientConnectedAlert.hide();
            clientDisconnectedAlert.show();

            System.err.println("Failed to send game start information to client");
            close();

            return;
        }

        SceneManager.getInstance().setScene(SceneType.MULTIPLAYER_SERVER);
        SceneManager.getInstance().getStage().setTitle("Checkers - multiplayer server");
    }
}
