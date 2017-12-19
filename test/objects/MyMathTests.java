package objects;

import org.junit.Assert;
import org.junit.Test;
import server.MyMath;
import server.ServerParameters;

import java.util.logging.Logger;

public class MyMathTests {
    private static final Logger LOGGER = Logger.getLogger(MyMathTests.class.getName());

    /**
     * Тест проверки вычисления угла в радианах по теореме косинусов по трем сторонам.
     */
    @Test
    public void cosTheoremTest() {
        Player playerToMark = new Player();
        playerToMark.setPosX(0);
        playerToMark.setPosY(10);
        playerToMark.setGlobalBodyAngle(0);

        Player me = new Player();
        me.setPosX(20);
        me.setPosY(15);
        FieldObject ourGoal = new FieldObject((double) ServerParameters.FIELD_WIDTH / 2.0, 0.0);
        double distanceBetweenOpponentAndOurGoal = MyMath.distance(playerToMark, ourGoal);
        double distanceBetweenOpponentAndMe = MyMath.distance(me, playerToMark);
        double distanceBetweenMeAndOurGoal = MyMath.distance(me, ourGoal);
        double angleBetweenMeAndOurGoalToOpponentInRadians = Math.acos((Math.pow(distanceBetweenOpponentAndOurGoal, 2) +
                Math.pow(distanceBetweenOpponentAndMe, 2) - Math.pow(distanceBetweenMeAndOurGoal, 2))
                / (2 * distanceBetweenOpponentAndOurGoal * distanceBetweenOpponentAndMe));
        Assert.assertEquals(angleBetweenMeAndOurGoalToOpponentInRadians,
                MyMath.getAngleInRadiansByCosTheorem(distanceBetweenOpponentAndOurGoal,
                        distanceBetweenOpponentAndMe, distanceBetweenMeAndOurGoal), 0.5);
    }
}
