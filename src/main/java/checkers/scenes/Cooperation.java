package checkers.scenes;

import checkers.game.CooperationGame;
import checkers.game.PieceType;
import checkers.scenes.utils.SceneType;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class Cooperation extends GameSceneBase
{
    private CooperationGame game;
    private final Object lock = new Object();

    public Cooperation()
    {
        game = new CooperationGame(lock, player1UI, player2UI);
        updateBoard(game.getBoard());

        startGame();
        listenForMessage();
    }

    private void startGame()
    {
        Thread thread = new Thread(() -> game.start());
        thread.setDaemon(true);
        thread.start();
    }

    private void listenForMessage()
    {
        Thread thread = new Thread(() ->
        {
            synchronized (lock)
            {
                try
                {
                    lock.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            Platform.runLater(this::showWinAlert);
        });

        thread.setDaemon(true);
        thread.start();
    }

    public void showWinAlert()
    {
        ButtonType playAgain = new ButtonType("PlayAgain", ButtonBar.ButtonData.OK_DONE);
        ButtonType quit = new ButtonType("Quit", ButtonBar.ButtonData.OK_DONE);
        ButtonType quitMainMenu = new ButtonType("Quit to main menu", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("");
        alert.setHeaderText("Game finished!");
        alert.setContentText((game.getCurrentTurn() == PieceType.WHITE ? player2Username : player1Username) + " win!");

        alert.getButtonTypes().setAll(playAgain, quit, quitMainMenu);

        alert.showAndWait();

        ButtonType alertResult = alert.getResult();

        if(alertResult.equals(quit))
        {
            Platform.exit();
        }
        if(alertResult.equals(quitMainMenu))
        {
            sceneManager.setScene(SceneType.MAIN_MENU);
        }
        if(alertResult.equals(playAgain))
        {
            game.reset();
            startGame();
            listenForMessage();
        }
    }
}
