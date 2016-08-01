package objects;

/**
 * Created by Danil on 26.03.2016.
 */
public class ServerPlayer extends Player {
    private Player agentPlayer; // ссылка на agentPlayer, подключенного к серверу

    public Player getAgentPlayer() {
        return agentPlayer;
    }

    public void setAgentPlayer(Player agentPlayer) {
        this.agentPlayer = agentPlayer;
    }
}
