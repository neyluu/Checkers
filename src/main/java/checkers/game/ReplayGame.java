package checkers.game;

import checkers.exceptions.ReplayFileCorrupted;
import checkers.game.board.Cell;
import checkers.game.pieces.King;
import checkers.game.pieces.Man;
import checkers.game.pieces.Piece;
import checkers.game.pieces.PieceType;
import checkers.game.replays.GameLoader;
import checkers.game.replays.GameSaver;
import checkers.game.utils.Position;
import checkers.gui.buttons.MenuButton;
import checkers.gui.outputs.PlayerUI;
import checkers.gui.outputs.replays.ReplayBar;
import checkers.logging.AppLogger;

import java.util.List;

public class ReplayGame extends Game
{
    private MenuButton previousMove;
    private MenuButton nextMove;
    private GameLoader gameLoader;
    private List<GameLoader.Move> moves;
    private int currentMove = 0;

    private final AppLogger logger = new AppLogger(ReplayGame.class);

    public ReplayGame(PlayerUI player1UI, PlayerUI player2UI, MenuButton previousMove, MenuButton nextMove)
    {
        super(player1UI, player2UI);

        this.previousMove = previousMove;
        this.nextMove = nextMove;

        this.gameLoader = ReplayBar.getLastClicked().getLoader();

        try
        {
            this.gameLoader.loadMoves();
        }
        catch(ReplayFileCorrupted e)
        {
            logger.error("Replay file is corrupted!");
        }

        moves = this.gameLoader.getMoves();

        previousMove.setOnAction(e -> handlePreviousMove());
        nextMove.setOnAction(e -> handleNextMove());
    }

    private void handleNextMove()
    {
        if(currentMove + 1 > moves.size())
        {
            logger.warn("No more moves!");
            return;
        }

        GameLoader.Move move = moves.get(currentMove);

        board.movePiece(move.from, move.to);
        if(move.beat)
        {
            board.removePiece(move.beatPosition);
        }
        if(move.promote)
        {
            Cell cell = board.getCell(move.to);
            Piece piece = cell.getPiece();
            King king = new King(piece.getSize(), piece.getType(), piece.isTop());
            king.setX(piece.getX());
            king.setY(piece.getY());
            cell.clearPiece();
            cell.setPiece(king);
        }

        currentMove++;
    }

    private void handlePreviousMove()
    {
        if(currentMove - 1 < 0)
        {
            logger.warn("No more moves");
            return;
        }

        currentMove--;
        GameLoader.Move move = moves.get(currentMove);


        if(move.beat)
        {
            if(move.turn == GameSaver.TurnType.WHITE)
            {
                Piece piece = new Man(board.getCellSize(), PieceType.MAN_BLACK, false);
                piece.setX(move.beatPosition.x);
                piece.setY(move.beatPosition.y);
                board.getCell(move.beatPosition).setPiece(piece);
            }
            else
            {
                System.out.println("test");
                Piece piece = new Man(board.getCellSize(), PieceType.MAN_WHITE, false);
                piece.setX(move.beatPosition.x);
                piece.setY(move.beatPosition.y);
                board.getCell(move.beatPosition).setPiece(piece);
            }
        }
        if(move.promote)
        {
            if(move.turn == GameSaver.TurnType.WHITE)
            {
                System.out.println("test2");
                Piece piece = new Man(board.getCellSize(), PieceType.MAN_WHITE, false);
                piece.setX(move.from.x);
                piece.setY(move.from.y);
                board.getCell(move.from).setPiece(piece);
                board.removePiece(move.to);
            }
            else
            {
                System.out.println("test3");
                Piece piece = new Man(board.getCellSize(), PieceType.MAN_BLACK, true);
                piece.setX(move.from.x);
                piece.setY(move.from.y);
                board.getCell(move.from).setPiece(piece);
                board.removePiece(move.to);
            }
        }

        if(board.getCell(move.to).havePiece())
        {
            board.movePiece(move.to, move.from);
        }
    }

    @Override
    protected void changeTurn() { }

    @Override
    protected void onMove(Position from, Position to, Position beat, boolean isBeatMoves) { }

    @Override
    public void start() { }
}
