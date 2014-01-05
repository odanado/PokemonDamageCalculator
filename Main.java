/**
 * 
 */
package com.odanado.pokemon.calculator.damege;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author odan
 * 
 */
public class Main {


    /**
     * @param args
     */
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        AdditionalDamageCalculator additionalDamageCalculator = new AdditionalDamageCalculator(
                70, 0, 116, 0,183, 120, 50, 0, 1.5, Abilities.NONE, Items.SITRUS_BERRY, 
                new Condition(false, false, false, false, false, false, false, false, false, false, false, false));


        
        additionalDamageCalculator.addSubstitute();
        additionalDamageCalculator.addCalculate(AdditionalMode.ATTACK);
        //additionalDamageCalculator.addSubstitute();
        //additionalDamageCalculator.addCalculate(AdditionalMode.ATTACK);
        //additionalDamageCalculator.prevDamageProbabilityList();

        long stop = System.currentTimeMillis();
        for (int i = 0; i < 2048; i++) {
            for (int j = 0; j <= 183 / 4; j++) {
                if (additionalDamageCalculator.damageProbabilityList1[i][j] != 0.0) {
                    System.out.println(String.format("%d\t+ (%d) %.2f" + "%%", i, j,additionalDamageCalculator.damageProbabilityList1[i][j] * 100));
                }
                if (additionalDamageCalculator.damageProbabilityList0[i][j] != 0.0) {
                    System.out.println(String.format("%d(盆)\t+ (%d) %.2f" + "%%", i, j,additionalDamageCalculator.damageProbabilityList0[i][j] * 100));
                }
            }
        }
        System.out.println("実行にかかった時間は " + (stop - start) + " ミリ秒です。");
        
        /*
        AdditionalDamageCalculator additionalDamageCalculator = new AdditionalDamageCalculator(141, 0, 116, 0, 120, 50, 0, 1.5, Abilities.NONE, Items.SITRUS_BERRY, 
                new Condition(false, false, false, false, false, false, false, false, false, false, false, false),183);
        additionalDamageCalculator.addCalculate();
        additionalDamageCalculator.addCalculate();
        for (int i = 0; i < 2048; i++) {
            if (additionalDamageCalculator.damageProbabilityList[0][i] != 0.0) {
                System.out.println(String.format("%d(盆) %.2f" + "%%", i, additionalDamageCalculator.damageProbabilityList[0][i] * 100));
            }
            if (additionalDamageCalculator.damageProbabilityList[1][i] != 0.0) {
                System.out.println(String.format("%d %.2f" + "%%", i, additionalDamageCalculator.damageProbabilityList[1][i] * 100));
            }
        }
        // */

    }

}
