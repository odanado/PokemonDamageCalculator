/**
 * 
 */
package com.odanado.pokemon.calculator.damege;

/**
 * @author odan
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        AdditionalDamageCalculator additionalDamageCalculator = new AdditionalDamageCalculator(200, 0, 116, 0, 120, 50, 0, 1.0, null, null,
                new Condition(false, false, false, false, false, false, false, false, false, false, false));
        additionalDamageCalculator.addCalculate();
        additionalDamageCalculator.addCalculate();
        additionalDamageCalculator.addCalculate();
        additionalDamageCalculator.addCalculate();

        for(int i=0; i<2048; i++) {
            if(additionalDamageCalculator.damageProbabilityList[i] != 0.0) {
                System.out.println(String.format("%d %.2f"+"%%", i,additionalDamageCalculator.damageProbabilityList[i] * 100));
            }
        }
    }

}
