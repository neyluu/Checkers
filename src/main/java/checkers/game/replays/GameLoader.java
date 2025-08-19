package checkers.game.replays;

import checkers.exceptions.ReplayFileCorrupted;
import checkers.game.utils.Position;
import checkers.game.utils.PositionCode;
import checkers.logging.AppLogger;

import java.io.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GameLoader
{
    public static class FileHeader
    {
        public String date;
        public String time;
        public String mode;
        public String player1;
        public String player2;
        public String gameTime;
    }

    public static class Move
    {
        public GameSaver.TurnType turn;
        public Position from;
        public Position to;
        public Position beatPosition;

        public boolean beat = false;
        public boolean promote = false;

        public Move() { }
        public Move(GameSaver.TurnType turn, Position from, Position to, Position beatPosition, boolean beat, boolean promote)
        {
            this.turn = turn;
            this.from = from;
            this.to = to;
            this.beatPosition = beatPosition;
            this.beat = beat;
            this.promote = promote;
        }

        @Override
        public String toString()
        {
            String _turn = turn == null ? "null" : turn.toString();
            String _from = from == null ? "null" : from.toString();
            String _to = to == null ? "null" : to.toString();
            String _beatPosition = beatPosition == null ? "null" : beatPosition.toString();
            String _beat = String.valueOf(beat);
            String _promote = String.valueOf(promote);

            return MessageFormat.format("[turn:{0} (from, to):[{1}, {2}] beat:({3}) beat:{4} promote:{5}]",
                    _turn, _from, _to, _beatPosition, _beat, _promote);
        }

        public static Move of(GameSaver.TurnType turn, String from, String to, String beat, boolean promote)
        {
            Move move = new Move();

            move.turn = turn;
            move.from = PositionCode.fromCode(from);
            move.to   = PositionCode.fromCode(to);
            if(beat != null)
            {
                move.beatPosition = PositionCode.fromCode(beat);
                move.beat = true;
            }
            move.promote = promote;

            return move;
        }
    }

    private final AppLogger logger = new AppLogger(GameLoader.class);

    private File file;
    private Scanner scanner;
    private BufferedReader reader = null;
    private GameSaver.TurnType currentTurn;

    private List<Move> moves = new ArrayList<>();

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

            checkGameStart();
        }
        catch(FileNotFoundException e)
        {
            throw new ReplayFileCorrupted();
        }
        catch(NoSuchElementException e)
        {
            throw new ReplayFileCorrupted();
        }
        catch(ReplayFileCorrupted e)
        {
            throw e;
        }

        return header;
    }


    public void loadMoves() throws ReplayFileCorrupted
    {
        try
        {
            reader = new BufferedReader(new FileReader(file));

            final int headerLines = 8;
            for(int i = 0; i < headerLines; i++)
            {
                reader.readLine();
            }
        }
        catch(IOException e)
        {
            throw new ReplayFileCorrupted();
        }

        while(true)
        {
            Optional<Move> move = getMove();
            if(move.isPresent()) moves.add(move.get());
            else break;
        }
    }

    private Optional<Move> getMove() throws ReplayFileCorrupted
    {
        System.out.println("====");

        Move move = new Move();
        String line;

        try
        {
            line = reader.readLine();
            if(line.trim().isEmpty())
            {
                System.out.println("E5");

                return Optional.empty();
            }
            System.out.println(line);

            boolean turnLoaded = false;

            if(line.toLowerCase().startsWith("[white]") || line.toLowerCase().startsWith("[black]"))
            {
                GameSaver.TurnType turn = line.substring(1, 6).equalsIgnoreCase("white")
                            ? GameSaver.TurnType.WHITE
                            : GameSaver.TurnType.BLACK;

                currentTurn = turn;
                move.turn = turn;
                turnLoaded = true;
                System.out.println("=reading turn");
            }
            else
            {
                move.turn = currentTurn;
            }


            if(turnLoaded)
            {
                line = reader.readLine();
                if(line.trim().isEmpty())
                {
                    System.out.println("E4");

                    return Optional.empty();
                }
                System.out.println(line);
            }

            if(line.toLowerCase().startsWith("    move:"))
            {
//                System.out.println("--move");

                try
                {
                    Position from = PositionCode.fromCode(line.substring(10, 12));
                    Position to = PositionCode.fromCode(line.substring(16, 18));

                    move.from = from;
                    move.to = to;
                    System.out.println("=reading move");
                }
                catch(IllegalArgumentException e)
                {
                    throw new ReplayFileCorrupted();
                }
            }
//            else
//            {
//                System.out.println("test");
//                throw new ReplayFileCorrupted();
//            }


            reader.mark(0);
            line = reader.readLine();
            System.out.println(line);
            if(line.trim().isEmpty())
            {
                return Optional.of(move);
//                System.out.println("E3");
//                return Optional.empty();
            }

            boolean promoteLoaded = false;

            if(line.toLowerCase().startsWith("    prom:"))
            {
//                System.out.println("--prom");

                try
                {
                    // dummy parse to check if code is correct
                    Position prom = PositionCode.fromCode(line.substring(10, 12));
                    move.promote = true;
                    promoteLoaded = true;
                    System.out.println("=reading prom");
                }
                catch(IllegalArgumentException e)
                {
                    throw new ReplayFileCorrupted();
                }
            }


            if(promoteLoaded)
            {
//                System.out.println("--promLoaded");

                reader.mark(0);
                line = reader.readLine();
                if(line.trim().isEmpty())
                {
                    System.out.println("E2");

                    return Optional.empty();
                }
                System.out.println(line);
            }

            boolean beatLoaded = false;

            if(line.toLowerCase().startsWith("    beat:"))
            {
//                System.out.println("--beat");

                try
                {
                    Position beat = PositionCode.fromCode(line.substring(10, 12));

                    move.beat = true;
                    move.beatPosition = beat;

                    beatLoaded = true;
                    System.out.println("=reading beat");
                }
                catch(IllegalArgumentException e)
                {
                    throw new ReplayFileCorrupted();
                }
            }


            if( ! promoteLoaded)
            {
                move.promote = false;
            }
            if( ! beatLoaded)
            {
                move.beatPosition = null;
                move.beat = false;
            }

            if( ! promoteLoaded && ! beatLoaded)
            {
                System.out.println("reset");
                reader.reset();
            }

            return Optional.of(move);
        }
        catch(Exception e)
        {
            System.out.println("E1");
            return Optional.empty();
        }
    }

    public List<Move> getMoves()
    {
        return moves;
    }

    private void checkGameStart() throws ReplayFileCorrupted
    {
        String empty1 = scanner.nextLine();
        String gameStart = scanner.nextLine();
        String empty2 = scanner.nextLine();

        if(! (empty1.isEmpty() && gameStart.equals("GAME START") && empty2.isEmpty()))
        {
            throw new ReplayFileCorrupted();
        }
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
        return getAndAssertResult(data);
    }

    private String getPlayer(String data) throws ReplayFileCorrupted
    {
        if(!data.contains("Player 1:") && !data.contains("Player 2:")) throw new ReplayFileCorrupted();
        return getAndAssertResult(data);
    }

    private String getGameTime(String data) throws ReplayFileCorrupted
    {
        if(!data.contains("Game time:")) throw new ReplayFileCorrupted();
        return getAndAssertResult(data);
    }

    private String getAndAssertResult(String data) throws ReplayFileCorrupted
    {
        String result = getHeaderLineValue(data);
        if(result.isEmpty()) throw new ReplayFileCorrupted();
        return result;
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
