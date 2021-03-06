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
        
        Condition condition = new Condition();
        condition.setAttackBonus(true);
        
        
        AdditionalDamageCalculator additionalDamageCalculator = new AdditionalDamageCalculator(
                141, 0, 183, 116, 0, 120, 50, 0, 1.0, Abilities.NONE, Items.NONE,Abilities.NONE,Items.NONE, condition);



        //additionalDamageCalculator.addCalculate(AdditionalMode.SUBSTITUTE);
        //additionalDamageCalculator.addCalculate(AdditionalMode.ATTACK_TWO_TO_FIVE_TIMES);
        additionalDamageCalculator.addCalculate(AdditionalMode.ATTACK);
        additionalDamageCalculator.addCalculate(AdditionalMode.ATTACK);
        additionalDamageCalculator.recovery(50);
        //additionalDamageCalculator.addCalculate(AdditionalMode.PREVIOUS);
        //additionalDamageCalculator.addCalculate(AdditionalMode.ATTACK);

        long stop = System.currentTimeMillis();
        
        for (int i = 0; i < 2048; i++) {
            for (int j = 0; j <= 183 / 4; j++) {
                if (additionalDamageCalculator.resultDamageProbabilityList1[i][j] != 0.0) {
                    System.out.println(String.format("%d\t+ (%d)\t%.2f" + "%%", i, j,
                            additionalDamageCalculator.resultDamageProbabilityList1[i][j] * 100));
                }
                
                if (additionalDamageCalculator.resultDamageProbabilityList0[i][j] != 0.0) {
                    System.out.println(String.format("%d(盆)\t+ (%d)\t%.2f" + "%%", i, j,
                            additionalDamageCalculator.resultDamageProbabilityList0[i][j] * 100));
                }
            }
        }
        System.out.println("\n\n");
        //*/
        for(int i=0;i<183;i++) {
            for (int j = 0; j <= 183 / 4; j++) {
                if (additionalDamageCalculator.resultHitPointsProbabilityList1[i][j] != 0.0) {
                    System.out.println(String.format("%d\t+ (%d)\t%.2f" + "%%", i, j,
                            additionalDamageCalculator.resultHitPointsProbabilityList1[i][j] * 100));
                }
                if (additionalDamageCalculator.resultHitPointsProbabilityList0[i][j] != 0.0) {
                    System.out.println(String.format("%d(盆)\t+ (%d)\t%.2f" + "%%", i, j,
                            additionalDamageCalculator.resultHitPointsProbabilityList0[i][j] * 100));
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
