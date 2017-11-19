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
        player.setCommand(Command.OUR);
    }


    @Test
    public void movToPosTest() {
        Action action = player.movToPos(new FieldObject(10, 10));
        Assert.assertEquals("turn", action.getActionType());
    }

    @Test
    public void outplayingOpponentBackActionTest() {
        Action outplayingBackAction = player.outplayingOpponent(new MobileObject(), AdditionalActionParameters.BACK);
        Assert.assertEquals(player.movToPos(new FieldObject(player.getPosX() - 5, player.getPosY())), outplayingBackAction);
    }

    /**
     * Тестирование простого блокирования игрока (преградить ему путь к нашим воротам).
     * Т.е. нужно встать на линии заданного игрока и наших ворот.
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
