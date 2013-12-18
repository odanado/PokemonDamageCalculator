/**
 * 
 */
package com.odanado.pokemon.calculator.damege;

import java.util.Arrays;

/**
 * @author odan
 *
 */
public class AdditionalDamageCalculator extends DamageCalculator{

    /**
     * @param baseAttackValue
     * @param attackRank
     * @param baseDefenseValue
     * @param defenseRank
     * @param basePowerValue
     * @param baseLevelValue
     * @param criticalRand
     * @param typeCompatibility
     * @param abilitys
     * @param items
     * @param condition
     */
    public AdditionalDamageCalculator(int baseAttackValue, int attackRank, int baseDefenseValue, int defenseRank,
            int basePowerValue, int baseLevelValue, int criticalRand, double typeCompatibility, Abilitys abilitys, Items items, Condition condition) {
        super(baseAttackValue, attackRank, baseDefenseValue, defenseRank, 
                basePowerValue, baseLevelValue, criticalRand, typeCompatibility, abilitys, items, condition);
        
        Arrays.fill(damageProbabilityList, 0.0);
        isFirstCalculate = true;
    }
    
    protected void addCalculate() {
        makeLevelValue();
        makeAttackValue();
        makeDefenseValue();
        makePowerValue();
        calculateDamage();
        
        double[] oldDamageProbabilityList = damageProbabilityList.clone();
        Arrays.fill(damageProbabilityList, 0.0);
        
        if(isFirstCalculate) {
            for(int i=0; i<16; i++) {
                damageProbabilityList[damageWithoutCritical[i]] += probabilityWithoutCritical;
                damageProbabilityList[damageWithCritical[i]]    += probabilityWithCritical;
            }
        }
        else {
            for(int i=0; i<2048; i++) {
                for(int j=0; j<16; j++) {
                    if(i + damageWithCritical[j] < 2048) {
                        damageProbabilityList[i + damageWithoutCritical[j]] += oldDamageProbabilityList[i] * probabilityWithoutCritical;
                        damageProbabilityList[i + damageWithCritical[j]]    += oldDamageProbabilityList[i] * probabilityWithCritical;
                    
                    }
                }
            
        }
        
        
        
        }
        //*/
        
        
        isFirstCalculate = false;
        
    }

    /** damageProbabilityList[ダメージ] = そのダメージになる確率P */
    double[] damageProbabilityList = new double[2048];
    
    private boolean isFirstCalculate;

}
