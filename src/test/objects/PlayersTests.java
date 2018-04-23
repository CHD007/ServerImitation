package objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.MyMath;
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
        Player opponentToOutplay = new Player();
        opponentToOutplay.setCommand(Command.OUR);
        player.addOppositeTeamPlayer(opponentToOutplay);
        Action outplayingBackAction = this.player.outplayingOpponent(new MobileObject(), AdditionalActionParameters.BACK);
        Assert.assertEquals(this.player.movToPos(new FieldObject(this.player.getPosX() + 5, this.player.getPosY())).getMoment(), outplayingBackAction.getMoment(), 0.5);
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
        playerToMark.setGlobalVelocity(null);

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
        playerToMark.setGlobalVelocity(null);
        Action action = player.markOpponent(playerToMark);
        Assert.assertEquals(player.movToPos(new FieldObject(1.3, -8.19)).getMoment(), action.getMoment(), 0.5);
    }

    /**
     * Тестирование метода предсказания позиции игрока после заданного кол-ва циклов
     */
    @Test
    public void predictPlayerPositionAfterNCyclesTest() {
        Player player = new Player();
        player.setPosX(0);
        player.setPosY(0);
        player.setGlobalBodyAngle(45);
        Velocity velocity = new Velocity();
        velocity.setX(0.5);
        velocity.setY(0.5);
        player.setGlobalVelocity(velocity);
        FieldObject fieldObject = player.predictPlayerStateAfterNCycles(player, 3);
        Assert.assertEquals(1.103, MyMath.distance(player, fieldObject), 0.05);
    }

    /**
     * Тест опеки при движении игрока.
     */
    @Test
    public void markOpponentUsingPredictionTest() {
        Player playerToMark = new Player();
        playerToMark.setPosX(0);
        playerToMark.setPosY(0);
        playerToMark.setGlobalBodyAngle(0);
        Velocity velocity = new Velocity();
        velocity.setX(1);
        velocity.setY(0);
        playerToMark.setGlobalVelocity(velocity);

        Player me = new Player();
        me.setPosX(2);
        me.setPosY(2);
        me.setGlobalBodyAngle(-90);
        Action action = me.markOpponent(playerToMark);
        Assert.assertEquals(me.movToPos(new FieldObject(2, 0)).getMoment(), action.getMoment(), 0.5);
    }
}
