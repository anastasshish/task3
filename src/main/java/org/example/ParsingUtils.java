package org.example;

import org.example.Enum.BuildingType;
import org.example.Enum.UnitType;

public class ParsingUtils {

    public static UnitType parseUnitType(String s) {
        switch(s) {
            case "1":
                return UnitType.SPEARMAN;
            case "2":
                return UnitType.ARCHER;
            case "3":
                return UnitType.SWORDSMAN;
            case "4":
                return UnitType.CAVALRY;
            case "5":
                return UnitType.PALADIN;
            default:
                return null;
        }
    }

    public static BuildingType parseBuildingType(String s) {
        switch(s) {
            case "1":
                return BuildingType.WATCHTOWER;
            case "2":
                return BuildingType.CROSSBOW_TOWER;
            case "3":
                return BuildingType.ARMORY;
            case "4":
                return BuildingType.ARENA;
            case "5":
                return BuildingType.CATHEDRAL;
            case "6":
                return BuildingType.LUCKY_TEMPLE;
            default:
                return null;
        }
    }
}

