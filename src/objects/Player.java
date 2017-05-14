package objects;

import server.MyMath;
import server.SeeMessage;
import server.SenseMessage;
import server.ServerParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danil on 27.02.2016.
 */
public class Player extends MobileObject {
    private static int PLAYERS_NUMBER = 0;
    private Command command;
    private int playerId;

    private double globalBodyAngle;
    private Action action;
    private double effort;
    private double recovery;
    private double stamina;

    //текущие координаты мяча относительно игрока
    private double ballPosX;
    private double ballPosY;

    //текущие глобальные коориднаты мяча
    private double ballGlobalPosX;
    private double ballGlobalPosY;

    //глобальные координаты мяча в предыдущем цикле
    private double oldBallGlobalPosX;
    private double oldBallGlobalPosY;

    //координаты мяча относительно игрока в предыдущем цикле
    private double oldBallPosX;
    private double oldBallPosY;

    private double interceptTurnAngle = 7;
    private double interceptDistanceBack = 2;
    private double interceptMaxCycles = 200;
    private double turnCorrection = 3;

    private boolean ballKicked;

    //глобальная скорость мяча
    private Velocity currentBallVelocity;

    List<Player> oppositeTeamPlayers;

    public Player() {
        playerId = PLAYERS_NUMBER++;
        globalBodyAngle = 0;
        ballPosX = 0;
        ballPosY = 0;
        oldBallPosY = 0;
        oldBallPosY = 0;
        currentBallVelocity = new Velocity();
        ballKicked = false;
        action = new Action();
        oppositeTeamPlayers = new ArrayList<>();
    }

    public Player(Player player) {
        posX = player.getPosX();
        posY = player.getPosY();
        globalVelocity = new Velocity(player.getGlobalVelocity());

        globalBodyAngle = player.getGlobalBodyAngle();
        effort = player.getEffort();
        stamina = player.getStamina();
        recovery = player.getRecovery();

        ballPosX = player.getBallPosX();
        ballPosY = player.getBallPosY();
        oldBallPosX = player.getOldBallPosX();
        oldBallPosY = player.getOldBallPosY();

        currentBallVelocity = new Velocity(player.getCurrentBallVelocity());
    }
    
    public Command getCommand() {
        return command;
    }
    
    public void setCommand(Command command) {
        this.command = command;
    }
    
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void turn(double angle) {
        action.setActionType("turn");
        action.setMoment(angle);
    }

    public void dash(double power) {
        action.setActionType("power");
        action.setPower(power);
    }

    public Velocity getCurrentBallVelocity() {
        return currentBallVelocity;
    }

    public double getEffort() {
        return effort;
    }

    public void setEffort(double effort) {
        this.effort = effort;
    }

    public double getRecovery() {
        return recovery;
    }

    public void setRecovery(double recovery) {
        this.recovery = recovery;
    }

    public double getStamina() {
        return stamina;
    }

    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    public double getGlobalBodyAngle() {
        return globalBodyAngle;
    }

    public void setGlobalBodyAngle(double globalBodyAngle) {
        this.globalBodyAngle = globalBodyAngle;
    }

    public double getBallPosX() {
        return ballPosX;
    }

    public double getBallPosY() {
        return ballPosY;
    }

    public double getOldBallPosY() {
        return oldBallPosY;
    }

    public double getOldBallPosX() {
        return oldBallPosX;
    }

    @Override
    public String toString() {
        return "Player " + playerId;
    }

    /**
     * @param a желаемый угол поворота
     * @return величина угла, которая должна быть передана команде turn для поворота на желаемый угол a
     */
    public double getAngleForTurn(double a) {
        double turnParameter = a*(1.0 + ServerParameters.inertia_moment*MyMath.velocityModule(getGlobalVelocity()));
        if (turnParameter > 180) {
            turnParameter = 180;
        } else {
            if (turnParameter < -180) {
                turnParameter = -180;
            }
        }
        return turnParameter;
    }

    /**
     * Выдает силу для команды dash необходимую для достижения заданной точки (x,y)
     * @param x координата x желаемой точки относительно игрока
     * @param y координата y желаемой точки относительно игрока
     * @return сила рывка для команды dash
     */
    public double getPowerForDash(double x, double y) {
        double distance = Math.min(ServerParameters.player_speed_max, x);
        // НАДО ПРОВЕРИТЬ (угол скорости игрока и его глобальный угол)
        double dashParameter = (distance- MyMath.velocityModule(getGlobalVelocity()))/(ServerParameters.dash_power_rate*effort);
        return MyMath.normalizePower(dashParameter);
    }

    /**
     * Обновление body sense после получения senseMessage
     * @param senseMessage
     */
    public void updateBodySense(SenseMessage senseMessage) {
        setGlobalVelocity(senseMessage.getVelocity());
        setEffort(senseMessage.getEffort());
        setStamina(senseMessage.getStamina());
    }

    /**
     * Обновление визуальнйо информации после получения seeMessage
     * @param seeMessage
     */
    public void updateSeeSense(SeeMessage seeMessage) {
        posX = seeMessage.getPlayerPosX();
        posY = seeMessage.getPlayerPosY();
        globalBodyAngle = seeMessage.getGlobalAngle();
        if (seeMessage.getTime() > 0) {
            oldBallPosX = ballPosX;
            oldBallPosY = ballPosY;
            oldBallGlobalPosX = ballGlobalPosX;
            oldBallGlobalPosY = ballGlobalPosY;
        }
        ballPosX = seeMessage.getBallRelativeX();
        ballPosY = seeMessage.getBallRelativeY();
        ballGlobalPosX = MyMath.unRelativeX(posX, posY, ballPosX, ballPosY, globalBodyAngle);
        ballGlobalPosY = MyMath.unRelativeY(posX, posY, ballPosX, ballPosY, globalBodyAngle);
        if (seeMessage.getTime() > 0) {
            positionBasedVelocityEstimation();
            System.out.println("updateSeeMessage() currentBallVelocityX = " + getCurrentBallVelocity().getX());
        }
        oppositeTeamPlayers = seeMessage.getOppositeTeamPlayers();
    }

    /**
     * Вычисление угла до точки относительно игрока
     * @param point глобальные координаты точки
     * @return относительный угол
     */
    public double getAngleToPointRelativeToPlayer(FieldObject point) {
        double predictedX = getPosX() + getGlobalVelocity().getX();
        double predictedY = getPosY() + getGlobalVelocity().getY();
        double relativeX = MyMath.relativeX(predictedX, predictedY, point.getPosX(), point.getPosY(), getGlobalBodyAngle());
        double relativeY = MyMath.relativeY(predictedX, predictedY, point.getPosX(), point.getPosY(), getGlobalBodyAngle());
        return MyMath.polarAngle(relativeX, relativeY);
    }

    /**
     * Поворот тела к точке с глобальными координатами (x,y)
     * @param point точки куда нужно повернуться с глобальными координатами
     * @return действие, которое нужно совершить, чтобы повернуться к точке с координатми (x,y)
     */
    public Action turnBodyToPoint(FieldObject point) {
        double angle = getAngleForTurn(getAngleToPointRelativeToPlayer(point));
        Action action = new Action();
        action.setActionType("turn");
        action.setMoment(angle);
        return action;
    }

    /**
     * Рывок к точке с глобальными координатами (x,y)
     * @param point точка куда нужно прибежать с глобальными координатами
     * @return действие, которое нужно совершить, чтобы прибежатьы к точке с координатми (x,y)
     */
    public Action dashToPoint(FieldObject point) {
        double relativeX = MyMath.relativeX(getPosX(), getPosY(), point.getPosX(), point.getPosY(), getGlobalBodyAngle());
        double relativeY = MyMath.relativeY(getPosX(), getPosY(), point.getPosX(), point.getPosY(), getGlobalBodyAngle());
        double power = getPowerForDash(relativeX, relativeY);
        Action action = new Action();
        action.setActionType("dash");
        action.setPower(power);
        return action;
    }

    /**
     * Оценка скорости мяча на основе данных о его предыдущем расположении и текущем
     */
    public void positionBasedVelocityEstimation() {
        currentBallVelocity.setX((ballGlobalPosX - oldBallGlobalPosX)*ServerParameters.ball_decay);
        currentBallVelocity.setY((ballGlobalPosY - oldBallGlobalPosY)*ServerParameters.ball_decay);
    }

    /**
     * Предполагаемая глобальная координата x мяча
     * @param n
     * @return Предполагаемая координата x мяча относительно игрока в цикле t+n
     */
    public double predictedBallPosX(int n) {
        System.out.println("prediectedBallPosX() ballPasX = " + ballPosX);
        System.out.println("prediectedBallPosX() currentBallVelocityX = " + getCurrentBallVelocity().getX());
        System.out.println("prediectedBallPosX() predictedBallPasX = " + (ballGlobalPosX + currentBallVelocity.getX()*(1-Math.pow(ServerParameters.ball_decay, n))/(1-ServerParameters.ball_decay)));
        return ballGlobalPosX + currentBallVelocity.getX()*(1-Math.pow(ServerParameters.ball_decay, n))/(1-ServerParameters.ball_decay);
    }

    public double predictedBallPosY(int n) {
        return ballGlobalPosY+ currentBallVelocity.getY()*(1-Math.pow(ServerParameters.ball_decay, n))/(1-ServerParameters.ball_decay);
    }

    /**
     * Предполагаемая позиция игрока (глобальная) в цикле t+n, если он не выполняет не каких действий, а движется по инерции
     * @param n цикл t+n
     * @return предполагаемая позиция игрока в цикле t+n
     */
    public FieldObject playerPredictedGlobalPosition(int n) {
        FieldObject playerPredictedPosition = new FieldObject();
        playerPredictedPosition.setPosX(posX + globalVelocity.getX()*(1-Math.pow(ServerParameters.player_decay, n))/(1-ServerParameters.player_decay));
        playerPredictedPosition.setPosY(posY + globalVelocity.getY()*(1-Math.pow(ServerParameters.player_decay, n))/(1-ServerParameters.player_decay));
        return playerPredictedPosition;
    }

    /**
     * Предполагаемая позиция мяча (глобальная) в цикле t+n
     * @param n цикл t+n
     * @return предполагаемая позиция игрока в цикле t+n
     */
    public FieldObject ballPredictedGlobalPosition(int n) {
        FieldObject ballPredictedPosition = new FieldObject();

        ballPredictedPosition.setPosX(predictedBallPosX(n));
        ballPredictedPosition.setPosY(predictedBallPosY(n));
        return ballPredictedPosition;
    }

    public int predictNrCyclesToPoint(FieldObject point) {
        int n = (int)((MyMath.distance(this, point)/ServerParameters.player_speed_max)
                + Math.abs((getAngleToPointRelativeToPlayer(point))/turnCorrection));
        return n;
    }

    /**
     * Вычисление действительного угла поворота
     * @param moment желаемый угол поворота
     * @return действительный угол поворота
     */
    public double predictedActAngle(double moment) {
        double playerSpeed = MyMath.velocityModule(getGlobalVelocity());
        return moment/(1.0 + ServerParameters.inertia_moment * playerSpeed);
    }

    /**
     * Изменение скорости игрока в зависимости от действия, которое он исполняет (dash)
     * @param player игрок, для которого нужно изменить скорость
     * @param action действие, в котором находится сила рывка
     */
    public void predictedPlayerVelocity(Player player, Action action) {
        Velocity oldVelocity = player.getGlobalVelocity();
        action.setPower(MyMath.normalizePower(action.getPower()));
        Velocity acceleration = predictedAcceleration(action.getPower(), player.getGlobalBodyAngle());
        player.getGlobalVelocity().setX(oldVelocity.getX()+acceleration.getX());
        player.getGlobalVelocity().setY(oldVelocity.getY()+acceleration.getY());
        if (MyMath.velocityModule(player.getGlobalVelocity()) > ServerParameters.player_speed_max) {
            MyMath.normalizeVector(player.getGlobalVelocity());
        }
    }

    /**
     * Вычисление действительной силы, зависящей от запаса сил игрока.
     * @param power сила, с которой игрок хочет увеличить скорость
     * @return действительная сила
     */
    public double predictedActPower(double power) {
        return power*getEffort();
    }

    /**
     * Вычисление вектора ускорения
     * @param power сила, с которой игрок хочет увеличить скорость
     * @param angle глобольный угол игрока, либо мяча
     * @return вектор ускорения
     */
    public Velocity predictedAcceleration(double power, double angle) {
        Velocity acceleration = new Velocity();
        acceleration.setX(predictedActPower(power)* ServerParameters.dash_power_rate*Math.cos(Math.toRadians(angle)));
        acceleration.setY(predictedActPower(power)* ServerParameters.dash_power_rate*Math.sin(Math.toRadians(angle)));
        if (MyMath.velocityModule(acceleration) > ServerParameters.player_accel_max) {
            MyMath.normalizeVector(acceleration);
        }
        return acceleration;
    }

    /**
     * Метод, который возвращает предсказанную позицию игрока после выполнения команды cmd
     * @param player позиция, относительно которой идет предсказание
     * @param cmd действие, выполняемое игроком
     * @return
     */
    public Player predictedPlayerStateAfterAction(Player player, Action cmd) {
        Player playerAfterAction = new Player(player);
        switch (cmd.getActionType()) {
            case "turn":
                double angle = player.getGlobalBodyAngle()+predictedActAngle(action.getMoment());
                if (angle > 180) {
                    angle = -360+angle;
                }
                else {
                    if (angle < -180) {
                        angle = 360 + angle;
                    }
                }
                playerAfterAction.setGlobalBodyAngle(angle);
                playerAfterAction.setPosX(playerAfterAction.getPosX() + playerAfterAction.getGlobalVelocity().getX());
                playerAfterAction.setPosY(playerAfterAction.getPosY() + playerAfterAction.getGlobalVelocity().getY());
                break;
            case "dash":
                predictedPlayerVelocity(playerAfterAction, cmd);
                playerAfterAction.setPosX(playerAfterAction.getPosX() + playerAfterAction.getGlobalVelocity().getX());
                playerAfterAction.setPosY(playerAfterAction.getPosY() + playerAfterAction.getGlobalVelocity().getY());
                break;
        }
        return playerAfterAction;
    }

    /**
     * Возвращает желаемую точку перехвата с глобальными координатами
     * @return желаемая точка перехвата, null - если ее нет
     */
    public FieldObject getDesiredInterceptionPoint() {
        //y = kx + c - уравнение прямой l проходящей через центр игрока в направление поворота его тела
        //(x-a)^2 + (y-b)^2 = R^2 - уравнение окружности с с радиусом = Rигрока/2 + Rмяча/2 + kickable_margin/6
        //Вычисление предполагаемой точки игрока
        FieldObject playerPredictedPosition = playerPredictedGlobalPosition(1);
        //вычисление уголового коэффициента прямой l
        double k = Math.atan(Math.toRadians(globalBodyAngle));
        //вычисление с
        double c = -k*playerPredictedPosition.getPosX() + playerPredictedPosition.getPosY();
        //Вычисление предполагаемой точки мяча в цикле t+1
        FieldObject ballPredictedPosition = ballPredictedGlobalPosition(1);
        //Вычисление радиуса окружности c
        double R = ServerParameters.ball_size + ServerParameters.player_size + ServerParameters.kickable_margin/6;
        //Параметр a уравнения окружности (x-a)^2 + (y-b)^2 = R^2
        double a = ballPredictedPosition.getPosX();
        //Параметр b уравнения окружности (x-a)^2 + (y-b)^2 = R^2
        double b = ballPredictedPosition.getPosY();
        /*
        Точки пересечения прямой y = kx + c и окружности (x-a)^2 + (y-b)^2 = R^2 можно найти, решив систему уравнений:
            |y = kx + c,
            |(x-a)^2 + (y-b)^2 = R^2
        Найдем дискрименант уравнения, полученного после небольших преобразований этой системы уравнений
         */
        double D = Math.pow(2*k*(c-b)-2*a, 2) - 4*(k*k+1)*(a*a+Math.pow(c-b, 2)-R*R);

        if (D == 0) { // одна точка пересечения
            FieldObject desiredInterceptionPoint = new FieldObject();
            double x = -b/2/a;
            desiredInterceptionPoint.setPosX(x);
            double y = k*x + c;
            desiredInterceptionPoint.setPosY(y);
            return desiredInterceptionPoint;
        }
        if (D > 0) { // две точки пересечения
            FieldObject point1 = new FieldObject();
            double x = (-b + Math.sqrt(D))/2/a;
            point1.setPosX(x);
            double y = k*x + c;
            point1.setPosY(y);
            FieldObject point2 = new FieldObject();
            x = (-b - Math.sqrt(D))/2/a;
            point1.setPosX(x);
            y = k*x + c;
            point1.setPosY(y);
            //Выбрать в качетсве точки перехвата ту, у которой абсолютрный угол между ней и центром окружности c меньше
            double relPosXP1 = MyMath.relativeX(playerPredictedPosition.getPosX(), playerPredictedPosition.getPosY(), point1.getPosX(), point1.getPosY(), globalBodyAngle);
            double relPosXP2 = MyMath.relativeX(playerPredictedPosition.getPosX(), playerPredictedPosition.getPosY(), point2.getPosY(), point2.getPosY(), globalBodyAngle);
            if (relPosXP1 < relPosXP2) {
                return point1;
            } else {
                return point2;
            }
        }
        return null;
    }

    public Action closeIntercept(double kickableDistance) {
        FieldObject interceptPoint = getDesiredInterceptionPoint();
        //Попытка перехватить мяч за один цикл с использованием одного dash
        if (interceptPoint != null) {
            Player playerAfterDash = predictedPlayerStateAfterAction(this, dashToPoint(interceptPoint));
            System.out.println("colseIntercept() interceptPointX = " + interceptPoint.getPosX());
            System.out.println("colseIntercept() interceptPointY = " + interceptPoint.getPosY());
            if (MyMath.distance(playerAfterDash, ballPredictedGlobalPosition(1)) < kickableDistance) {
                return dashToPoint(interceptPoint);
            }
        }

        //Попытка перехватить мяч за два цикла с использованием dash, а затем turn
        FieldObject ballPredictedGlobalPosition2 = ballPredictedGlobalPosition(2);
        Player playerAfterTurn = predictedPlayerStateAfterAction(this, turnBodyToPoint(ballPredictedGlobalPosition2));
        Player playerAfterDash = predictedPlayerStateAfterAction(playerAfterTurn, dashToPoint(ballPredictedGlobalPosition2));
        if (MyMath.distance(playerAfterDash, ballPredictedGlobalPosition2) < kickableDistance) {
            return turnBodyToPoint(ballPredictedGlobalPosition2);
        }

        //Попытка перехватить мяч за два цикла с использованием двух dash
        Player playerAfterSecondDash = predictedPlayerStateAfterAction(playerAfterDash, dashToPoint(ballPredictedGlobalPosition2));
        if (MyMath.distance(playerAfterSecondDash, ballPredictedGlobalPosition2) < kickableDistance) {
            return dashToPoint(ballPredictedGlobalPosition2);
        }

        System.out.println("closeIntercept() desiredPosition = null");
        return null;
    }

    /**
     * Перемещение к заданной точке
     * @param point точка, к которой нужно подбежать
     * @return одна из команд последовательности команд, необходимых для достижения заданной точки
     */
    public Action movToPos(FieldObject point) {
        double angle = getAngleToPointRelativeToPlayer(point);
        double dist = MyMath.distance(this, point);
        if ((Math.abs(angle) < interceptTurnAngle) ||
                (Math.abs(MyMath.normalizeAngle(angle + interceptTurnAngle)) < interceptTurnAngle & dist < interceptDistanceBack)) {
            return dashToPoint(point);
        } else {
            return turnBodyToPoint(point);
        }
    }

    public Action intercept() {
        double dist = ServerParameters.ball_size + ServerParameters.player_size + ServerParameters.kickable_margin;
        Action action = closeIntercept(dist);
        if (action != null) {
            return action;
        }
        int i = 1;
        int n;
        FieldObject ballPosAfterNCycles;
        do {
            i++;
            ballPosAfterNCycles = ballPredictedGlobalPosition(i);
            n = predictNrCyclesToPoint(ballPosAfterNCycles);
            System.out.println("n = " + n);
        } while (!(n < i || i > interceptMaxCycles));
        if (i <= interceptMaxCycles) {
            System.out.println("ballPosXrelativ = " + ballPosX);
            System.out.println("ballPosYrelativ = " + ballPosY);
            System.out.println("pridictedBallPosX[" + i + "] = " + ballPosAfterNCycles.getPosX());
            System.out.println("pridictedBallPosY[" + i + "] = " + ballPosAfterNCycles.getPosY());
            return movToPos(ballPosAfterNCycles);
        } else {
            return null;
        }
    }

    /**
     * Пас партнеру
     * @param x глобальная координата x точки, в которую нужно ударить мяч
     * @param y глобальная координата y точки, в которую нужно ударить мяч
     * @param circles кол-во циклов симуляции
     * @return
     */
    public Action pass(double x, double y, int circles) {
        double relativeX = MyMath.relativeX(posX, posY, x, y, globalBodyAngle);
        double relativeY = MyMath.relativeY(posX, posY, x, y, globalBodyAngle);

        // расстояние между игроком и точкой, в которую нужно ударить мяч
        double distanseBetweenPlayerAndPointForPass = Math.sqrt(Math.pow(Math.abs(x-posX), 2)
                + Math.pow(Math.abs(y - posY), 2));
        // скорость мяча после удара, такая, что мяч достигне указанную точку за circles циклов
        double velocityModule = (distanseBetweenPlayerAndPointForPass * (1 - ServerParameters.ball_decay)) / (1 - Math.pow(ServerParameters.ball_decay, circles));
        // угол между мячом и направлением поворота тела игрока
        FieldObject ball = new FieldObject(ballPosX, ballPosY);
        double angleBetweenTheBallAndPlayer = getAngleToPointRelativeToPlayer(ball);
        double distanceBetwennPlayerAndBall = MyMath.distance(this, ball);
        // действительная сила удара
        double actPower = ServerParameters.kick_power_rate * (1 - 0.25*angleBetweenTheBallAndPlayer/180
                - 0.25*distanceBetwennPlayerAndBall/ServerParameters.kickable_margin);
        // сила удара, передваемая команде kick
        double kickPower = velocityModule/actPower;

//        double angleForKick = Math.toDegrees(Math.acos(relativeX/distanseBetweenPlayerAndPointForPass));
        double angleForKick = MyMath.polarAngle(relativeX, relativeY);

        Action action = new Action();
        action.setPower(kickPower);
        action.setMoment(angleForKick);
        action.setActionType("kick");
        return action;
    }
    
    /**
     * Находит последнего защитника противоположной команды
     * @return последний защитник
     */
    public Player getLastDefender() {
        oppositeTeamPlayers.sort((p1, p2) -> -Double.valueOf(p1.getPosX()).compareTo(p2.getPosX()));
        return oppositeTeamPlayers.get(0);
    }

    /**
     * Проверяет, находится ли мяч в kickable area.
     * Команда kick исполняется, только когда расстояние между центром мяча и центром игрока
     * минус радиус игрока и радиус мяча лежит в пределах от 0 до kickable_margin.
     * @return true - если мяч находится в kickable area, false - инача
     */
    private boolean isBallKickableForPlayer() {
        FieldObject ball = new FieldObject(ballPosX, ballPosY);
        return  MyMath.distance(this, ball) <
                (ServerParameters.ball_size + ServerParameters.player_size + ServerParameters.kickable_margin);
    }

    public Action dribbling(double x, double y, int circles) {
        if (isBallKickableForPlayer() && !ballKicked) {
            ballKicked = true;
            return pass(x, y, circles-1);
        } else {
            Action action = new Action();
            action.setActionType("dash");
            action.setPower(getPowerForDash(x, y));
            return action;
        }
    }
}
