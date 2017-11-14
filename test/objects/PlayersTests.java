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
}
