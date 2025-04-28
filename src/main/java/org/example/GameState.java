package org.example;

import org.example.Heroes.Computer;
import org.example.Heroes.Player;
import org.example.UI.GameMap;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;
    private Player player;
    private Computer computer;
    private GameMap map;
    private long elapsedTime;

    public GameState(String pName, Player p, Computer c, GameMap m) {
        this.playerName = pName;
        this.player = p;
        this.computer = c;
        this.map = m;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Player getPlayer() {
        return player;
    }

    public Computer getComputer() {
        return computer;
    }

    public GameMap getMap() {
        return map;
    }
}
