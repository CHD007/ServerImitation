package server;

import objects.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danil on 26.03.2016.
 */
public class SeeMessage {
    private int time; // номер цикла симуляции
    private double playerPosX; // глобальная координата x игрока
    private double playerPosY; // глобальная координата y игрока
    private double globalAngle; // глобальный угол игрока
    protected double ballRelativeX; //x координата мяча в системе координат игрока
    protected double ballRelativeY; //y координата мяча в системе координат игрока

    public SeeMessage() {

    }

    public SeeMessage(double ballRelativeX, double ballRelativeY) {
        this.ballRelativeX = ballRelativeX;
        this.ballRelativeY = ballRelativeY;
    }
    
    public double getBallRelativeX() {
        return ballRelativeX;
    }

    public void setBallRelativeX(double ballRelativeX) {
        this.ballRelativeX = ballRelativeX;
    }

    public double getBallRelativeY() {
        return ballRelativeY;
    }

    public void setBallRelativeY(double ballRelativeY) {
        this.ballRelativeY = ballRelativeY;
    }

    public double getPlayerPosX() {
        return playerPosX;
    }

    public void setPlayerPosX(double playerPosX) {
        this.playerPosX = playerPosX;
    }

    public double getPlayerPosY() {
        return playerPosY;
    }

    public void setPlayerPosY(double playerPosY) {
        this.playerPosY = playerPosY;
    }

    public double getGlobalAngle() {
        return globalAngle;
    }

    public void setGlobalAngle(double globalAngle) {
        this.globalAngle = globalAngle;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
