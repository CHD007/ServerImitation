package server;

import objects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Класс для имитации сервера RoboCup Soccer
 */
public class ServerImitator {
    private Ball ball;
    private int time; //цикл симуляции
    private List<ServerPlayer> serverPlayers; //игроки, подключенные к серверу.

    /**
     * Перед началом тайма восстановить состояние игрока
     */
    public void beforeHalf() {
        time = 0;
        for (ServerPlayer serverPlayer: serverPlayers) {
            serverPlayer.setEffort(ServerParameters.effort_max);
            serverPlayer.setRecovery(ServerParameters.recover_max);
            serverPlayer.setStamina(ServerParameters.stamina_max);
        }
    }

    public ServerImitator() {
        ball = new Ball();
        serverPlayers = new ArrayList<>();
        beforeHalf();
    }

    public void connectToServer(Player agentPlayer) {
        ServerPlayer serverPlayer = new ServerPlayer();
        if (agentPlayer.getPlayerId() == 1) {
            serverPlayer.setPosX(10);
        }
        serverPlayer.setAgentPlayer(agentPlayer);
        serverPlayers.add(serverPlayer);
    }

    /**
     * Находит по id игрока, подключенного к серверу
     * @param id id игрока, которого нужно найти
     * @return игрок, если он найден, в противном случае - null
     */
    public Player findPlayerById(int id) {
        for (ServerPlayer serverPlayer: serverPlayers) {
            if (serverPlayer.getAgentPlayer().getPlayerId() == id) {
                return serverPlayer;
            }
        }
        return null;
    }

    /**
     * Цикл моделирования
     */
    public void simulationStep() {
        for (ServerPlayer serverPlayer: serverPlayers) {
            //исполнение желаемого действия игрока
            Action action = serverPlayer.getAgentPlayer().getAction();
            if (action != null && action.getActionType() != null) {
                System.out.println("simulationStep() action = " + action.getActionType());
                action(serverPlayer, action);
            }
            //перемещение объектов
            changePosition(serverPlayer);
            changePosition(ball);
            //замедление скорости
            speedDecay(serverPlayer, ServerParameters.player_decay);
            speedDecay(ball, ServerParameters.ball_decay);
            //восстановление запаса сил
            staminaRecovery(serverPlayer);
            //посылка sense и see сообщения
            serverPlayer.getAgentPlayer().updateBodySense(sendSenseMessage(serverPlayer));
            System.out.println("serverImitator: ");
            serverPlayer.getAgentPlayer().updateSeeSense(sendSeeMessage(serverPlayer));
            //проверка методов предсказания игроком позиции мяча
       /* double pX = MyMath.unRelativeX(serverPlayer.getPosX(), serverPlayer.getPosY(),
                serverPlayer.getAgentPlayer().predictedBallPosX(1), serverPlayer.getAgentPlayer().predictedBallPosY(1), serverPlayer.getGlobalBodyAngle());
        double pY = MyMath.unRelativeY(serverPlayer.getPosX(), serverPlayer.getPosY(),
                serverPlayer.getAgentPlayer().predictedBallPosX(1), serverPlayer.getAgentPlayer().predictedBallPosY(1), serverPlayer.getGlobalBodyAngle());

        System.out.println("currentBallPosX = " + ball.getPosX());
        System.out.println("currentBallPosY = " + ball.getPosY());
        System.out.println("predictedBallPosX = " + pX);
        System.out.println("predictedBallPosY = " + pY);*/
        /*double predictedBallPosXrelative = serverPlayer.getAgentPlayer().predictedBallPosX(1);
        double predictedBallPosYrelative = serverPlayer.getAgentPlayer().predictedBallPosY(1);
        System.out.println("Old relative pos x = " + serverPlayer.getAgentPlayer().getOldBallPosX());
        System.out.println("Old relative pos y = " + serverPlayer.getAgentPlayer().getOldBallPosY());
        System.out.println("Current relative pos x = " + serverPlayer.getAgentPlayer().getBallPosX());
        System.out.println("Current relative pos y = " + serverPlayer.getAgentPlayer().getBallPosY());
        System.out.println("Predicted relative pos x = " + predictedBallPosXrelative);
        System.out.println("Predicted relative pos y = " + predictedBallPosYrelative);*/
            time++;
        }
    }

    /**
     * Действие action, которое совершает игрок player в данном цикле
     * @param player игрок, который совершает действие
     * @param action действие, которое совершает игрок
     */
    public void action(Player player, Action action) {
        if (action != null) {
            switch (action.getActionType()) {
                case "turn":
                    changePlayerGlobalAngle(player, action);
                    break;
                case "dash":
                    changePlayerVelocity(player, action);
                    break;
                case "kick":

                    break;
            }
        }
    }

    /**
     * Проверяет, находится ли мяч в kickable area.
     * Команда kick исполняется, только когда расстояние между центром мяча и центром игрока
     * минус радиус игрока и радиус мяча лежит в пределах от 0 до kickable_margin.
     * @param player игрок, для которого проверяется, находится ли мяч в kickable area
     * @return true - если мяч находится в kickable area, false - инача
     */
    private boolean isBallKickableForPlayer(Player player) {
        return  MyMath.distance(player, ball) <
                (ServerParameters.ball_size + ServerParameters.player_size + ServerParameters.kickable_margin);
    }

    /**
     * Действительная сила удара, в зависимости от взаимного расположения игрока и мяча
     * @param player игрок, выполняющий удар по мячу
     * @param power сила, с которой игрок хочет ударить по мячу
     * @return действительная сила удара
     */
    private double actPowerForKick(Player player, double power) {
        // угол между мячом и направлением поворота тела игрока
        double angleBetweenTheBallAndPlayer = player.getAngleToPointRelativeToPlayer(ball);
        // расстояние между мячом и игроком
        double distanceBetweenTheBallAndPlayer = MyMath.distance(player, ball);
        return power * (1 - 0.25*(angleBetweenTheBallAndPlayer/180) - 0.25*(distanceBetweenTheBallAndPlayer/ServerParameters.kickable_margin));
    }

    /**
     * Ускорение мяча после удара
     * @param player игрок, совершающий удар
     * @param power сила, с которой игрок хочет ударить по мячу
     * @param angle угол, в направлении которого игрок хочет ударить мяч
     * @return ускорение мяча после удара
     */
    private Velocity ballAccelerationAfterKick(Player player, double power, double angle) {
        Velocity acceleration = new Velocity();
        acceleration.setX(actPowerForKick(player, power) * ServerParameters.kick_power_rate * Math.cos(Math.toRadians(angle)) + kkmax(kmax(power)));
        acceleration.setY(actPowerForKick(player, power) * ServerParameters.kick_power_rate * Math.sin(Math.toRadians(angle)) + kkmax(kmax(power)));
        if (MyMath.velocityModule(acceleration) > ServerParameters.ball_accel_max) {
            MyMath.normalizeVector(acceleration);
        }
        return acceleration;
    }

    /**
     * Изменение скорости мяча после удара
     * @param player игрок, выполняющий удра
     * @param power сила, с которой игрок хочет ударить по мячу
     * @param angle угол, в направлении которого игрок хочет ударить по мячу
     */
    private void changeBallVelocity(Player player, double power, double angle) {
        Velocity oldBallVelocity = ball.getGlobalVelocity();
        Velocity acceleration = ballAccelerationAfterKick(player, power, angle);
        double rrmax = rrmax(rmax(ball.getGlobalVelocity()));
        ball.getGlobalVelocity().setX(oldBallVelocity.getX() + acceleration.getX() + rrmax);
        ball.getGlobalVelocity().setY(oldBallVelocity.getY() + acceleration.getY() + rrmax);
        if (MyMath.velocityModule(ball.getGlobalVelocity()) > ServerParameters.ball_speed_max) {
            MyMath.normalizeVector(ball.getGlobalVelocity());
        }
    }

    /**
     * Вычисление действительной силы, зависящей от запаса сил игрока.
     * @param power сила, с которой игрок хочет увеличить скорость
     * @return действительная сила
     */
    public double actPower(Player player, double power) {
        return power*player.getEffort();
    }

    /**
     * Вычисление вектора ускорения
     * @param player игрок, ускорение которого вычисляется
     * @param power сила, с которой игрок хочет увеличить скорость
     * @param angle глобольный угол игрока, либо мяча
     * @return вектор ускорения
     */
    public Velocity acceleration(Player player, double power, double angle) {
        Velocity acceleration = new Velocity();
        acceleration.setX(actPower(player, power)* ServerParameters.dash_power_rate*Math.cos(Math.toRadians(angle)));
        acceleration.setY(actPower(player, power)* ServerParameters.dash_power_rate*Math.sin(Math.toRadians(angle)));
        if (MyMath.velocityModule(acceleration) > ServerParameters.player_accel_max) {
            MyMath.normalizeVector(acceleration);
        }
        return acceleration;
    }

    /**
     * Изменение скорости игрока в зависимости от действия, которое он исполняет (dash)
     * @param player игрок, для которого нужно изменить скорость
     * @param action действие, в котором находится сила рывка
     */
    public void changePlayerVelocity(Player player, Action action) {
        Velocity oldVelocity = player.getGlobalVelocity();
        action.setPower(MyMath.normalizePower(action.getPower()));
        changeStamina(player, action);
        Velocity acceleration = acceleration(player, action.getPower(), player.getGlobalBodyAngle());
        double rrmax = rrmax(rmax(player.getGlobalVelocity()));
        player.getGlobalVelocity().setX(oldVelocity.getX()+acceleration.getX()+rrmax);
        player.getGlobalVelocity().setY(oldVelocity.getY()+acceleration.getY()+rrmax);
        if (MyMath.velocityModule(player.getGlobalVelocity()) > ServerParameters.player_speed_max) {
            MyMath.normalizeVector(player.getGlobalVelocity());
        }
    }

    /**
     * Уменьшает запас сил игрока в зависимости от силы рывка
     * @param serverPlayer игрок, стамина которого уменьшается
     * @param action дейсвия с силой рывка
     */
    public void changeStamina(Player serverPlayer, Action action) {
        double power = action.getPower();
        if (Math.abs(power) < serverPlayer.getStamina()) {
            if (power > 0) {
                serverPlayer.setStamina(serverPlayer.getStamina() - Math.abs(power));
            }
            else {
                serverPlayer.setStamina(serverPlayer.getStamina() - 2*Math.abs(power));
            }
        }
        else {
            action.setPower(serverPlayer.getStamina());
            serverPlayer.setStamina(0);
        }
    }

    /**
     * Модель изменения запаса сил, восстановления и эффективности восстановления
     * @param serverPlayer игрок, стамина которого изменятеся
     */
    public void staminaRecovery(Player serverPlayer) {
        // Если запас сил ниже порога уменьшения восстановления, восстановление понижается
        if (serverPlayer.getStamina() <= ServerParameters.recover_dec_thr* ServerParameters.stamina_max) {
            if (serverPlayer.getRecovery() > ServerParameters.recover_min) {
                serverPlayer.setRecovery(serverPlayer.getRecovery() - ServerParameters.recover_dec);
            }
        }

        // Если запас сил ниже порога уменьшения эффективности, то эффективность понижается
        if (serverPlayer.getStamina() <= ServerParameters.effort_dec_thr* ServerParameters.stamina_max) {
            if (serverPlayer.getEffort() > ServerParameters.effort_min) {
                serverPlayer.setEffort(serverPlayer.getEffort() - ServerParameters.effort_dec);
            }
            else {
                serverPlayer.setEffort(Math.max(serverPlayer.getEffort(), ServerParameters.effort_min));
            }
        }

        // Если запас сил выше порога увелечения эффективности, то эффективность увеличивается
        if (serverPlayer.getStamina() >= ServerParameters.effort_inc_thr* ServerParameters.stamina_max) {
            if (serverPlayer.getEffort() < ServerParameters.effort_max) {
                serverPlayer.setEffort(serverPlayer.getEffort() + ServerParameters.effort_inc);
                serverPlayer.setEffort(Math.min(serverPlayer.getEffort(), ServerParameters.effort_max));
            }
        }

        // Немного восстановить запасс сил
        serverPlayer.setStamina(serverPlayer.getStamina() + serverPlayer.getRecovery()* ServerParameters.stamina_inc_max);
        serverPlayer.setStamina(Math.min(serverPlayer.getStamina(), ServerParameters.stamina_max));
    }

    /**
     * Уменьшение скорости
     * @param mobileObject
     * @param decay
     */
    public void speedDecay(MobileObject mobileObject, double decay) {
        mobileObject.getGlobalVelocity().setX(mobileObject.getGlobalVelocity().getX()*decay);
        mobileObject.getGlobalVelocity().setY(mobileObject.getGlobalVelocity().getY()*decay);
    }

    /**
     * Граничные значения из промежутка [-rmax;rmax], из которого выбирается шум для перемещения
     * @param velocity скорость, от которой зависит граничн
     * @return rmax
     */
    public double rmax(Velocity velocity) {
        return ServerParameters.player_rand*Math.sqrt(velocity.getX()*velocity.getX()+velocity.getY()*velocity.getY());
    }

    /**
     * @param rmax граничные значения
     * @return Шум к перемещению
     */
    public double rrmax(double rmax) {
        Random rnd = new Random(System.currentTimeMillis());
        double rrmax = -rmax + rnd.nextDouble()*(2*rmax);
        return rrmax;
    }

    /**
     * Граничные значения из промежутка [-kmax;kmax], из которого выбирается шум для удара
     * @param power сила, с которой игрок хочет ударить по мячу
     * @return kmax
     */
    private double kmax(double power) {
        return ServerParameters.kick_rand*(power/ServerParameters.maxpower);
    }

    /**
     * @param kmax граничные значения
     * @return Шум к удару
     */
    private double kkmax(double kmax) {
        Random rnd = new Random(System.currentTimeMillis());
        double kkmax = -kmax + rnd.nextDouble()*(2*kmax);
        return kkmax;
    }


    /**
     * Перемещение объектов
     * @param mobileObject объект для перемещения
     */
    public void changePosition(MobileObject mobileObject) {
        mobileObject.setPosX(mobileObject.getPosX()+mobileObject.getGlobalVelocity().getX());
        mobileObject.setPosY(mobileObject.getPosY()+mobileObject.getGlobalVelocity().getY());
    }

    /**
     * Вычисление действительного угла поворота
     * @param serverPlayer игрок, действительный угол поворота которого вычисляется
     * @param moment желаемый угол поворота
     * @return действительный угол поворота
     */
    public double actAngle(Player serverPlayer, double moment) {
        double playerSpeed = MyMath.velocityModule(serverPlayer.getGlobalVelocity());
        return moment/(1.0 + ServerParameters.inertia_moment * playerSpeed);
    }

    /**
     * Изменение глобального угла поворота тела игрока
     * @param player игрок, чей угол будет изменен
     * @param action действие, на основе которого будет изменен угол
     */
    public void changePlayerGlobalAngle (Player player, Action action) {
        action.setMoment(MyMath.normalizeAngle(action.getMoment()));
        double angle = player.getGlobalBodyAngle()+actAngle(player, action.getMoment());
        if (angle > 180) {
            angle = -360+angle;
        }
        else {
            if (angle < -180) {
                angle = 360 + angle;
            }
        }
        player.setGlobalBodyAngle(angle);
    }

    public List<ServerPlayer> getServerPlayers() {
        return serverPlayers;
    }

    public Ball getBall() {
        return ball;
    }

    public boolean isIntercept() {
        for (ServerPlayer serverPlayer : serverPlayers){
            if (MyMath.distance(serverPlayer, ball) < ServerParameters.player_size + ServerParameters.ball_size + ServerParameters.kickable_margin + 1) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public SenseMessage sendSenseMessage(Player serverPlayer) {
        SenseMessage senseMessage = new SenseMessage();
        senseMessage.setVelocity(serverPlayer.getGlobalVelocity());
        senseMessage.setEffort(serverPlayer.getEffort());
        senseMessage.setStamina(serverPlayer.getStamina());
        return senseMessage;
    }

    public SeeMessage sendSeeMessage(Player serverPlayer) {
        SeeMessage seeMessage = new SeeMessage();
        seeMessage.setPlayerPosX(serverPlayer.getPosX());
        seeMessage.setPlayerPosY(serverPlayer.getPosY());
        seeMessage.setGlobalAngle(serverPlayer.getGlobalBodyAngle());
        seeMessage.setBallRelativeX(MyMath.relativeX(serverPlayer.getPosX(), serverPlayer.getPosY(), ball.getPosX(), ball.getPosY(), serverPlayer.getGlobalBodyAngle()));
        seeMessage.setBallRelativeY(MyMath.relativeY(serverPlayer.getPosX(), serverPlayer.getPosY(), ball.getPosX(), ball.getPosY(), serverPlayer.getGlobalBodyAngle()));
        seeMessage.setTime(time);
        return seeMessage;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
