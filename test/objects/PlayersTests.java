package objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.AdditionalActionParameters;

import java.util.logging.Logger;

public class PlayersTests {
    private static final Logger LOGGER = Logger.getLogger(PlayersTests.class.getName());
    private Player player;

    @Before
    public void initPlayer() {
        player = new Player();
        player.setGlobalBodyAngle(0);
        player.setPosX(0);
        player.setPosY(0);
        player.setCommand(Command.OPPOSSITE);
    }


    @Test
    public void movToPosTest() {
        Action action = player.movToPos(new FieldObject(10, 10));
        Assert.assertEquals("turn", action.getActionType());
    }

    @Test
    public void outplayingOpponentBackActionTest() {
        Action outplayingBackAction = player.outplayingOpponent(new MobileObject(), AdditionalActionParameters.BACK);
        Assert.assertEquals(player.movToPos(new FieldObject(player.getPosX() + 5, player.getPosY())).getMoment(), outplayingBackAction.getMoment(), 0.5);
    }

    /**
     * Тестирование простого блокирования игрока (преградить ему путь к нашим воротам).
     * Т.е. нужно встать на линии заданного игрока и наших ворот.
     */
    @Test
    public void markOpponentTest() {
        Player playerToMark = new Player();
        playerToMark.setPosX(0);
        playerToMark.setPosY(10);
        playerToMark.setGlobalBodyAngle(0);

        Player me = new Player();
        me.setPosX(20);
        me.setPosY(15);
        Action action = me.markOpponent(playerToMark);
        Assert.assertEquals(me.movToPos(new FieldObject(18.4, 6.72)).getMoment(), action.getMoment(), 0.5);
    }

    /**
     * Тестирование действия опеки игрока.
     */
    @Test
    public void markOpponentSimpleTest() {
        Player playerToMark = new Player();
        playerToMark.setPosX(-10);
        playerToMark.setPosY(-10);
        playerToMark.setGlobalBodyAngle(0);
        Action action = player.markOpponent(playerToMark);
        Assert.assertEquals(player.movToPos(new FieldObject(1.3, -8.19)).getMoment(), action.getMoment(), 0.5);
    }
}
