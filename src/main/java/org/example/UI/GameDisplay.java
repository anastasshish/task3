package org.example.UI;

import org.example.Heroes.Computer;
import org.example.Heroes.Player;

public class GameDisplay {

    public void printMap(GameMap map, Player player, Computer comp) {
        map.printMap(player, comp);
    }

    public void displayStatus(Player player, Computer comp) {
        player.printStatus();
        System.out.println("\nЗолото компьютера: " + comp.getGold());
        System.out.println("\nСостояние компьютера:");
        comp.printStatus();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}