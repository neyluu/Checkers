package checkers.network;

import checkers.exceptions.ServerConnectionException;
import checkers.game.utils.GameSession;
import checkers.gui.popups.PopupAlert;
import checkers.gui.popups.PopupAlertButton;
import checkers.logging.AppLogger;
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
    private final AppLogger logger = new AppLogger(Client.class);

    public Client(String ip) throws ServerConnectionException
    {
        initAlert();

        logger.info("Creating client...");

        try
        {
            this.socket = new Socket(ip, port);

            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectOutputStream.flush();
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());

            logger.info("Trying to connect to server...");

            ServerState response = (ServerState) objectInputStream.readObject();
            logger.debug("Server response: {}", response);
            if(response == ServerState.BUSY)
            {
                // TODO alert????????
                throw new ServerConnectionException();
            }

            isConnected = true;

            logger.info("Connected to server!");
        }
        catch (IOException | ClassNotFoundException e)
        {
            logger.error("Failed to connect to server: {}", e.getMessage());
        }
    }

    private void initAlert()
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
    }

    public void start() throws ServerConnectionException
    {
        if(!isConnected) throw new ServerConnectionException("Failed to connect to server");

        logger.info("Starting client...");

        synchronizeGameSession();
        waitForGameStart();
    }

    @Override
    public void close()
    {
        if(!isClosed) isClosed = true;
        else return;

        logger.info("Closing client...");

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

            logger.info("Client closed!");
        }
        catch (IOException e)
        {
            logger.error("Failed to close client!");
        }
    }

    private void synchronizeGameSession()
    {
        logger.info("Synchronizing game session...");

        try
        {
            sendSynchronizationData();
            getSynchronizationData();
            logger.info("Game session synchronized!");
       }
        catch (IOException | ClassNotFoundException e)
        {
            logger.error("Failed to synchronize game session!");
            close();
        }
    }

    private void getSynchronizationData() throws IOException, ClassNotFoundException
    {
        GameSession clientData = (GameSession) objectInputStream.readObject();
        GameSession.getInstance().player1Username = clientData.player1Username;
        GameSession.getInstance().turnTime = clientData.turnTime;

        logger.debug(GameSession.getInstance().toString());
    }

    private void waitForGameStart()
    {
        waitForGameStartAlert.show();

        Thread waitThread = new Thread(() ->
        {
            try
            {
                logger.info("Waiting to game start...");

                ServerState gameStart = (ServerState) objectInputStream.readObject();
                logger.debug("Start game state: {}", gameStart);
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
                logger.error("Failed to get game start state!");
                close();
            }
        });

        waitThread.setDaemon(true);
        waitThread.start();
    }

    private void startGame()
    {
        logger.info("Starting game");
        SceneManager.getInstance().setScene(SceneType.MULTIPLAYER_CLIENT);
        SceneManager.getInstance().getStage().setTitle("Checkers - multiplayer client");
    }
}
