package checkers.game;

public class Game
{
    final private Board board = new Board();
    final private Player[] players = { new Player(), new Player() };
    private int currentPlayer = 0;

    public Board getBoard()
    {
        return board;
    }
}
