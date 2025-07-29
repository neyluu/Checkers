package checkers;

import checkers.exceptions.ReplayFileCorrupted;
import checkers.game.replays.GameLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class GameLoaderTest
{
    private final String testFileDirectory = "gameLoaderTest/";

    @Test
    public void getHeader_correctData()
    {
        GameLoader loader = new GameLoader(loadTestFile("correctData.txt"));
        GameLoader.FileHeader header = null;
        try
        {
            header = loader.getHeader();
        }
        catch (ReplayFileCorrupted e)
        {
            fail();
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
        GameLoader loader = new GameLoader(loadTestFile(invalidFilename));
        assertThrows(Exception.class, loader::getHeader);
    }


    private File loadTestFile(String path)
    {
        URL resource = getClass().getClassLoader().getResource(testFileDirectory + path);

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
