package gui;

import objects.Ball;
import objects.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * Created by Danil on 05.03.2016.
 */
public class FieldComponent extends JPanel {
    private static final int DEFAULT_WIDTH = 450;
    private static final int DEFAULT_HEIGHT = 300;

    private static final double scale = 5.0;

    private java.util.List<Player> players;
    private Ball ball;

    public static final double playerShapeWidth = 25;
    public static final double playerShapeHeight = 25;

    public static final double ballShapeWidth = 15;
    public static final double ballShapeHeight = 15;

    /**
     * Add a player to the component.
     * @param player the player to add
     */
    public void addPlayer(Player player)
    {
        if (players == null) {
            players = new ArrayList<Player>();
        }
        players.add(player);

    }

    /**
     * Add a ball to the component.
     * @param ball the player to add
     */
    public void addBall(Ball ball)
    {
        this.ball = ball;
    }

    /**
     * Gets the shape of the player at its current position.
     */
    public Ellipse2D getPlayerShape(Player player)
    {
        return new Ellipse2D.Double(getPlayerShapePosX(player),
                getPlayerShapePosY(player), playerShapeWidth, playerShapeHeight);
    }

    /**
     * Gets the shape of the ball at its current position.
     */
    public Ellipse2D getBallShape()
    {
        return new Ellipse2D.Double(getBallShapePosX(),
                getBallShapePosY(), ballShapeWidth, ballShapeHeight);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // erase background
        Graphics2D g2 = (Graphics2D) g;
        //сглаживание фигуры при рисовании
        for (Player player: players) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //градиет для игрока, чтобы определить, где лицо
            GradientPaint blackToWhite = new GradientPaint(getPointP1(player), Color.black,
                    getPointP2(player), Color.white);
            g2.setPaint(blackToWhite);
            g2.fill(getPlayerShape(player));
            //обрисовка круга линеей
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(Color.black);
            g2.draw(getPlayerShape(player));
        }
        //рисование мяча
        g2.setColor(Color.black);
        g2.fill(getBallShape());
        //обрисовка круга линеей
        g2.setStroke(new BasicStroke(1f));
        g2.draw(getBallShape());
    }

    public Dimension getPreferredSize() { return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT); }

    public double getPlayerShapePosX(Player player) {
        return player.getPosX()*scale-playerShapeWidth/2 + DEFAULT_WIDTH/2;
    }

    public double getPlayerShapePosY(Player player) {
        return player.getPosY()*scale-playerShapeHeight/2 + DEFAULT_HEIGHT/2;
    }

    public double getBallShapePosX() {return ball.getPosX()*scale-ballShapeWidth/2 + DEFAULT_WIDTH/2;}

    public double getBallShapePosY() {return ball.getPosY()*scale-ballShapeHeight/2 + DEFAULT_HEIGHT/2;}

    /**
     * Вычисление координат первой точки для отрисовки градиента
     * @return точка (тыльная сторона игрока)
     */
    public Point2D getPointP1(Player player) {
        double angle = player.getGlobalBodyAngle();
        double posX = 0;
        double posY = 0;
        if (angle >= 0) {
            if (angle > 90) {
                angle = 180 - angle;
                posX = player.getPosX()*scale+ DEFAULT_WIDTH/2 + playerShapeWidth/2*Math.cos(Math.toRadians(-angle));
                posY = player.getPosY()*scale+ DEFAULT_HEIGHT/2 + playerShapeWidth/2*Math.sin(Math.toRadians(-angle));
            }
            else {
                posX = player.getPosX()*scale+ DEFAULT_WIDTH/2 - playerShapeWidth/2*Math.cos(Math.toRadians(-angle));
                posY = player.getPosY()*scale+ DEFAULT_HEIGHT/2 + playerShapeWidth/2*Math.sin(Math.toRadians(-angle));
            }
        }
        else {
            if (player.getGlobalBodyAngle() < -90) {
                posX = player.getPosX()*scale+ DEFAULT_WIDTH/2 + playerShapeWidth/2*Math.cos(Math.toRadians(180+angle));
                posY = player.getPosY()*scale+ DEFAULT_HEIGHT/2 + playerShapeWidth/2*Math.sin(Math.toRadians(180+angle));
            }
            else {
                posX = player.getPosX()*scale+ DEFAULT_WIDTH/2 - playerShapeWidth/2*Math.cos(Math.toRadians(-angle));
                posY = player.getPosY()*scale+ DEFAULT_HEIGHT/2 + playerShapeWidth/2*Math.sin(Math.toRadians(-angle));
            }
        }
        return new Point2D.Double(posX, posY);
    }

    /**
     * Вычисление координат второй точки для отрисовки градиента
     * @return точка (передная сторона игрока)
     */
    public Point2D getPointP2(Player player) {
        double angle = player.getGlobalBodyAngle();
        double posX = 0;
        double posY = 0;
        if (angle >= 0) {
            if (angle > 90) {
                angle = 180 - angle;
                posX = player.getPosX()*scale+ DEFAULT_WIDTH/2 - playerShapeWidth / 2 * Math.cos(Math.toRadians(angle));
                posY = player.getPosY()*scale+ DEFAULT_HEIGHT/2 + playerShapeWidth / 2 * Math.sin(Math.toRadians(angle));
            } else {
                posX = player.getPosX()*scale+ DEFAULT_WIDTH/2 + playerShapeWidth / 2 * Math.cos(Math.toRadians(angle));
                posY = player.getPosY()*scale+ DEFAULT_HEIGHT/2 + playerShapeWidth / 2 * Math.sin(Math.toRadians(angle));
            }
        } else {
            if (player.getGlobalBodyAngle() < -90) {
                posX = player.getPosX()*scale+ DEFAULT_WIDTH/2 - playerShapeWidth / 2 * Math.cos(Math.toRadians(-180 - angle));
                posY = player.getPosY()*scale+ DEFAULT_HEIGHT/2 + playerShapeWidth / 2 * Math.sin(Math.toRadians(-180 - angle));
            } else {
                posX = player.getPosX()*scale+ DEFAULT_WIDTH/2 + playerShapeWidth / 2 * Math.cos(Math.toRadians(angle));
                posY = player.getPosY()*scale+ DEFAULT_HEIGHT/2 + playerShapeWidth / 2 * Math.sin(Math.toRadians(angle));
            }
        }
        return new Point2D.Double(posX, posY);
    }
}
