package checkers.game;

import java.util.ArrayList;
import java.util.List;

public class Man extends Piece
{
    public Man(int size, PieceType type)
    {
        super(size, type);
        isKing = false;

        if(isWhite())   textureName = "whiteMan.png";
        else            textureName = "blackMan.png";
        createStyle();
    }

    @Override
    public List<Position[]> getValidMoves(Board board)
    {
        List<Position[]> validMoves = new ArrayList<>();

        if(isWhite()) validMoves.addAll(getDiagonalMoves(board, -1));
        if(isBlack()) validMoves.addAll(getDiagonalMoves(board, 1));

        return validMoves;
    }

    @Override
    public List<Position[]> getBeatMoves(Board board)
    {
        List<Position[]> beatMoves = new ArrayList<>();
        Position[][] positions = {
            getDiagonalBeatMoves(board, -1, 1),
            getDiagonalBeatMoves(board, 1, 1),
            getDiagonalBeatMoves(board, -1, -1),
            getDiagonalBeatMoves(board, 1, -1)
        };

        for(Position[] position : positions)
        {
            if(position != null) beatMoves.add(position);
        }

        return beatMoves;
    }


    private List<Position[]> getDiagonalMoves(Board board, int directionY)
    {
        List<Position[]> moves = new ArrayList<>();

        checkAndAddMove(board, moves, x - 1, y + directionY);
        checkAndAddMove(board, moves, x + 1, y + directionY);

        return moves;
    }
    private void checkAndAddMove(Board board, List<Position[]> moves, int x, int y)
    {
        Position[] positions = new Position[1];
        if (board.isInBounds(x, y) && !board.getCell(x, y).havePiece())
        {
            positions[0] = new Position(x, y);
            moves.add(positions);
        }
    }

    private Position[] getDiagonalBeatMoves(Board board, int directionX, int directionY)
    {
        Position[] positions = new Position[2];

        Position positionClose = new Position(x + directionX, y - directionY);
        Position positionFar = new Position(x + directionX * 2, y - directionY * 2);

        Cell cellClose = board.getCell(positionClose);
        Cell cellFar = board.getCell(positionFar);

        if(board.isInBounds(positionClose) && cellClose.havePiece() &&
                cellClose.getPiece().isOpposite(this) &&
                board.isInBounds(positionFar) && !cellFar.havePiece()
        )
        {
            positions[0] = positionFar;
            positions[1] = positionClose;
            return positions;
        }

        return null;
    }
}
