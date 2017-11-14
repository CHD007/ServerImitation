package objects;

import org.junit.Assert;
import org.junit.Test;

import java.util.logging.Logger;

public class PlayersTests {
    private static final Logger LOGGER = Logger.getLogger(PlayersTests.class.getName());

    @Test
    public void movToPosTest() {
        Player player = new Player();
        player.setPosX(0);
        player.setPosY(0);
        Action action = player.movToPos(new FieldObject(10, 10));
        Assert.assertEquals("turn", action.getActionType());
    }
}
