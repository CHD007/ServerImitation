package gui;

import logs.Logs;
import objects.*;
import objects.Action;
import server.MyMath;
import server.ServerParameters;
import uml.Manager;
import utils.ActionsEnum;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JFormattedTextField textFieldBallVelocity;
    private JFormattedTextField textFieldBallAngle;
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

    public MainFrame() {
        setFrameSize();
        manager = new Manager();
        logs = new Logs();
        fieldComponent = new FieldComponent();
        fieldComponent.addPlayer(manager.getServerImitator().getServerPlayer());
        fieldComponent.addBall(manager.getServerImitator().getBall());
        createVerticalBox();
    }

    /**
     * Установка размера фрейма
     */
    public void setFrameSize() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth / 2, screenHeight*5 / 8);
        setLocationByPlatform(true);
    }


    public void createVerticalBox() {
        Box box = Box.createVerticalBox();
        box.add(createObjectInfoBox());
        box.add(Box.createVerticalStrut(10));
        box.add(createButtonsBox());
        box.add(Box.createVerticalStrut(10));
        box.add(createFieldBox());
        box.add(Box.createVerticalStrut(10));
        box.add(Box.createVerticalGlue());
        add(box, BorderLayout.NORTH);
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
        startButton = new JButton("Старт");
        startButton.addActionListener(new BtnStartListener());
        box.add(startButton);
        box.add(Box.createHorizontalStrut(10));
        dashButton = new JButton("dash");
        dashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player = manager.getAgentPlayer();
                player.updateBodySense(manager.getServerImitator().sendSenseMessage());
                player.updateSeeSense(manager.getServerImitator().sendSeeMessage());
                FieldObject point = new FieldObject(manager.getServerImitator().getBall().getPosX(), manager.getServerImitator().getBall().getPosY());
                manager.setAction(player.dashToPoint(point));
                System.out.println("dash: currentBallVelocity = " + manager.getAgentPlayer().getCurrentBallVelocity().getX());
                System.out.println("dash: " + manager.getAgentPlayer().predictedBallPosX(10));
                manager.getServerImitator().simulationStep(manager.getAction());
                refreshPlayerInfo();
                refreshBallInfo();
                paintShapes();
                if (manager.getServerImitator().isIntercept()) {
                    JOptionPane.showMessageDialog(null, "Ball is intercepted");
                }
            }
        });
        box.add(dashButton);
        box.add(Box.createHorizontalStrut(10));
        turnButton = new JButton("turn");
        turnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player = manager.getAgentPlayer();
                player.updateBodySense(manager.getServerImitator().sendSenseMessage());
                player.updateSeeSense(manager.getServerImitator().sendSeeMessage());
               /* manager.getAction().setActionType("turn");
                manager.getAction().setMoment(((Number)textFieldPlayerAngle.getValue()).doubleValue());
                manager.getServerImitator().simulationStep(manager.getAction());*/
                FieldObject point = new FieldObject(manager.getServerImitator().getBall().getPosX(), manager.getServerImitator().getBall().getPosY());
                manager.setAction(player.turnBodyToPoint(point));
                manager.getServerImitator().simulationStep(manager.getAction());
                refreshPlayerInfo();
                refreshBallInfo();
                paintShapes();
                if (manager.getServerImitator().isIntercept()) {
                    JOptionPane.showMessageDialog(null, "Ball is intercepted");
                }
            }
        });
        box.add(turnButton);
        box.add(Box.createHorizontalStrut(10));
        stepButton = new JButton("step");
        stepButton.setEnabled(autoSimulationCheckBox.isSelected());
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Player player = manager.getAgentPlayer();
                player.updateBodySense(manager.getServerImitator().sendSenseMessage());
                System.out.println("step: ");
                player.updateSeeSense(manager.getServerImitator().sendSeeMessage());*/
                /*manager.getAction().setActionType("dash");
                double relX = MyMath.relativeX(player.getPosX(), player.getPosY(), 10, 10, player.getGlobalBodyAngle());
                double relY = MyMath.relativeY(player.getPosX(), player.getPosY(), 10, 10, player.getGlobalBodyAngle());
                double power = manager.getAgentPlayer().getPowerForDash(relX,relY);
                System.out.println("power = " + power);
                manager.getAction().setPower(power);*/
                //проверка алгоритма перехвата
                switch ((ActionsEnum)actionComboBox.getSelectedItem()) {
                    case INTERSEPT:
                        Action action = manager.getAgentPlayer().intercept();
                        if (action != null) {
                            manager.getServerImitator().simulationStep(action);
                        } else {
                            JOptionPane.showMessageDialog(null, "Can't intercept the ball: mainFrame");
                        }
                        refreshPlayerInfo();
                        refreshBallInfo();

                        logs.writeLog(manager.getServerImitator().getServerPlayer(), manager.getServerImitator().getBall(), manager.getServerImitator().getTime());
                        paintShapes();
                        if (manager.getServerImitator().isIntercept()) {
                            JOptionPane.showMessageDialog(null, "Ball is intercepted");
                        }
                        break;

                    case PASS:
                        JOptionPane.showMessageDialog(null, "Pass algorithm");
                        break;

                    case DRIBBLING:
                        JOptionPane.showMessageDialog(null, "Dribbling algorithm");
                        break;

                    case MARK_OPPONENT:
                        JOptionPane.showMessageDialog(null, "Mark opponent algorithm");
                        break;
                }
            }
        });
        box.add(stepButton);
        box.add(Box.createHorizontalStrut(10));
        interceptButton = new JButton("Intercept");
        interceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action action;
                while (!manager.getServerImitator().isIntercept()) {
                    action = manager.getAgentPlayer().intercept();
                    if (action != null) {
                        manager.getServerImitator().simulationStep(action);
                    } else {
                        JOptionPane.showMessageDialog(null, "Can't intercept the ball: mainFrame");
                    }
                    refreshPlayerInfo();
                    refreshBallInfo();
                    logs.writeLog(manager.getServerImitator().getServerPlayer(), manager.getServerImitator().getBall(), manager.getServerImitator().getTime());
                    paintShapes();
                }
            }
        });
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
            setPlayerInfo();
            setBallInfo();
            manager.getServerImitator().beforeHalf();
            paintShapes();
            if (manager.getServerImitator().isIntercept()) {
                JOptionPane.showMessageDialog(null, "Ball is intercepted");
            }
        }
    }

    public void setPlayerInfo() {
        Player player = manager.getServerImitator().getServerPlayer();
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
        textFieldPlayerPosX.setValue(manager.getServerImitator().getServerPlayer().getPosX());
        textFieldPlayerPosY.setValue(manager.getServerImitator().getServerPlayer().getPosY());
        textFieldPlayerAngle.setValue(manager.getServerImitator().getServerPlayer().getGlobalBodyAngle());
        textFieldPlayerVelocity.setValue(MyMath.velocityModule(manager.getServerImitator().getServerPlayer().getGlobalVelocity()));
    }

    public void refreshBallInfo() {
        textFieldBallPosX.setValue(manager.getServerImitator().getBall().getPosX());
        textFieldBallPosY.setValue(manager.getServerImitator().getBall().getPosY());
        textFieldBallAngle.setValue(manager.getServerImitator().getBall().getAngle());
        textFieldBallVelocity.setValue(MyMath.velocityModule(manager.getServerImitator().getBall().getGlobalVelocity()));
    }
}
