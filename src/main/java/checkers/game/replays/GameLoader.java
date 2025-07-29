package checkers.game.replays;

import checkers.exceptions.ReplayFileCorrupted;
import checkers.logging.AppLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GameLoader
{
    public class FileHeader
    {
        public String date;
        public String time;
        public String mode;
        public String player1;
        public String player2;
        public String gameTime;
    }

    private final AppLogger logger = new AppLogger(GameLoader.class);

    private File file;
    private Scanner scanner;


    public GameLoader(String filename)
    {
        file = new File(filename);
    }

    public GameLoader(File file)
    {
        this.file = file;
    }

    public FileHeader getHeader() throws ReplayFileCorrupted
    {
        FileHeader header = new FileHeader();

        try
        {
            scanner = new Scanner(file);

            String dateTime = scanner.nextLine();
            header.date = getDate(dateTime);
            header.time = getTime(dateTime);

            String mode = scanner.nextLine();
            header.mode = getMode(mode);

            String player1 = scanner.nextLine();
            String player2 = scanner.nextLine();
            header.player1 = getPlayer(player1);
            header.player2 = getPlayer(player2);

            String gameTime = scanner.nextLine();
            header.gameTime = getGameTime(gameTime);
        }
        catch(FileNotFoundException e)
        {
            logger.error("Failed to create scanner!");
            throw new ReplayFileCorrupted();
        }
        catch(NoSuchElementException e)
        {
            logger.error("Failed to load line!");
            throw new ReplayFileCorrupted();
        }
        catch(ReplayFileCorrupted e)
        {
            throw e;
        }

        return header;
    }

    private String getDate(String data) throws ReplayFileCorrupted
    {
        if(!data.contains("Time:")) throw new ReplayFileCorrupted();

        String[] parts = splitHeaderRawLine(data);
        String dateTime = parts[1].trim();
        String date = dateTime.split(" ", 2)[0];
        return reformatDate(date);
    }

    private String getTime(String data) throws ReplayFileCorrupted
    {
        if(!data.contains("Time:")) throw new ReplayFileCorrupted();

        String[] parts = splitHeaderRawLine(data);
        String dateTime = parts[1].trim();
        return dateTime.split(" ", 2)[1];
    }

    private String getMode(String data) throws ReplayFileCorrupted
    {
        if(!data.contains("Type:")) throw new ReplayFileCorrupted();

        return getHeaderLineValue(data).trim();
    }

    private String getPlayer(String data) throws ReplayFileCorrupted
    {
        if(!data.contains("Player 1:") && !data.contains("Player 2:")) throw new ReplayFileCorrupted();

        return getHeaderLineValue(data);
    }

    private String getGameTime(String data) throws ReplayFileCorrupted
    {
        if(!data.contains("Game time:")) throw new ReplayFileCorrupted();

        return getHeaderLineValue(data);
    }

    private String reformatDate(String date)
    {
        DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateTime = LocalDate.parse(date, parseFormatter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return localDateTime.format(formatter);
    }

    private String[] splitHeaderRawLine(String line)
    {
        return line.split(":", 2);
    }

    private String getHeaderLineValue(String line)
    {
        return splitHeaderRawLine(line)[1].trim();
    }
}
