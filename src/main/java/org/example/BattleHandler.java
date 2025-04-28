package org.example;

import org.example.Heroes.ArmyGroup;
import org.example.Heroes.Computer;
import org.example.Heroes.Player;
import org.example.Heroes.Unit;
import org.example.Move.MovementRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BattleHandler {

    public static boolean megaBattle(Player p, Computer c, String mapName) {
        List<Unit> playerUnits = new ArrayList<>();
        for (ArmyGroup group : p.getGroups()) {
            playerUnits.addAll(group.getUnits());
        }
        List<Unit> compUnits = new ArrayList<>();
        for (ArmyGroup group : c.getGroups()) {
            compUnits.addAll(group.getUnits());
        }

        int round = 1;
        while (!playerUnits.isEmpty() && !compUnits.isEmpty() && round <= 200) {
            System.out.println("\nМега-бой раунд " + round);
            boolean damageDealt = false;

            Random random = new Random();

            for (Iterator<Unit> it = playerUnits.iterator(); it.hasNext();) {
                Unit attacker = it.next();
                if (compUnits.isEmpty()) {
                    break;
                }
                Unit target = compUnits.get(0);

                int baseDamage = attacker.getAttack();
                int finalDamage = LuckCalculator.calculateFinalDamage(
                        baseDamage,
                        attacker.getLuck(),
                        attacker.isTempleBonusActive(),
                        random
                );

                target.takeDamage(finalDamage);
                System.out.println("PLAYER " + attacker.getType() + " атакует " + target.getType()
                        + " с уроном " + finalDamage + ", остаток HP: " + target.getHp());
                damageDealt = true;
                if (!target.isAlive()) {
                    System.out.println("COMPUTER " + target.getType() + " убит.");
                    compUnits.remove(target);
                }
            }
            if (compUnits.isEmpty()) {
                break;
            }


            random = new Random();
            for (Iterator<Unit> it = compUnits.iterator(); it.hasNext();) {
                Unit attacker = it.next();
                if (playerUnits.isEmpty()) {
                    break;
                }
                Unit target = playerUnits.get(0);

                int baseDamage = attacker.getAttack();
                int finalDamage = LuckCalculator.calculateFinalDamage(
                        baseDamage,
                        attacker.getLuck(),
                        attacker.isTempleBonusActive(),
                        random
                );

                target.takeDamage(finalDamage);
                System.out.println("COMPUTER " + attacker.getType() + " атакует " + target.getType()
                        + " с уроном " + finalDamage + ", остаток HP: " + target.getHp());
                damageDealt = true;
                if (!target.isAlive()) {
                    System.out.println("PLAYER " + target.getType() + " убит.");
                    playerUnits.remove(target);
                }
            }

            if (!damageDealt) {
                System.out.println("Никто не смог нанести урон в этом раунде. Бой прекращается.");
                break;
            }
            round++;
        }

        if (playerUnits.isEmpty() && compUnits.isEmpty()) {
            System.out.println("Мега-бой закончился вничью!");
            return false;
        } else if (playerUnits.isEmpty()) {
            return false;
        } else {

            System.out.println("Игрок победил в мега-бою!");



            return true;
        }
    }

    public static boolean checkBattle(Player p, Computer c) {
        for (ArmyGroup pg : p.getGroups()) {
            for (ArmyGroup cg : c.getGroups()) {
                int dx = Math.abs(pg.getX() - cg.getX());
                int dy = Math.abs(pg.getY() - cg.getY());
                int dist = dx + dy;
                int rangeP = MovementRules.getAttackRange(pg.getType());
                int rangeC = MovementRules.getAttackRange(cg.getType());
                if (dist <= rangeP || dist <= rangeC) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkBattle(Computer c, Player p) {
        for (ArmyGroup cg : c.getGroups()) {
            for (ArmyGroup pg : p.getGroups()) {
                int dx = Math.abs(cg.getX() - pg.getX());
                int dy = Math.abs(cg.getY() - pg.getY());
                int dist = dx + dy;
                int rangeC = MovementRules.getAttackRange(cg.getType());
                int rangeP = MovementRules.getAttackRange(pg.getType());
                if (dist <= rangeC || dist <= rangeP) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void removeDeadGroups(Player p) {
        p.getGroups().removeIf(ArmyGroup::isEmpty);
    }

    public static void removeDeadGroups(Computer c) {
        c.getGroups().removeIf(ArmyGroup::isEmpty);
    }

    public static boolean doBattleInRange(Player p, Computer c, String mapName) {
        List<ArmyGroup> pGroups = new ArrayList<>(p.getGroups());
        List<ArmyGroup> cGroups = new ArrayList<>(c.getGroups());
        int round = 1;

        while (!pGroups.isEmpty() && !cGroups.isEmpty() && round <= 200) {
            System.out.println("\nРаунд " + round);
            boolean anyDamageThisRound = false;


            for (ArmyGroup pg : new ArrayList<>(pGroups)) {
                if (pg.isEmpty())
                    continue;
                ArmyGroup target = findClosestEnemyGroupInRange(pg, cGroups);
                if (target != null) {
                    int dmg = doGroupAttack(pg, target);
                    if (dmg > 0) {
                        anyDamageThisRound = true;
                    }
                }
            }
            pGroups.removeIf(ArmyGroup::isEmpty);
            cGroups.removeIf(ArmyGroup::isEmpty);
            if (pGroups.isEmpty() || cGroups.isEmpty())
                break;

            for (ArmyGroup cg : new ArrayList<>(cGroups)) {
                if (cg.isEmpty())
                    continue;
                ArmyGroup target = findClosestEnemyGroupInRange(cg, pGroups);
                if (target != null) {
                    int dmg = doGroupAttack(cg, target);
                    if (dmg > 0) {
                        anyDamageThisRound = true;
                    }
                }
            }
            pGroups.removeIf(ArmyGroup::isEmpty);
            cGroups.removeIf(ArmyGroup::isEmpty);

            if (!anyDamageThisRound) {
                System.out.println("Никто не смог нанести урон в этом раунде — бой прекращён.");
                int totalP = countUnits(pGroups);
                int totalC = countUnits(cGroups);

                return (totalP >= totalC);
            }
            round++;
        }


        if (round > 200) {
            int totalP = countUnits(pGroups);
            int totalC = countUnits(cGroups);
            boolean pWin = (totalP >= totalC);

            return pWin;
        }


        if (pGroups.isEmpty() && cGroups.isEmpty()) {
            System.out.println("Ничья!");
            return false;
        } else if (pGroups.isEmpty()) {
            return false;
        } else {
            System.out.println("Игрок победил в бою!");
            return true;
        }
    }

    private static ArmyGroup findClosestEnemyGroupInRange(ArmyGroup attacker, List<ArmyGroup> enemies) {
        int range = MovementRules.getAttackRange(attacker.getType());
        ArmyGroup bestTarget = null;
        int bestDist = Integer.MAX_VALUE;
        for (ArmyGroup e : enemies) {
            int dx = Math.abs(attacker.getX() - e.getX());
            int dy = Math.abs(attacker.getY() - e.getY());
            int dist = dx + dy;
            if (dist <= range && dist < bestDist) {
                bestDist = dist;
                bestTarget = e;
            }
        }
        return bestTarget;
    }

    private static int doGroupAttack(ArmyGroup attacker, ArmyGroup victim) {
        int totalDamageDealt = 0;
        List<Unit> attackers = attacker.getUnits();
        List<Unit> defenders = victim.getUnits();
        Random random = new Random();

        for (Unit attackerUnit : attackers) {
            if (defenders.isEmpty())
                break;

            int baseDamage = attackerUnit.getAttack();
            int finalDamage = LuckCalculator.calculateFinalDamage(
                    baseDamage,
                    attacker.getLuck(),
                    attacker.isTempleBonusActive(),
                    random
            );

            Unit defenderUnit = defenders.get(0);
            defenderUnit.takeDamage(finalDamage);
            totalDamageDealt += finalDamage;

            System.out.println(attacker.getOwner() + " " + attackerUnit.getType()
                    + " атакует " + defenderUnit.getType()
                    + " с уроном " + finalDamage
                    + ". Осталось HP: " + defenderUnit.getHp());

            if (!defenderUnit.isAlive()) {
                System.out.println("Юнит " + defenderUnit.getType() + " убит.");
                defenders.remove(defenderUnit);
            }
        }
        return totalDamageDealt;
    }

    private static int countUnits(List<ArmyGroup> groups) {
        int total = 0;
        for (ArmyGroup ag : groups) {
            total += ag.getUnits().size();
        }
        return total;
    }
}
