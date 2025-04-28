package org.example.Heroes;

import org.example.Enum.BuildingType;
import org.example.Enum.Side;
import org.example.Enum.UnitType;
import org.example.UI.GameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.io.Serializable;

public class ArmyGroup implements Serializable{
    private final UnitType type;
    private int x, y;
    private List<Unit> units;
    private Side owner;
    private int luck;
    private boolean templeBonusActive;  // флаг, что у стороны построен LUCKY_TEMPLE


    public ArmyGroup(UnitType type, int startX, int startY, Side owner) {
        this.type = type;
        this.x = startX;
        this.y = startY;
        this.owner = owner;
        this.units = new ArrayList<>();
        this.luck = 0;
        this.templeBonusActive = false;

    }

    public UnitType getType() {
        return type;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Side getOwner() {
        return owner;
    }

    public List<Unit> getUnits() {
        return units;
    }


    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
    public void addUnit(Unit unit) {
        if (unit.getType() == type) {
            units.add(unit);
        }
    }
    public boolean isEmpty() {
        return units.isEmpty();
    }

    public int getLuck() {
        return luck;
    }
    public boolean isTempleBonusActive() {
        return templeBonusActive;
    }


    public void calculateLuck(GameMap map, Set<BuildingType> buildings, boolean sideHasBiggerArmy) {
        int luckValue = 0;

        int ownCastleX, ownCastleY;
        int enemyCastleX, enemyCastleY;
        if (owner == Side.PLAYER) {
            ownCastleX = 8;  ownCastleY = 8;
            enemyCastleX = 1; enemyCastleY = 1;
        } else {
            ownCastleX = 1;  ownCastleY = 1;
            enemyCastleX = 8; enemyCastleY = 8;
        }
        int distToOwn   = Math.abs(x - ownCastleX) + Math.abs(y - ownCastleY);
        int distToEnemy = Math.abs(x - enemyCastleX) + Math.abs(y - enemyCastleY);

        if (distToOwn < distToEnemy) {
            luckValue += 2 ;
        }

        if (units.size() == 1) {
            luckValue--;
        }


        if (sideHasBiggerArmy) {
            luckValue += 2;
        }


        this.luck = luckValue;


        this.templeBonusActive = buildings.contains(BuildingType.LUCKY_TEMPLE);
    }



}