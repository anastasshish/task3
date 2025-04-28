package org.example.MiniGameJson;

import org.example.Heroes.Unit;

import java.util.List;

public class StoredBattleData {
    private final int gold;
    private final List<Unit> survivingUnits;

    public StoredBattleData(int gold, List<Unit> survivingUnits) {
        this.gold = gold;
        this.survivingUnits = survivingUnits;
    }

    public int getGold() {
        return gold;
    }
    public List<Unit> getSurvivingUnits() {
        return survivingUnits;
    }
}
