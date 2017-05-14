package gui;

import logs.Logs;
import objects.Action;
import objects.*;
import server.MyMath;
import server.ServerParameters;
import uml.Manager;
import utils.ActionsEnum;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;

/**
 * Created by Danil on 05.03.2016.
 */
public class MainFrame extends JFrame {
    private JFormattedTextField textFieldPlayerPosX;
    private JFormattedTextField textFieldPlayerPosY;
    private JFormattedTextField textFieldPlayerVelocity;
    private JFormattedTextField textFieldPlayerAngle;
    private JFormattedTextField textFieldBallPosX;
    private JFormattedTextField textFieldBallPosY;
    private JFormattedTextField textFieldPointForPassPosX;
    private JFormattedTextField textFieldPointForPassPosY;
    private JFormattedTextField textFieldTactsNumber;
    private JFormattedTextField textFieldBallVelocity;
    private JFormattedTextField textFieldBallAngle;
    private JComboBox<Player> playersComboBox;
    private JComboBox actionComboBox;
    private JCheckBox autoSimulationCheckBox;
    private JButton startButton;
    private JButton dashButton;
    private JButton turnButton;
    private JButton stepButton;
    private JButton interceptButton;
    private FieldComponent fieldComponent;

    private Manager manager;
    private Logs logs;

    private final static int WIDHT = 1000;
    private final static int HEIGHT = 700;

    public MainFrame() {
        setFrameSize();
        manager = new Manager();
        logs = new Logs();
        fieldComponent = new FieldComponent();
        java.util.List<ServerPlayer> serverPlayers = manager.getServerImitator().getServerPlayers();
        for (Player player: serverPlayers) {
            fieldComponent.addPlayer(player);
        }
        fieldComponent.addBall(manager.getServerImitator().getBall());
        createVerticalBox();
    }

    /**
     * Установка размера фрейма
     */
    public void setFrameSize() {
        setSize(WIDHT, HEIGHT);
    }


    public void createVerticalBox() {
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(10));
        box.add(createPlayersSelectionBox());
        box.add(Box.createVerticalStrut(10));
        box.add(createObjectInfoBox());
        box.add(Box.createVerticalStrut(10));
        box.add(createInitialDataBox());
        box.add(Box.createVerticalStrut(10));
        box.add(createButtonsBox());
        box.add(Box.createVerticalStrut(10));
        box.add(createFieldBox());
        box.add(Box.createVerticalStrut(10));
        box.add(Box.createVerticalGlue());
        add(box, BorderLayout.NORTH);
    }

    /**
     * Перывый блок с комбобоксом игроков
     */
    private Box createPlayersSelectionBox() {
        Box box = Box.createHorizontalBox();
        initializePlayersComboBox();
        box.add(playersComboBox);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    public Box createObjectInfoBox() {
        Box box1 = Box.createHorizontalBox();
        textFieldPlayerPosX = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldPlayerPosX.setColumns(4);
        textFieldPlayerPosY = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldPlayerPosY.setColumns(4);
        textFieldPlayerVelocity = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldPlayerVelocity.setColumns(4);
        textFieldPlayerAngle = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldPlayerAngle.setColumns(6);
        refreshPlayerInfo();
        box1.add(objectInfoBox("Состояние игрока", textFieldPlayerPosX, textFieldPlayerPosY,
                textFieldPlayerVelocity, textFieldPlayerAngle));
        textFieldBallPosX = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldBallPosX.setColumns(4);
        textFieldBallPosY = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldBallPosY.setColumns(4);
        textFieldBallVelocity = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldBallVelocity.setColumns(4);
        textFieldBallAngle = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldBallAngle.setColumns(6);
        refreshBallInfo();
        box1.add(Box.createHorizontalStrut(10));
        box1.add(objectInfoBox("Состояние мяча", textFieldBallPosX, textFieldBallPosY,
                textFieldBallVelocity, textFieldBallAngle));
        return box1;
    }

    public Box createInitialDataBox() {
        Box box1 = Box.createHorizontalBox();
        textFieldPointForPassPosX = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldPointForPassPosX.setColumns(4);
        textFieldPointForPassPosY = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldPointForPassPosY.setColumns(4);
        textFieldTactsNumber = new JFormattedTextField(NumberFormat.getNumberInstance());
        textFieldTactsNumber.setColumns(4);
        box1.add(initialDataBox("Параметры для выбранного действия", textFieldPointForPassPosX,
                textFieldPointForPassPosY, textFieldTactsNumber));
        return box1;
    }

    public Box objectInfoBox(String objectName, JFormattedTextField x, JFormattedTextField y,
                             JFormattedTextField speed, JFormattedTextField angle) {
        Box objectInfoBox = Box.createHorizontalBox();
        TitledBorder title = BorderFactory.createTitledBorder(objectName);
        objectInfoBox.setBorder(title);

        objectInfoBox.add(new JLabel("X: "));
        objectInfoBox.add(x);

        objectInfoBox.add(Box.createHorizontalStrut(10));
        objectInfoBox.add(new JLabel("Y: "));
        objectInfoBox.add(y);

        objectInfoBox.add(Box.createHorizontalStrut(10));
        objectInfoBox.add(new JLabel("Speed: "));
        objectInfoBox.add(speed);

        objectInfoBox.add(Box.createHorizontalStrut(10));
        objectInfoBox.add(new JLabel("Angle: "));
        objectInfoBox.add(angle);

        return objectInfoBox;
    }

    /**
     * Создает Box для исходный данных одного из действий
     * @param objectName название этого бокса
     * @param x поле для ввода x координаты точки
     * @param y поле для ввода y координаты точки
     * @param tactsNumber поле для ввода кол-ва тактов, за которое мяч должен достичь заданной точки
     * @return
     */
    private Box initialDataBox(String objectName, JFormattedTextField x, JFormattedTextField y,
                               JFormattedTextField tactsNumber) {
        Box objectInfoBox = Box.createHorizontalBox();
        TitledBorder title = BorderFactory.createTitledBorder(objectName);
        objectInfoBox.setBorder(title);

        objectInfoBox.add(new JLabel("X: "));
        objectInfoBox.add(x);

        objectInfoBox.add(Box.createHorizontalStrut(10));
        objectInfoBox.add(new JLabel("Y: "));
        objectInfoBox.add(y);

        objectInfoBox.add(Box.createHorizontalStrut(10));
        objectInfoBox.add(new JLabel("Кол-во тактов: "));
        objectInfoBox.add(tactsNumber);
        return objectInfoBox;
    }

    private boolean validateInitialDataBox() {
        if (textFieldPointForPassPosX.getValue() == null) {
            JOptionPane.showMessageDialog(null, "Не заполнено поле x для выбранного действия");
            return false;
        } else if (textFieldPointForPassPosY.getValue() == null) {
            JOptionPane.showMessageDialog(null, "Не заполнено поле y для выбранного действия");
            return false;
        } else if (textFieldTactsNumber.getValue() == null) {
            JOptionPane.showMessageDialog(null,
                    "Не заполнено поле \"Кол-во тактов\" для выбранного действия");
            return false;
        }
        return true;
    }

    private void initializeActionComboBox() {
        actionComboBox = new JComboBox();
        for (ActionsEnum actionsEnum: ActionsEnum.values()) {
            actionComboBox.addItem(actionsEnum);
        }
        actionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void initializePlayersComboBox() {
        playersComboBox = new JComboBox<Player>();
        for (Player player: manager.getPlayerList()) {
            playersComboBox.addItem(player);
        }

        playersComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                refreshPlayerInfo();
            }
        });
    }

    private Box createButtonsBox() {
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalStrut(10));
        initializeActionComboBox();
        box.add(actionComboBox);
        box.add(Box.createHorizontalStrut(10));

        autoSimulationCheckBox = new JCheckBox("Выполнение по шагам");
        autoSimulationCheckBox.setSelected(false);
        autoSimulationCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stepButton.setEnabled(autoSimulationCheckBox.isSelected());
            }
        });
        box.add(autoSimulationCheckBox);
        box.add(Box.createHorizontalStrut(10));

        startButton = new JButton("Подготовить");
        startButton.setToolTipText("Установка начального положения игроков и мяча");
        startButton.addActionListener(new BtnStartListener());
        box.add(startButton);
        box.add(Box.createHorizontalStrut(10));
        dashButton = new JButton("dash");
        dashButton.addActionListener(new BtnDashListener());
        box.add(dashButton);
        box.add(Box.createHorizontalStrut(10));

        turnButton = new JButton("turn");
        turnButton.addActionListener(new BtnTurnListener());
        box.add(turnButton);
        box.add(Box.createHorizontalStrut(10));

        stepButton = new JButton("step");
        stepButton.setEnabled(autoSimulationCheckBox.isSelected());
        stepButton.addActionListener(new BtnStepListener());
        box.add(stepButton);
        box.add(Box.createHorizontalStrut(10));

        interceptButton = new JButton("Intercept");
        interceptButton.addActionListener(new BtnRunAlgorithmListener());
        box.add(interceptButton);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    private Box createFieldBox() {
        Box box = Box.createHorizontalBox();
        fieldComponent.setBorder(BorderFactory.createLineBorder(Color.black));
        fieldComponent.setMaximumSize(fieldComponent.getPreferredSize());
        box.add(Box.createHorizontalStrut(10));
        box.add(fieldComponent);
        box.add(Box.createHorizontalStrut(10));
        return box;
    }

    public void paintShapes() {
            fieldComponent.paint(fieldComponent.getGraphics());
    }

    public class BtnStartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Player player = (Player) playersComboBox.getSelectedItem();
            setPlayerInfo(manager.getServerImitator().findPlayerById(player.getPlayerId()));
            setBallInfo();
            updatePlayersWordState();
            manager.getServerImitator().beforeHalf();
            paintShapes();
            if (manager.getServerImitator().isIntercept()) {
                JOptionPane.showMessageDialog(null, "Ball is intercepted");
            }
        }
    }
    
    /**
     * Обновление информации о мире для каждого игрока
     */
    private void updatePlayersWordState() {
        manager.getPlayerList().forEach(p -> {
            p.updateSeeSense(manager.getServerImitator().sendSeeMessage(p));
            p.updateBodySense(manager.getServerImitator().sendSenseMessage(p));
        });
    }

    public class BtnDashListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Player player = (Player) playersComboBox.getSelectedItem();
            updatePlayersWordState();
            FieldObject point = new FieldObject(manager.getServerImitator().getBall().getPosX(), manager.getServerImitator().getBall().getPosY());
            player.setAction(player.dashToPoint(point));
            System.out.println("dash: currentBallVelocity = " + player.getCurrentBallVelocity().getX());
            System.out.println("dash: " + player.predictedBallPosX(10));
            manager.getServerImitator().simulationStep();
            refreshPlayerInfo();
            refreshBallInfo();
            paintShapes();
            if (manager.getServerImitator().isIntercept()) {
                JOptionPane.showMessageDialog(null, "Ball is intercepted");
            }
        }
    }

    public class BtnTurnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Player player = (Player) playersComboBox.getSelectedItem();
            updatePlayersWordState();
            FieldObject point = new FieldObject(manager.getServerImitator().getBall().getPosX(), manager.getServerImitator().getBall().getPosY());
            player.setAction(player.turnBodyToPoint(point));
            manager.getServerImitator().simulationStep();
            refreshPlayerInfo();
            refreshBallInfo();
            paintShapes();
            if (manager.getServerImitator().isIntercept()) {
                JOptionPane.showMessageDialog(null, "Ball is intercepted");
            }
        }
    }

    public class BtnStepListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //проверка алгоритма перехвата
            Player agentPlayer = (Player) playersComboBox.getSelectedItem();
            switch ((ActionsEnum)actionComboBox.getSelectedItem()) {
                case INTERSEPT:
//                    Action action = manager.getAgentPlayer().intercept();
                    Action action = agentPlayer.intercept();
                    agentPlayer.setAction(action);
                    if (action != null) {
                        manager.getServerImitator().simulationStep();
                    } else {
                        JOptionPane.showMessageDialog(null, "Can't intercept the ball: mainFrame");
                    }
                    refreshPlayerInfo();
                    refreshBallInfo();

                    logs.writeLog(manager.getServerImitator().getServerPlayers().get(0), manager.getServerImitator().getBall(), manager.getServerImitator().getTime());
                    paintShapes();
                    if (manager.getServerImitator().isIntercept()) {
                        JOptionPane.showMessageDialog(null, "Ball is intercepted");
                    }
                    break;

                case PASS:
                    if (validateInitialDataBox()) {
                        Number pointX = (Number)textFieldPointForPassPosX.getValue();
                        Number pointY = (Number)textFieldPointForPassPosY.getValue();
                        Number circles = (Number)textFieldTactsNumber.getValue();
                        agentPlayer.setAction(agentPlayer.pass(pointX.doubleValue(), pointY.doubleValue(), circles.intValue()));
                        manager.getServerImitator().simulationStep();
                        paintShapes();
                        refreshBallInfo();
                    }
                    break;

                case DRIBBLING:
                    if (validateInitialDataBox()) {
                        Number pointX = (Number)textFieldPointForPassPosX.getValue();
                        Number pointY = (Number)textFieldPointForPassPosY.getValue();
                        Number circles = (Number)textFieldTactsNumber.getValue();
                        Action action1 = agentPlayer.dribbling(pointX.doubleValue(), pointY.doubleValue(), circles.intValue());
                        agentPlayer.setAction(action1);
                        manager.getServerImitator().simulationStep();
                        paintShapes();
                        refreshBallInfo();
                        refreshPlayerInfo();
                    }
                    break;
                    
                case KEEP_TO_OFFSIDE:
                    agentPlayer.setAction(agentPlayer.keepInLineWithLastDefender());
                    manager.getServerImitator().simulationStep();
                    paintShapes();
                    refreshBallInfo();
                    refreshPlayerInfo();
                    break;
                    
                case DASH:
                    if (validateInitialDataBox()) {
                        Number pointX = (Number)textFieldPointForPassPosX.getValue();
                        Number pointY = (Number)textFieldPointForPassPosY.getValue();
                        agentPlayer.setAction(agentPlayer.movToPos(new FieldObject(pointX.doubleValue(), pointY.doubleValue())));
                        manager.getServerImitator().simulationStep();
                        paintShapes();
                        refreshBallInfo();
                        refreshPlayerInfo();
                    }
                    break;
                    
                case MARK_OPPONENT:
                    JOptionPane.showMessageDialog(null, "Mark opponent algorithm");
                    break;
                    
                case NOTHING:
                    agentPlayer.setAction(null);
                    break;
            }
            updatePlayersWordState();
        }
    }

    public class BtnRunAlgorithmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //проверка алгоритма перехвата
            Player agentPlayer = (Player) playersComboBox.getSelectedItem();
            switch ((ActionsEnum)actionComboBox.getSelectedItem()) {
                case INTERSEPT:
                    Action action;
                    while (!manager.getServerImitator().isIntercept()) {
                        action = agentPlayer.intercept();
                        agentPlayer.setAction(action);
//                        manager.getAgentPlayer2().setAction(manager.getAgentPlayer2().intercept());
                        if (action != null) {
                            manager.getServerImitator().simulationStep();
                        } else {
                            JOptionPane.showMessageDialog(null, "Can't intercept the ball: mainFrame");
                        }
                        refreshPlayerInfo();
                        refreshBallInfo();
                        logs.writeLog(manager.getServerImitator().getServerPlayers().get(0), manager.getServerImitator().getBall(), manager.getServerImitator().getTime());
                        paintShapes();
                    }
                    break;

                case PASS:
                    if (validateInitialDataBox()) {
                        if (manager.getServerImitator().getBall().isZeroVelocity()) {
//                            Action action1 = new Action();
//                            action1.setActionType("kick");
//                            action1.setPower(100);
//                            action1.setMoment(-45);
//                            manager.getAgentPlayer().setAction(action1);
//                            manager.getServerImitator().simulationStep();
//                            paintShapes();
                            Number pointX = (Number)textFieldPointForPassPosX.getValue();
                            Number pointY = (Number)textFieldPointForPassPosY.getValue();
                            Number circles = (Number)textFieldTactsNumber.getValue();
                            agentPlayer.setAction(agentPlayer.pass(pointX.doubleValue(), pointY.doubleValue(), circles.intValue()));
                            manager.getServerImitator().simulationStep();
                            paintShapes();
                        }
                        while (!manager.getServerImitator().getBall().isZeroVelocity()) {
                            agentPlayer.setAction(null);
                            manager.getServerImitator().simulationStep();
                            paintShapes();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    break;

                case DRIBBLING:
                    JOptionPane.showMessageDialog(null, "Dribbling algorithm");
                    break;
                
                case KEEP_TO_OFFSIDE:
                    agentPlayer.getLastDefender();
                    break;

                case MARK_OPPONENT:
                    JOptionPane.showMessageDialog(null, "Mark opponent algorithm");
                    break;
            }
        }
    }

    public void setPlayerInfo(Player player) {
        Number number = (Number)textFieldPlayerPosX.getValue();
        player.setPosX(number.doubleValue());
        number = (Number)textFieldPlayerPosY.getValue();
        player.setPosY(number.doubleValue());
        number = (Number)textFieldPlayerAngle.getValue();
        player.setGlobalBodyAngle(number.doubleValue());

        number = (Number)textFieldPlayerVelocity.getValue();
        setVelocity(player, player.getGlobalBodyAngle(), number.doubleValue());
        if (MyMath.velocityModule(player.getGlobalVelocity()) > ServerParameters.player_speed_max) {
            MyMath.normalizeVector(player.getGlobalVelocity());
        }
    }

    public void setBallInfo() {
        Ball ball = manager.getServerImitator().getBall();
        Number number = (Number)textFieldBallPosX.getValue();
        ball.setPosX(number.doubleValue());
        number = (Number)textFieldBallPosY.getValue();
        ball.setPosY(number.doubleValue());
        number = (Number)textFieldBallAngle.getValue();
        ball.setAngle(number.doubleValue());

        number = (Number)textFieldBallVelocity.getValue();
        setVelocity(ball, ball.getAngle(), number.doubleValue());
        if (MyMath.velocityModule(ball.getGlobalVelocity()) > ServerParameters.ball_speed_max) {
            MyMath.normalizeVector(ball.getGlobalVelocity());
        }
    }


    /**
     * Задать скорость по x и y на основе данных об угле вектора скорости и его модуле
     * @param object объект, для которого будет задаваться скорость
     * @param angle угол направления вектора скорости
     * @param velocity модуль вектора скорости
     */
    public void setVelocity(MobileObject object, double angle, double velocity) {
        if (angle >= 0) {
            if (angle > 90) {
                angle = 180 - angle;
                object.getGlobalVelocity().setX(velocity*Math.cos(Math.toRadians(angle)));
                object.getGlobalVelocity().setY(velocity*Math.sin(Math.toRadians(angle)));
            }
            else {
                object.getGlobalVelocity().setX(velocity*Math.cos(Math.toRadians(angle)));
                object.getGlobalVelocity().setY(velocity*Math.sin(Math.toRadians(angle)));
            }
        }
        else {
            if (angle < -90) {
                object.getGlobalVelocity().setX(-velocity*Math.cos(Math.toRadians(-180-angle)));
                object.getGlobalVelocity().setY(velocity*Math.sin(Math.toRadians(-180-angle)));
            }
            else {
                object.getGlobalVelocity().setX(velocity*Math.cos(Math.toRadians(angle)));
                object.getGlobalVelocity().setY(velocity*Math.sin(Math.toRadians(angle)));
            }
        }
    }

    public void refreshPlayerInfo() {
        Player player = (Player) playersComboBox.getSelectedItem();
        Player playerToView = manager.getServerImitator().findPlayerById(player.getPlayerId());
        textFieldPlayerPosX.setValue(playerToView.getPosX());
        textFieldPlayerPosY.setValue(playerToView.getPosY());
        textFieldPlayerAngle.setValue(playerToView.getGlobalBodyAngle());
        textFieldPlayerVelocity.setValue(MyMath.velocityModule(playerToView.getGlobalVelocity()));
    }

    public void refreshBallInfo() {
        textFieldBallPosX.setValue(manager.getServerImitator().getBall().getPosX());
        textFieldBallPosY.setValue(manager.getServerImitator().getBall().getPosY());
        textFieldBallAngle.setValue(manager.getServerImitator().getBall().getAngle());
        textFieldBallVelocity.setValue(MyMath.velocityModule(manager.getServerImitator().getBall().getGlobalVelocity()));
    }
}
