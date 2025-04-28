package org.example.Heroes;

import org.example.Enum.UnitType;
import java.io.Serializable;

public class Unit implements Serializable{
    private static final long serialVersionUID = 1L;
    private UnitType type;
    private int hp;
    private int attack;
    private int cost;
    private int luck;
    private boolean templeBonusActive;

    public Unit(UnitType type) {
        this.type = type;
        switch(type) {
            case SPEARMAN:
                hp = 30;
                attack = 20;
                cost = 10;
                break;
            case ARCHER:
                hp = 20;
                attack = 40;
                cost = 15;
                break;
            case SWORDSMAN:
                hp = 40;
                attack = 30;
                cost = 20;
                break;
            case CAVALRY:
                hp = 30;
                attack = 40;
                cost = 25;
                break;
            case PALADIN:
                hp = 40;
                attack = 40;
                cost = 30;
                break;
        }
        luck = 0;
        templeBonusActive = false;
    }

    public UnitType getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getCost() {
        return cost;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int getLuck() {
        return luck;
    }
    public boolean isTempleBonusActive() {
        return templeBonusActive;
    }


}
