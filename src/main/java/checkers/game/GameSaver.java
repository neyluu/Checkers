package checkers.game;

import checkers.game.utils.GameSession;
import checkers.game.utils.Position;
import checkers.game.utils.PositionCode;
import checkers.logging.AppLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GameSaver
{
    public enum TurnType
    {
        WHITE,
        BLACK
    }

    private static GameSaver instance = null;

    private final String path = "games";
    private String filename;
    private File file;
    private FileWriter fileWriter;
    private AppLogger logger = new AppLogger(GameSaver.class);

    private LocalDateTime currentDateTime;
    private GameSession gameSession = GameSession.getInstance();
    private TurnType turn = TurnType.WHITE;

    private GameSaver() { }

    public static GameSaver get()
    {
        if(instance == null)
        {
            instance = new GameSaver();
        }

        return instance;
    }

    public void start() throws IOException
    {
        currentDateTime = LocalDateTime.now();
        filename = createFilename();
        logger.debug("Created filename: {}", filename);

        Files.createDirectories(Paths.get(path));
        file = new File(path + "/" + filename);

        try
        {
            fileWriter = new FileWriter(file, true);
        }
        catch (IOException e)
        {
            logger.error("Failed to create file writer!" + e.getMessage());
            // TODO if file cannot be created, rest of methods should not try save anything to it
        }

        writeHeader();
    }

    public void stop()
    {
        try
        {
            fileWriter.close();
        }
        catch (IOException e)
        {
            logger.error("Failed to close file writer!");
        }
    }

    public void setTurn(TurnType type) throws IOException
    {
        turn = type;
        writeTurn();
    }

    public void changeTurn() throws IOException
    {
        if(turn == TurnType.WHITE) turn = TurnType.BLACK;
        else                       turn = TurnType.WHITE;

        writeTurn();
    }

    public void move(Position from, Position to) throws IOException
    {
        fileWriter.write("    Move: " + PositionCode.toCode(from) + " to " + PositionCode.toCode(to) + "\n");
    }

    public void move(int fromX, int fromY, int toX, int toY) throws IOException
    {
        move(new Position(fromX, fromY), new Position(toX, toY));
    }

    public void beat(Position position) throws IOException
    {
        fileWriter.write("    Beat: " + PositionCode.toCode(position) + "\n");
    }

    public void beat(int x, int y) throws IOException
    {
        beat(new Position(x, y));
    }

    public void promote(Position position) throws IOException
    {
        fileWriter.write("    Prom: " + PositionCode.toCode(position) + "\n");
    }

    public void promote(int x, int y) throws IOException
    {
        promote(new Position(x, y));
    }

    public void win(String player, String reason) throws IOException
    {
        fileWriter.write("\n");
        fileWriter.write("GAME END\n");
        fileWriter.write("\n");
        fileWriter.write("Winner:       " + player + "\n");
        fileWriter.write("Reason:       " + reason);
    }

    private String createFilename()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss");
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime + "__" + gameSession.type + ".txt";
    }

    private void writeHeader() throws IOException
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = currentDateTime.format(formatter);

        fileWriter.write("Time:         " + time + "\n");
        fileWriter.write("Type:         " + gameSession.type + "\n");
        fileWriter.write("Player 1:     " + gameSession.player1Username + "\n");
        fileWriter.write("Player 2:     " + gameSession.player2Username + "\n");
        fileWriter.write("Game time:    " + gameSession.turnTime + "\n");
        fileWriter.write("\n");
        fileWriter.write("GAME START\n");
        fileWriter.write("\n");
    }

    private void writeTurn() throws IOException
    {
        String turnName = capitalize(turn.name());
        fileWriter.write("[" + turnName + "]:\n");
    }

    private String capitalize(String string)
    {
        string = string.toLowerCase();
        char firstChar = string.charAt(0);
        firstChar -= 32;
        return firstChar + string.substring(1);
    }
}
