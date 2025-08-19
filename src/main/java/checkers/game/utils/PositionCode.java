package checkers.game.utils;

public class PositionCode
{
    private String code;

    public PositionCode() { }

    public PositionCode(Position position)
    {
        this(position.x, position.y);
    }

    public PositionCode(int x, int y)
    {
        setCode(x, y);
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(Position position)
    {
        code = toCode(position);
    }

    public void setCode(int x, int y)
    {
        code = toCode(x, y);
    }

    public Position getPosition()
    {
        char c1 = code.charAt(0);
        char c2 = code.charAt(1);

        int x = (int) c1 - 65;
        int y = 8 - (c2 - '0');
        return new Position(x, y);
    }

    public static String toCode(Position position)
    {
        return toCode(position.x, position.y);
    }

    public static String toCode(int x, int y)
    {
        final int aAscii = 65;
        final int boardSize = 8;

        int c1 = x + aAscii;
        int c2 = boardSize - y;
        return "" + (char) c1 + (char)(c2 + '0');
    }

    public static Position fromCode(String code)
    {
        if(!validateCode(code))
        {
            throw new IllegalArgumentException("Code are invalid!");
        }

        int x = code.toUpperCase().charAt(0) - 'A';
        int y = 8 - (int)(code.charAt(1) - '0');

        return new Position(x, y);
    }

    private static boolean validateCode(String code)
    {
        if(code.length() != 2) return false;

        final char c1 = code.toUpperCase().charAt(0);
        final char c2 = code.charAt(1);

        return ((int) c1 >= (int) 'A' && (int) c1 <= (int) 'H') &&
               ((int) c2 >= (int) '0' && (int) c2 <= (int) '8');
    }
}
