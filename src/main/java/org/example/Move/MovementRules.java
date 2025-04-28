package org.example.Move;

import org.example.Enum.UnitType;

public class MovementRules {
    public static int getMoveDistance(UnitType t) {
        switch(t) {
            case SPEARMAN:
                return 1;
            case ARCHER:
                return 1;
            case SWORDSMAN:
                return 2;
            case CAVALRY:
                return 4;
            case PALADIN:
                return 4;
        }
        return 1;
    }

    public static int getAttackRange(UnitType t) {
        switch(t) {
            case SPEARMAN:
                return 1;
            case ARCHER:
                return 4;
            case SWORDSMAN:
                return 1;
            case CAVALRY:
                return 2;
            case PALADIN:
                return 2;
        }
        return 1;
    }
}
