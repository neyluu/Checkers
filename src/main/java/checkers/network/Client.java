package checkers.network;

import checkers.exceptions.ServerConnectionException;
import checkers.game.GameSession;
import checkers.gui.outputs.ClientAlerts;
import checkers.scenes.utils.SceneManager;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Communicator
{
    private final int port = 5555;
    private Socket socket = null;

    private boolean isConnected = false;
    private boolean isClosed = false;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private ClientAlerts clientAlerts = new ClientAlerts();

    public Client(String ip) throws ServerConnectionException
    {
        clientAlerts.setOnWaitAction(this::close);

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

//        new Thread(() ->
//        {
            System.out.println("Starting client");

            synchronizeGameSession();
            waitForGameStart();
//        }).start();
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
        objectOutputStream.reset();
        objectOutputStream.writeObject(GameSession.getInstance());
        objectOutputStream.flush();
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
        Platform.runLater(clientAlerts::showWaitAlert);

        new Thread(() ->
        {
            try
            {
                System.out.println("Waiting for game start");
                ServerState gameStart = (ServerState) objectInputStream.readObject();
                System.out.println("Start game state: " + gameStart);
                if(gameStart == ServerState.GAME_START)
                {
                    clientAlerts.setOnWaitAction(null);
                    Platform.runLater(clientAlerts::hideWaitAlert);
                    Platform.runLater(() ->
                    {
                        clientAlerts.hideWaitAlert();
                        startGame();
                    });
                }
            }
            catch (IOException | ClassNotFoundException e)
            {
                System.err.println("Failed to get game start information");
                close();
            }
        }).start();
    }

    private void startGame()
    {
        System.out.println("Starting game");
        SceneManager.getInstance().setScene(SceneType.MULTIPLAYER_CLIENT);
        SceneManager.getInstance().getStage().setTitle("Checkers - multiplayer client");
    }

    @Override
    public void sendMove(MovePacket move)
    {
        try
        {
            objectOutputStream.writeObject(move);
            System.out.println("Packet sent");
        }
        catch (IOException e)
        {
            System.err.println("Failed to send move packet!");
            e.printStackTrace();
        }
    }

    @Override
    public MovePacket getMove()
    {
        try
        {
            MovePacket move = (MovePacket) objectInputStream.readObject();
            return move;
        }
        catch(IOException | ClassNotFoundException e)
        {
            System.err.println("Failed to get move packet!");
        }
        return null;
    }
}
