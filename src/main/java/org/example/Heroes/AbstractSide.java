package org.example.Heroes;

import org.example.Enum.BuildingType;
import org.example.Enum.Side;
import org.example.Enum.UnitType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.Serializable;

public abstract class AbstractSide implements Serializable {
    private static final long serialVersionUID = 1L;
    protected final Side side;
    protected int heroX, heroY;
    public int gold;
    protected Set<BuildingType> buildings;
    protected List<ArmyGroup> groups;

    public AbstractSide(Side side, int sx, int sy) {
        this.side = side;
        this.heroX = sx;
        this.heroY = sy;
        this.gold = 200;
        this.buildings = new HashSet<>();
        this.buildings.add(BuildingType.WATCHTOWER);
        this.groups = new ArrayList<>();
        ArmyGroup g = new ArmyGroup(UnitType.SPEARMAN, heroX, heroY, side);
        g.addUnit(new Unit(UnitType.SPEARMAN));
        this.groups.add(g);
    }

    public Side getSide() {
        return side;
    }

    public int getHeroX() {
        return heroX;
    }

    public int getHeroY() {
        return heroY;
    }

    public void setHeroXY(int x, int y) {
        this.heroX = x;
        this.heroY = y;
    }

    public int getGold() {
        return gold;
    }

    public Set<BuildingType> getBuildings() {
        return buildings;
    }

    public List<ArmyGroup> getGroups() {
        return groups;
    }

    public List<Unit> getArmy() {
        List<Unit> all = new ArrayList<>();
        for (ArmyGroup ag : groups) {
            all.addAll(ag.getUnits());
        }
        return all;
    }

    public void printStatus() {
        System.out.println("\n=== Статус " + side + " ===");
        System.out.println("Герой: (" + heroX + "," + heroY + "), Gold=" + gold);
        System.out.println("Постройки: " + buildings);
        System.out.println("Группы войск:");
        for (ArmyGroup ag : groups) {
            System.out.println(" - " + ag.getType() + " @(" + ag.getX() + "," + ag.getY()
                    + "), size=" + ag.getUnits().size());
            for (Unit u : ag.getUnits()) {
                System.out.println("     Юнит: " + u.getType() + ", HP=" + u.getHp());
            }
        }
    }
}
