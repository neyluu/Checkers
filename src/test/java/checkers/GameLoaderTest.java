package checkers;

import checkers.exceptions.ReplayFileCorrupted;
import checkers.game.replays.GameLoader;
import checkers.game.replays.GameSaver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameLoaderTest
{
    private final String testFileDirectory = "gameLoaderTest/";

    @Test
    public void getHeader_correctData()
    {
        GameLoader loader = createLoader("correctData.txt");
        GameLoader.FileHeader header = null;
        try
        {
            header = loader.getHeader();
        }
        catch (ReplayFileCorrupted e)
        {
            fail(e.getMessage());
        }

        assertEquals("28-07-2025", header.date);
        assertEquals("17:38:24", header.time);
        assertEquals("singleplayer", header.mode);
        assertEquals("Bot", header.player1);
        assertEquals("Player", header.player2);
        assertEquals("unlimited", header.gameTime);
    }

    @ParameterizedTest(name = "getHeader throws exception for invalid file: {0}")
    @ValueSource(strings = {
            "empty.txt",
            "corruptedDateTime.txt",
            "corruptedType.txt",
            "corruptedPlayers.txt",
            "corruptedGameTime.txt",
            "noDateTime.txt",
            "noMode.txt",
            "noPlayer1.txt",
            "noPlayer2.txt",
            "noBothPlayers.txt",
            "noGameTime.txt"
    })
    public void getHeader_throwsExceptionForCorruptedFiles(String invalidFilename)
    {
        GameLoader loader = createLoader(invalidFilename);
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getMoves_correctData()
    {
        GameLoader loader = createLoader("validMoves1.txt");

        List<GameLoader.Move> expectedMoves = List.of(
                GameLoader.Move.of(GameSaver.TurnType.WHITE, "E3", "D4", null, false),
                GameLoader.Move.of(GameSaver.TurnType.BLACK, "F6", "G5", null, false),
                GameLoader.Move.of(GameSaver.TurnType.WHITE, "G5", "E3", "F4", false),
                GameLoader.Move.of(GameSaver.TurnType.WHITE, "E3", "C5", "D4", false),
                GameLoader.Move.of(GameSaver.TurnType.BLACK, "C5", "E3", "D4", false),
                GameLoader.Move.of(GameSaver.TurnType.WHITE, "C5", "E3", "D4", false),
                GameLoader.Move.of(GameSaver.TurnType.WHITE, "E3", "G1", "F2", true),
                GameLoader.Move.of(GameSaver.TurnType.BLACK, "A3", "C1", "B2", true),
                GameLoader.Move.of(GameSaver.TurnType.BLACK, "C1", "F4", "D2", false)
        );

        try
        {
            loader.loadMoves();
        }
        catch (ReplayFileCorrupted e)
        {
            fail("Loading file failed");
        }

        List<GameLoader.Move> loadedMoves = loader.getMoves();

        if(expectedMoves.size() != loadedMoves.size())
        {
            fail("Expected size: " + expectedMoves.size() + " received: " + loadedMoves.size());
        }

        for(int i = 0; i < expectedMoves.size(); i++)
        {
            assertEquals(expectedMoves.get(i).toString(), loadedMoves.get(i).toString(), " index: " + i);
        }
    }
    

    private GameLoader createLoader(String filename)
    {
        return new GameLoader(loadTestFile(filename));
    }

    private File loadTestFile(String filename)
    {
        URL resource = getClass().getClassLoader().getResource(testFileDirectory + filename);

        try
        {
            return new File(resource.toURI());
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }
}
