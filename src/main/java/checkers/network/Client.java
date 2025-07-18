package checkers.network;

import checkers.exceptions.ServerConnectionException;
import checkers.game.utils.GameSession;
import checkers.gui.popups.PopupAlert;
import checkers.gui.popups.PopupAlertButton;
import checkers.scenes.SceneBase;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Communicator
{
    private Socket socket = null;

    private boolean isConnected = false;
    private boolean isClosed = false;

    private PopupAlert waitForGameStartAlert = new PopupAlert("Waiting for game start...");

    public Client(String ip) throws ServerConnectionException
    {
        PopupAlertButton cancelButton = new PopupAlertButton("Cancel");
        cancelButton.setOnAction(e ->
        {
            close();
            waitForGameStartAlert.hide();
        });
        waitForGameStartAlert.addButton(cancelButton);

        SceneManager sceneManager = SceneManager.getInstance();
        SceneBase currentScene = sceneManager.getCurrentScene();
        currentScene.getContainer().getChildren().add(waitForGameStartAlert);

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
            System.out.println("Failed to create socket");
        }
    }

    public void start() throws ServerConnectionException
    {
        if(!isConnected) throw new ServerConnectionException("Failed to connect to server");

        System.out.println("Starting client");

        synchronizeGameSession();
        waitForGameStart();
    }

    @Override
    public void close()
    {
        if(!isClosed) isClosed = true;
        else return;

        System.out.println("Closing client");

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

    private void getSynchronizationData() throws IOException, ClassNotFoundException
    {
        GameSession clientData = (GameSession) objectInputStream.readObject();
        GameSession.getInstance().player1Username = clientData.player1Username;
        GameSession.getInstance().turnTime = clientData.turnTime;

        System.out.println(GameSession.getInstance().player1Username + " " +  GameSession.getInstance().player2Username + " " + GameSession.getInstance().turnTime);
    }

    private void waitForGameStart()
    {
        waitForGameStartAlert.show();

        Thread waitThread = new Thread(() ->
        {
            try
            {
                System.out.println("Waiting for game start");
                ServerState gameStart = (ServerState) objectInputStream.readObject();
                System.out.println("Start game state: " + gameStart);
                if(gameStart == ServerState.GAME_START)
                {
                    Platform.runLater(() ->
                    {
                        waitForGameStartAlert.hide();
                        startGame();
                    });
                }
            }
            catch (IOException | ClassNotFoundException e)
            {
                System.err.println("Failed to get game start information");
                close();
            }
        });

        waitThread.setDaemon(true);
        waitThread.start();
    }

    private void startGame()
    {
        System.out.println("Starting game");
        SceneManager.getInstance().setScene(SceneType.MULTIPLAYER_CLIENT);
        SceneManager.getInstance().getStage().setTitle("Checkers - multiplayer client");
    }
}
