package uml;

import objects.Action;
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
        Player agentPlayer = new Player();
        agentPlayer.setGlobalBodyAngle(0);
        Player agentPlayer2 = new Player();
        agentPlayer2.setGlobalBodyAngle(0);
        playerList.add(agentPlayer);
        playerList.add(agentPlayer2);

        serverImitator.connectToServer(agentPlayer);
        serverImitator.connectToServer(agentPlayer2);
    }

    public ServerImitator getServerImitator() {
        return serverImitator;
    }

    public void setServerImitator(ServerImitator serverImitator) {
        this.serverImitator = serverImitator;
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
