package logs;

import objects.Ball;
import objects.Player;
import server.MyMath;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Danil on 16.05.2016.
 */
public class Logs {
    private String fileName;

    public Logs() {}

    public Logs(String fileName) {
        this.fileName = fileName;
    }

    public void writeLog(Player player, Ball ball, int time) {
        try(FileWriter writer = new FileWriter("C:\\logs\\logs.txt", true))
        {
            String cycle = "Time: " + time;
            writer.write(cycle);
            writer.append('\n');
            // запись информации об игроке
            String playerLog = "Player: " + " x: " + player.getPosX() + " Y: " + player.getPosY() + " speed: "
                    + MyMath.velocityModule(player.getGlobalVelocity()) + " angle: " + player.getGlobalBodyAngle();
            writer.write(playerLog);
            writer.append('\n');
            // запись информации об мяче
            String ballInfo = "Ball: " + " x: " + ball.getPosX() + " Y: " + ball.getPosY() + " speed: "
                    + MyMath.velocityModule(ball.getGlobalVelocity()) + " angle: " + ball.getAngle();
            writer.write(ballInfo);
            writer.append('\n');
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
