package uml;

import objects.Command;
import objects.Player;
import server.ServerImitator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danil on 27.02.2016.
 */
public class Manager {
    private List<Player> playerList;
    private ServerImitator serverImitator;

    public Manager() {
        serverImitator = new ServerImitator();
        playerList = new ArrayList<>();
        Player agentPlayer0 = new Player();
        agentPlayer0.setCommand(Command.OUR);
        agentPlayer0.setGlobalBodyAngle(0);
        Player agentPlayer1 = new Player();
        agentPlayer1.setCommand(Command.OUR);
        agentPlayer1.setGlobalBodyAngle(0);
        agentPlayer1.setPosX(10);
        agentPlayer1.setPosY(10);
        Player agentPlayer2 = new Player();
        agentPlayer2.setGlobalBodyAngle(-180);
        agentPlayer2.setCommand(Command.OPPOSSITE);
        agentPlayer2.setPosX(10);
        Player agentPlayer3 = new Player();
        agentPlayer3.setGlobalBodyAngle(-180);
        agentPlayer3.setPosX(20);
        agentPlayer3.setPosY(-10);
        agentPlayer3.setCommand(Command.OPPOSSITE);
        playerList.add(agentPlayer0);
        playerList.add(agentPlayer1);
        playerList.add(agentPlayer2);
        playerList.add(agentPlayer3);

        serverImitator.connectToServer(agentPlayer0);
        serverImitator.connectToServer(agentPlayer1);
        serverImitator.connectToServer(agentPlayer2);
        serverImitator.connectToServer(agentPlayer3);
    }

    public ServerImitator getServerImitator() {
        return serverImitator;
    }

    public void setServerImitator(ServerImitator serverImitator) {
        this.serverImitator = serverImitator;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * @return основной игрок для тестирования алгоритмов
     */
    public Player getAgentPlayer() {
        return playerList.get(0);
    }

    /**
     * @return второй основной игрок для тестирования алгоритмов
     */
    public Player getAgentPlayer2() {
        return playerList.get(1);
    }

    /**
     * Добавляет нового игрока и соединяет его с сервером
     */
    public void addPlayer() {
        if (playerList == null) {
            playerList = new ArrayList<>();
        }
        Player player = new Player();
        player.setGlobalBodyAngle(0);
        playerList.add(player);
        serverImitator.connectToServer(player);
    }
}
