package uml;

import objects.Action;
import objects.Player;
import server.ServerImitator;

/**
 * Created by Danil on 27.02.2016.
 */
public class Manager {
    private Player agentPlayer;
    private Action action;
    private ServerImitator serverImitator;

    public Manager() {
        serverImitator = new ServerImitator();
        agentPlayer = new Player();
        agentPlayer.setGlobalBodyAngle(0);
        serverImitator.connectToServer(agentPlayer);
        action = new Action();
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public ServerImitator getServerImitator() {
        return serverImitator;
    }

    public void setServerImitator(ServerImitator serverImitator) {
        this.serverImitator = serverImitator;
    }

    public Player getAgentPlayer() {
        return agentPlayer;
    }
}
