package checkers.game;

import checkers.game.board.Cell;
import checkers.game.pieces.Piece;
import checkers.game.pieces.PieceType;
import checkers.game.utils.Position;
import checkers.gui.outputs.PlayerUI;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SingleplayerGame extends OfflineGame
{
    private final int aiMoveDelay = 1000;
    private final Random random = new Random();

    public SingleplayerGame(PlayerUI player1UI, PlayerUI player2UI)
    {
        super(player1UI, player2UI);
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

    private void aiTurn()
    {
        Map<Piece, List<Position[]>> beatMoves = board.getPiecesWithValidMoves(currentTurn, true);
        if(!beatMoves.isEmpty())
        {
            aiDoMove(beatMoves, true);
            return;
        }

        Map<Piece, List<Position[]>> moves = board.getPiecesWithValidMoves(currentTurn, false);
        if (moves.isEmpty()) gameOver();

        aiDoMove(moves, false);
    }

    private void aiDoMove(Map<Piece, List<Position[]>> moves, boolean isBeatMoves)
    {
        Thread thread = new Thread(() ->
        {
            try
            {
                Thread.sleep(aiMoveDelay);
            }
            catch (Exception e) { }

            int pieceIndex = random.ints(0, moves.size()).findFirst().getAsInt();

            int i = 0;
            for(Map.Entry<Piece, List<Position[]>> entry : moves.entrySet())
            {
                if (i == pieceIndex)
                {
                    int moveIndex = random.ints(0, entry.getValue().size()).findFirst().getAsInt();

                    Piece piece = entry.getKey();
                    List<Position[]> validMoves = entry.getValue();
                    Position[] to = validMoves.get(moveIndex);

                    Platform.runLater(() ->
                    {
                        board.movePiece(piece.getX(), piece.getY(), to[0].x, to[0].y);

                        Cell cell = board.getCell(to[0].x, to[0].y);
                        Piece currentPiece = cell.getPiece();
                        currentPiece = tryPromoteToKing(currentPiece, cell);

                        if(isBeatMoves)
                        {
                            board.removePiece(to[1]);
                            aiNextBeats(currentPiece);
                            return;
                        }

                        changeTurn();
                    });
                }
                i++;
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    private void aiNextBeats(Piece piece)
    {
        List<Position[]> pieceBeatMoves = piece.getBeatMoves(board);
        if(pieceBeatMoves.isEmpty())
        {
            changeTurn();
            return;
        }

        Map<Piece, List<Position[]>> data = new HashMap<>();
        data.put(piece, pieceBeatMoves);

        aiDoMove(data, true);
    }
}
