package org.example.Heroes;

import org.example.CompBuildOrder;
import org.example.Enum.BuildingType;
import org.example.Enum.Side;
import org.example.Enum.UnitType;
import org.example.Move.Move;
import org.example.UI.GameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

public class Computer extends AbstractSide implements Serializable{
    private static final long serialVersionUID = 1L;
    private Random rand;
    private int nextPurchaseIndex;
    private BuildingType lastBuiltBuilding;

    public Computer(int sx, int sy) {
        super(Side.COMPUTER, sx, sy);
        this.rand = new Random();
        this.nextPurchaseIndex = 1;
        this.lastBuiltBuilding = BuildingType.WATCHTOWER;
    }

    public void computerTurn(Player player, GameMap map) {
        int scenario = rand.nextInt(5);
        switch (scenario) {
            case 0 -> handleScenario1(map);
            case 1 -> handleScenario2(map);
            case 2 -> handleScenario3(map);
            case 3 -> handleScenario4(map);
            default -> handleScenario5(map);
        }
    }

    private void handleScenario1(GameMap map) {
        tryBuildNextInOrder();
        ArmyGroup group = tryBuyUnitForLastBuilt();
        moveOneGroupToCastle(map, group);
    }

    private void handleScenario2(GameMap map) {
        tryBuildNextInOrder();
        ArmyGroup ag = getRandomNonMovedGroup();
        moveOneGroupToCastle(map, ag);
    }

    private void handleScenario3(GameMap map) {
        ArmyGroup boughtGroup = tryBuyUnitForLastBuilt();
        if (boughtGroup != null) {
            for (ArmyGroup ag : groups) {
                if (ag.getType() == boughtGroup.getType()) {
                    moveOneGroupToCastle(map, ag);
                }
            }
        }
    }

    private void handleScenario4(GameMap map) {
        ArmyGroup ag = getRandomNonMovedGroup();
        moveOneGroupToCastle(map, ag);
    }

    private void handleScenario5(GameMap map) {
        moveHeroOneStep(map);
    }

    public void tryBuildNextInOrder() {
        BuildingType current = CompBuildOrder.ORDER.get(nextPurchaseIndex);
        if (buildings.contains(current)) {
            nextPurchaseIndex = (nextPurchaseIndex + 1) % CompBuildOrder.ORDER.size();
            return;
        }
        int cost = getBuildingCost(current);
        if (gold >= cost) {
            gold -= cost;
            buildings.add(current);
            lastBuiltBuilding = current;
            nextPurchaseIndex = (nextPurchaseIndex + 1) % CompBuildOrder.ORDER.size();
            System.out.println("Компьютер построил здание: " + current);
        } else {
            System.out.println("У компьютера не хватает золота на " + current);
        }
    }

    public ArmyGroup tryBuyUnitForLastBuilt() {
        if (lastBuiltBuilding == null) return null;
        if (!buildings.contains(lastBuiltBuilding)) {
            System.out.println("У компьютера нет здания " + lastBuiltBuilding + ", не можем купить юнита");
            return null;
        }
        UnitType ut = buildingToUnit(lastBuiltBuilding);
        if (ut == null) return null;

        Unit tmp = new Unit(ut);
        if (gold >= tmp.getCost()) {
            gold -= tmp.getCost();
            ArmyGroup g = addUnitToGroup(tmp);
            System.out.println("Компьютер нанял юнита: " + ut + " (осталось золота=" + gold + ")");
            return g;
        }
        return null;
    }

    private ArmyGroup addUnitToGroup(Unit u) {
        for (ArmyGroup ag : groups) {
            if (ag.getType() == u.getType()) {
                ag.addUnit(u);
                return ag;
            }
        }
        ArmyGroup newGroup = new ArmyGroup(u.getType(), heroX, heroY, side);
        newGroup.addUnit(u);
        groups.add(newGroup);
        return newGroup;
    }

    public void moveOneGroupToCastle(GameMap map, ArmyGroup ag) {
        if (ag == null) return;
        Move.moveGroupTo(ag, map, 8, 8);
    }

    public void moveHeroOneStep(GameMap map) {
        int dx = Integer.compare(8, heroX);
        int dy = Integer.compare(8, heroY);
        Move.moveHero(this, map, dx, dy);
    }

    private ArmyGroup getRandomNonMovedGroup() {
        List<ArmyGroup> candidates = new ArrayList<>();
        for (ArmyGroup ag : groups) {
            if (!ag.isEmpty()) {
                candidates.add(ag);
            }
        }
        if (candidates.isEmpty()) return null;
        return candidates.get(rand.nextInt(candidates.size()));
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

    private UnitType buildingToUnit(BuildingType b) {
        return switch (b) {
            case WATCHTOWER -> UnitType.SPEARMAN;
            case CROSSBOW_TOWER -> UnitType.ARCHER;
            case ARMORY -> UnitType.SWORDSMAN;
            case ARENA -> UnitType.CAVALRY;
            case CATHEDRAL -> UnitType.PALADIN;
            case LUCKY_TEMPLE -> null;
        };
    }
}
