package checkers.network;

public interface Communicator
{
    public void sendMove(MovePacket move);
    public MovePacket getMove();
}
