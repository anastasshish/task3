package org.example.Heroes;

import org.example.Enum.BuildingType;
import org.example.Enum.Side;
import org.example.Enum.UnitType;

import java.io.Serializable;

public class Player extends AbstractSide implements Serializable {
    private static final long serialVersionUID = 1L;
    private String playerName;

    public Player(int sx, int sy) {
        super(Side.PLAYER, sx, sy);
    }

    public boolean spendGold(int amt) {
        if (gold >= amt) {
            gold -= amt;
            return true;
        }
        return false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public boolean buildStructure(BuildingType b) {
        if (buildings.contains(b)) {
            System.out.println("Здание уже построено: " + b);
            return false;
        }
        int cost = getBuildingCost(b);
        if (!spendGold(cost)) {
            System.out.println("Недостаточно золота!");
            return false;
        }
        buildings.add(b);
        System.out.println("Построено: " + b + " (cost=" + cost + ")");
        return true;
    }

    private int getBuildingCost(BuildingType b) {
        return switch (b) {
            case WATCHTOWER -> 10;
            case CROSSBOW_TOWER -> 15;
            case ARMORY -> 20;
            case ARENA -> 25;
            case CATHEDRAL -> 30;
            case LUCKY_TEMPLE -> 40;
        };
    }

    public void recruitUnit(Unit unit) {
        if (!canRecruit(unit.getType())) {
            System.out.println("Нельзя нанять " + unit.getType() + ": нет постройки!");
            return;
        }
        int cost = unit.getCost();
        if (!spendGold(cost)) {
            System.out.println("Недостаточно золота!");
            return;
        }

        ArmyGroup found = null;
        for (ArmyGroup ag : groups) {
            if (ag.getType() == unit.getType()) {
                found = ag;
                break;
            }
        }
        if (found == null) {
            found = new ArmyGroup(unit.getType(), 8, 8, side);
            groups.add(found);
            System.out.println("Создаём новую группу " + unit.getType() + " в (8,8).");
        } else {
            System.out.println("Добавляем юнита к группе " + unit.getType()
                    + " @(" + found.getX() + "," + found.getY() + ").");
        }

        found.addUnit(unit);
        System.out.println("Нанят: " + unit.getType() + " (cost=" + cost + "), теперь в группе "
                + found.getType() + " " + found.getUnits().size() + " юнит(ов)");
    }

    private boolean canRecruit(UnitType t) {
        return switch (t) {
            case SPEARMAN -> buildings.contains(BuildingType.WATCHTOWER);
            case ARCHER -> buildings.contains(BuildingType.CROSSBOW_TOWER);
            case SWORDSMAN -> buildings.contains(BuildingType.ARMORY);
            case CAVALRY -> buildings.contains(BuildingType.ARENA);
            case PALADIN -> buildings.contains(BuildingType.CATHEDRAL);
        };
    }
}
