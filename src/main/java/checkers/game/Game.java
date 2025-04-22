package checkers.game;

public class Game
{
    final protected Board board = new Board();
    final protected Player[] players = { new Player(), new Player() };
    protected int currentPlayer = 0;

    public Board getBoard()
    {
        return board;
    }
}
