package checkers.game;

import checkers.gui.outputs.PlayerUI;
import javafx.application.Platform;
import javafx.geometry.Pos;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SingleplayerGame extends Game
{
    public SingleplayerGame(PlayerUI player1UI, PlayerUI player2UI)
    {
        super(player1UI, player2UI);
        currentTurn = PieceType.WHITE;
    }

    @Override
    protected void onMove(Position from, Position to, Position beat, boolean isBeatMoves)
    {
        return;
    }

    @Override
    protected void changeTurn()
    {
        if(currentTurn == PieceType.WHITE)
        {
            if(board.getBlackPiecesCount() == 0)
            {
                gameOver();
                return;
            }
            currentTurn = PieceType.BLACK;
            uiPlayer1Turn();
            System.out.println("Black turn");
            aiTurn();
        }
        else if(currentTurn == PieceType.BLACK)
        {
            if(board.getWhitePieceCount() == 0)
            {
                gameOver();
                return;
            }
            currentTurn = PieceType.WHITE;
            uiPlayer2Turn();
            System.out.println("White turn");
            turn();
        }
    }

    public void start()
    {
        uiPlayer2Turn();
        watchTimers();
        turn();
    }

    public void reset()
    {
        resetScheduler();
        board.clearBoard(false);
        player1UI.resetTimer();
        player2UI.resetTimer();
        currentTurn = PieceType.WHITE;
    }

    private void watchTimers()
    {
        if(timersScheduler == null)
        {
            createTimersScheduler(() ->
            {
                if (player1UI.isTimerFinished() || player2UI.isTimerFinished())
                {
                    if (currentTurn == PieceType.WHITE) currentTurn = PieceType.BLACK;
                    else if (currentTurn == PieceType.BLACK) currentTurn = PieceType.WHITE;
                    gameOver();
                    timersScheduler.shutdown();
                }
            });
        }
    }

    private void gameOver()
    {

    }

    private void aiTurn()
    {
        System.out.println("Generating beat moves...");
        Map<Piece, List<Position[]>> beatMoves = board.getPiecesWithValidMoves(currentTurn, true);
        System.out.println("Moves generated");

        if(!beatMoves.isEmpty())
        {

            System.out.println("Beat move, currently skipped");
            Random random = new Random();
            int pieceIndex = random.ints(0, beatMoves.size()).findFirst().getAsInt();
            System.out.println("Piece: " + pieceIndex);

            int i = 0;
            for(Map.Entry<Piece, List<Position[]>> entry : beatMoves.entrySet())
            {
                if(i == pieceIndex)
                {
                    int moveIndex = random.ints(0, entry.getValue().size()).findFirst().getAsInt();
                    System.out.println("Move: " + moveIndex);

                    Piece piece = entry.getKey();
                    List<Position[]> validMoves = entry.getValue();
                    Position[] to = validMoves.get(moveIndex);

                    board.movePiece(piece.getX(), piece.getY(), to[0].x, to[0].y);

                    Cell cell = board.getCell(to[0].x, to[0].y);
                    Piece currentPiece = cell.getPiece();
                    currentPiece = tryPromoteToKing(currentPiece, cell);


                    board.removePiece(to[1]);
//                    aiTurn();

                    changeTurn();
                    return;
                }
                i++;
            }


        }

        Map<Piece, List<Position[]>> moves = board.getPiecesWithValidMoves(currentTurn, false);
        if(moves.isEmpty()) gameOver(); // ???

        Random random = new Random();
        int pieceIndex = random.ints(0, moves.size()).findFirst().getAsInt();
        System.out.println(pieceIndex);

        int i = 0;
        for(Map.Entry<Piece, List<Position[]>> entry : moves.entrySet())
        {
            if(i == pieceIndex)
            {
                Piece piece = entry.getKey();
                List<Position[]> validMoves = entry.getValue();

                int moveIndex = random.ints(0, entry.getValue().size()).findFirst().getAsInt();
                System.out.println("Move: " + moveIndex);

                Position[] to = validMoves.get(moveIndex);

                board.movePiece(piece.getX(), piece.getY(), to[0].x, to[0].y);

                Cell cell = board.getCell(to[0].x, to[0].y);
                Piece currentPiece = cell.getPiece();
                currentPiece = tryPromoteToKing(currentPiece, cell);

                changeTurn();
            }
            i++;
        }
    }
}
