package org.example.Move;

import org.example.Heroes.ArmyGroup;
import org.example.Heroes.Computer;
import org.example.Enum.CellType;
import org.example.Enum.Side;
import org.example.Heroes.Player;
import org.example.UI.GameMap;
import org.example.Heroes.Unit;

import java.util.Iterator;

public class Move {

    public static void moveGroup(ArmyGroup group, GameMap map, int dx, int dy) {

        int stepsLeft = MovementRules.getMoveDistance(group.getType());
        int currentX = group.getX();
        int currentY = group.getY();


        while (stepsLeft > 0 && !group.isEmpty()) {
            int cost = (dx != 0 && dy != 0) ? 2 : 1;
            if (cost > stepsLeft) {
                break;
            }

            int nextX = currentX + dx;
            int nextY = currentY + dy;
            if (!map.isMoveValid(nextX, nextY, group.getOwner())) {
                break;
            }

            currentX = nextX;
            currentY = nextY;
            stepsLeft -= cost;

            // ШТРАФЫ
            CellType cell = map.getCellType(currentX, currentY);
            int damage = 0;
            if (cell == CellType.ROAD) {
                damage = 1;
            } else if (cell == CellType.NEUTRAL) {
                damage = 3;
            } else if (cell == CellType.PLAYER_CASTLE && group.getOwner() != Side.PLAYER) {
                damage = 5;
            } else if (cell == CellType.COMPUTER_CASTLE && group.getOwner() != Side.COMPUTER) {
                damage = 5;
            }


            Iterator<Unit> it = group.getUnits().iterator(); // Iterator – это интерфейс в Java, который предоставляет механизм последовательного перебора элементов коллекций (например, List, Set и т.д.)
            while (it.hasNext()) {
                Unit unit = it.next();
                unit.takeDamage(damage);
                if (!unit.isAlive()) {
                    it.remove();
                }
            }
        }

        group.setPosition(currentX, currentY);

    }


    public static void moveGroupTo(ArmyGroup group, GameMap map, int xTarget, int yTarget) {
        int stepsLeft = MovementRules.getMoveDistance(group.getType());
        if (stepsLeft <= 0) {

            return;
        }

        int currentX = group.getX();
        int currentY = group.getY();

        while (stepsLeft > 0 && !group.isEmpty()) {
            int dx = Integer.compare(xTarget, currentX);
            int dy = Integer.compare(yTarget, currentY);

            if (dx == 0 && dy == 0) {
                break;
            }

            int cost = (dx != 0 && dy != 0) ? 2 : 1;
            if (cost > stepsLeft) {
                int distX = Math.abs(xTarget - currentX);
                int distY = Math.abs(yTarget - currentY);
                if (distX > distY) {
                    dy = 0;
                } else {
                    dx = 0;
                }
                cost = 1;
                if (cost > stepsLeft) {
                    break;
                }
            }

            int nextX = currentX + dx;
            int nextY = currentY + dy;
            if (!map.isMoveValid(nextX, nextY, group.getOwner())) {
                break;
            }

            currentX = nextX;
            currentY = nextY;
            stepsLeft -= cost;

            CellType cell = map.getCellType(currentX, currentY);
            int damage = 0;
            if (cell == CellType.ROAD) {
                damage = 1;
            } else if (cell == CellType.NEUTRAL) {
                damage = 3;
            } else if (cell == CellType.PLAYER_CASTLE && group.getOwner() != Side.PLAYER) {
                damage = 5;
            } else if (cell == CellType.COMPUTER_CASTLE && group.getOwner() != Side.COMPUTER) {
                damage = 5;
            }
            Iterator<Unit> it = group.getUnits().iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                unit.takeDamage(damage);
                if (!unit.isAlive()) {
                    it.remove();
                }
            }
            if (group.isEmpty()) {
                break;
            }
            group.setPosition(currentX, currentY);
        }
    }


    public static void moveHero(Player player, GameMap map, int dx, int dy) {
        int targetX = 1, targetY = 1;
        if (dx != 0 && dy != 0) {
            int diffX = Math.abs(targetX - player.getHeroX());
            int diffY = Math.abs(targetY - player.getHeroY());
            if (diffX >= diffY) {
                dx = Integer.compare(targetX, player.getHeroX());
                dy = 0;
            } else {
                dx = 0;
                dy = Integer.compare(targetY, player.getHeroY());
            }

        }
        int newX = player.getHeroX() + dx;
        int newY = player.getHeroY() + dy;
        if (map.isMoveValid(newX, newY, player.getSide())) {
            player.setHeroXY(newX, newY);
        }
    }

    public static void moveHero(Computer comp, GameMap map, int dx, int dy) {
        int targetX = 8, targetY = 8; // целевая точка для компьютера
        if (dx != 0 && dy != 0) {
            int diffX = Math.abs(targetX - comp.getHeroX());
            int diffY = Math.abs(targetY - comp.getHeroY());
            if (diffX >= diffY) {
                dx = Integer.compare(targetX, comp.getHeroX());
                dy = 0;
            } else {
                dx = 0;
                dy = Integer.compare(targetY, comp.getHeroY());
            }
        }
        int newX = comp.getHeroX() + dx;
        int newY = comp.getHeroY() + dy;
        if (map.isMoveValid(newX, newY, comp.getSide())) {
            comp.setHeroXY(newX, newY);
        }
    }

}
