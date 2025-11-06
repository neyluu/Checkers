package checkers;

import checkers.game.utils.Position;
import checkers.game.utils.PositionCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PositionCodeTest
{
    @Test
    public void constructorInts_1()
    {
        PositionCode positionCode = new PositionCode(0, 0);
        String code = positionCode.getCode();
        assertEquals("A8", code);
    }

    @Test
    public void constructorInts_2()
    {
        PositionCode positionCode = new PositionCode(5, 2);
        String code = positionCode.getCode();
        assertEquals("F6", code);
    }

    @Test
    public void constructorPosition_1()
    {
        PositionCode positionCode = new PositionCode(new Position(0, 0));
        String code = positionCode.getCode();
        assertEquals("A8", code);
    }

    @Test
    public void constructorPosition_2()
    {
        PositionCode positionCode = new PositionCode(new Position(3, 3));
        String code = positionCode.getCode();
        assertEquals("D5", code);
    }

    @Test
    public void setCodeInts_1()
    {
        PositionCode positionCode = new PositionCode();
        positionCode.setCode(1, 2);
        assertEquals("B6", positionCode.getCode());
    }

    @Test
    public void setCodePosition_1()
    {
        PositionCode positionCode = new PositionCode();
        positionCode.setCode(new Position(1, 2));
        assertEquals("B6", positionCode.getCode());
    }

    @Test
    public void multipleSetting()
    {
        PositionCode positionCode = new PositionCode();
        positionCode.setCode(0, 7);
        positionCode.setCode(7, 0);
        assertEquals("H8", positionCode.getCode());
    }

    @Test
    public void toCodeInts_1()
    {
        assertEquals("A8", PositionCode.toCode(0,0));
    }

    @Test
    public void toCodeInts_2()
    {
        assertEquals("H1", PositionCode.toCode(7,7));
    }

    @Test
    public void toCodeInts_3()
    {
        assertEquals("E3", PositionCode.toCode(4, 5));
    }

    @Test
    public void toCodePosition_1()
    {
        assertEquals("C2", PositionCode.toCode(new Position(2,6)));
    }

    @Test
    public void getPosition_1()
    {
        PositionCode positionCode = new PositionCode(0,0);
        assertEquals(new Position(0, 0).toString(), positionCode.getPosition().toString());
    }

    @Test
    public void getPosition_2()
    {
        PositionCode positionCode = new PositionCode(3,5);
        assertEquals(new Position(3, 5).toString(), positionCode.getPosition().toString());
    }
    @Test
    public void getPosition_3()
    {
        PositionCode positionCode = new PositionCode(7,7);
        assertEquals(new Position(7, 7).toString(), positionCode.getPosition().toString());
    }

    @Test
    public void fromCode_1()
    {
        Position position = PositionCode.fromCode("A1");
        assertEquals(new Position(0, 7).toString(), position.toString());
    }

    @Test
    public void fromCode_2()
    {
        Position position = PositionCode.fromCode("c2");
        assertEquals(new Position(2, 6).toString(), position.toString());
    }

    @Test
    public void fromCode_3()
    {
        Position position = PositionCode.fromCode("H1");
        assertEquals(new Position(7, 7).toString(), position.toString());
    }
}
