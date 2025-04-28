package org.example.UI;

import org.example.Heroes.ArmyGroup;
import org.example.Heroes.Computer;
import org.example.Enum.CellType;
import org.example.Enum.Side;
import org.example.Enum.UnitType;
import org.example.Heroes.Player;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class GameMap implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    private final CellType[][] grid;

    public GameMap() {
        grid = new CellType[HEIGHT][WIDTH];
        initDefaultMap();
    }

    public GameMap(boolean allNeutral) {
        grid = new CellType[HEIGHT][WIDTH];
        if (allNeutral) {
            initAllNeutral();
        } else {
            initDefaultMap();
        }
    }

    private void initDefaultMap() {

        for (int y = 0; y < HEIGHT; y++) {
            Arrays.fill(grid[y], CellType.NEUTRAL);
        }

        fillArea(0, 0, 2, 2, CellType.COMPUTER_ZONE);
        grid[1][1] = CellType.COMPUTER_CASTLE;
        fillArea(7, 7, 9, 9, CellType.PLAYER_ZONE);
        grid[8][8] = CellType.PLAYER_CASTLE;
        for (int i = 3; i < 7; i++) {
            grid[i][i] = CellType.ROAD;
        }
    }

    private void initAllNeutral() {
        for (int y = 0; y < HEIGHT; y++) {
            Arrays.fill(grid[y], CellType.NEUTRAL);
        }
    }

    private void fillArea(int x1, int y1, int x2, int y2, CellType type) {
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                grid[y][x] = type;
            }
        }
    }

    public boolean isMoveValid(int x, int y, Side side) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public CellType getCellType(int x, int y) {
        return grid[y][x];
    }

    public void setCellType(int x, int y, CellType type) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            System.out.println("Координата (" + x + "," + y + ") вне карты!");
            return;
        }
        grid[y][x] = type;
    }

    public void clearCell(int x, int y) {
        setCellType(x, y, CellType.NEUTRAL);
    }

    public void printMap(Player p, Computer c) {
        System.out.println("\n=== Текущее состояние карты ===");
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {


                if (p != null && row == p.getHeroY() && col == p.getHeroX()) {
                    System.out.print("H ");
                    continue;
                }

                if (c != null && row == c.getHeroY() && col == c.getHeroX()) {
                    System.out.print("h ");
                    continue;
                }


                ArmyGroup pg = (p != null) ? findGroupAt(p.getGroups(), col, row) : null;
                if (pg != null) {
                    System.out.print(getPlayerSymbol(pg.getType()) + " ");
                    continue;
                }


                ArmyGroup cg = (c != null) ? findGroupAt(c.getGroups(), col, row) : null;
                if (cg != null) {
                    System.out.print(getCompSymbol(cg.getType()) + " ");
                    continue;
                }


                switch (grid[row][col]) {
                    case ROAD:
                        System.out.print("· ");
                        break;
                    case NEUTRAL:
                        System.out.print("x ");
                        break;
                    case PLAYER_ZONE:
                        System.out.print("& ");
                        break;
                    case COMPUTER_ZONE:
                        System.out.print("$ ");
                        break;
                    case PLAYER_CASTLE:
                        System.out.print("И ");
                        break;
                    case COMPUTER_CASTLE:
                        System.out.print("К ");
                        break;
                    case BATTLE:
                        System.out.print("⚔ ");
                        break;

                }
            }
            System.out.println();
        }
    }

    public static ArmyGroup findGroupAt(List<ArmyGroup> list, int cx, int cy) {
        if (list == null) return null;
        for (ArmyGroup g : list) {
            if (g.getX() == cx && g.getY() == cy) {
                return g;
            }
        }
        return null;
    }



    public static String getPlayerSymbol(UnitType t) {
        switch (t) {
            case SPEARMAN:
                return "S";
            case ARCHER:
                return "A";
            case SWORDSMAN:
                return "W";
            case CAVALRY:
                return "C";
            case PALADIN:
                return "P";
        }
        return "?";
    }

    public static String getCompSymbol(UnitType t) {
        switch (t) {
            case SPEARMAN:
                return "s";
            case ARCHER:
                return "a";
            case SWORDSMAN:
                return "w";
            case CAVALRY:
                return "c";
            case PALADIN:
                return "p";
        }
        return "?";
    }
}
