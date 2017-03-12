package uml;

import objects.Action;
import objects.Player;
import server.ServerImitator;

/**
 * Created by Danil on 27.02.2016.
 */
public class Manager {
    private Player agentPlayer;
    private Player agentPlayer2;
    private Action action;
    private Action actionOfSecondPlayer;
    private ServerImitator serverImitator;

    public Manager() {
        serverImitator = new ServerImitator();
        agentPlayer = new Player();
        agentPlayer.setGlobalBodyAngle(0);
        agentPlayer2 = new Player();
        serverImitator.connectToServer(agentPlayer);
        serverImitator.connectToServer(agentPlayer2);
        action = new Action();
        actionOfSecondPlayer = new Action();
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

    public Player getAgentPlayer2() {
        return agentPlayer2;
    }
}
