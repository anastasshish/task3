package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class LuckCalculator {

    private static final Logger logger = LoggerFactory.getLogger(LuckCalculator.class);

    public static int calculateFinalDamage(int baseDamage, int luck, boolean templeBonus, Random random) {
        int chanceDouble = 10 + luck * 5;
        int chanceHalf   = 10 - luck * 5;

        if (templeBonus) {
            chanceDouble += 5;
            if (chanceDouble > 100) {
                chanceDouble = 100;
            }
        }

        int roll = random.nextInt(100);
        if (roll < chanceDouble) {
            logger.warn("Урон удвоен: {}", baseDamage * 2);
            return baseDamage * 2;
        } else if (roll < chanceDouble + chanceHalf) {
            logger.error("Урон уменьшен вдвое: {}", baseDamage / 2);
            return baseDamage / 2;
        } else {
            logger.info("Удача не сработала никак, урон остался прежним: {}", baseDamage);
            return baseDamage;
        }
    }
}
