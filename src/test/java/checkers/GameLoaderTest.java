package checkers;

import checkers.exceptions.ReplayFileCorrupted;
import checkers.game.replays.GameLoader;
import org.junit.jupiter.api.Test;

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

    @Test
    public void getHeader_emptyFile()
    {
        GameLoader loader = new GameLoader(loadTestFile("empty.txt"));
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getHeader_corruptedDateTime()
    {
        GameLoader loader = new GameLoader(loadTestFile("corruptedDateTime.txt"));
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getHeader_corruptedType()
    {
        GameLoader loader = new GameLoader(loadTestFile("corruptedType.txt"));
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getHeader_corruptedPlayers()
    {
        GameLoader loader = new GameLoader(loadTestFile("corruptedPlayers.txt"));
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getHeader_corruptedGameTime()
    {
        GameLoader loader = new GameLoader(loadTestFile("corruptedGameTime.txt"));
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getHeader_noDateTime()
    {
        GameLoader loader = new GameLoader(loadTestFile("noDateTime.txt"));
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getHeader_noMode()
    {
        GameLoader loader = new GameLoader(loadTestFile("noMode.txt"));
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getHeader_noPlayer1()
    {
        GameLoader loader = new GameLoader(loadTestFile("noPlayer1.txt"));
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getHeader_noPlayer2()
    {
        GameLoader loader = new GameLoader(loadTestFile("noPlayer2.txt"));
        assertThrows(Exception.class, loader::getHeader);
    }

    @Test
    public void getHeader_noBothPlayers()
    {
        GameLoader loader = new GameLoader(loadTestFile("noBothPlayers.txt"));
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
