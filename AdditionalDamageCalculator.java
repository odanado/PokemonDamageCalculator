/**
 * 
 */
package com.odanado.pokemon.calculator.damege;

/**
 * @author odan
 * 
 */
public class AdditionalDamageCalculator extends DamageCalculator {

    /**
     * @param baseAttackValue
     * @param attackRank
     * @param baseDefenseValue
     * @param defenseRank
     * @param basePowerValue
     * @param baseLevelValue
     * @param criticalRand
     * @param typeCompatibility
     * @param abilities
     * @param items
     * @param condition
     */
    public AdditionalDamageCalculator(int baseAttackValue, int attackRank, int baseDefenseValue, int defenseRank, int MAX_HP, int basePowerValue, int baseLevelValue, int criticalRand, double typeCompatibility, Abilities abilities, Items items, Condition condition) {
        super(baseAttackValue, attackRank, baseDefenseValue, defenseRank, basePowerValue, baseLevelValue, criticalRand, typeCompatibility, abilities, items, condition);


        this.MAX_HP = MAX_HP;

        isFirstCalculate = true;

        initArray(damageProbabilityList0);
        initArray(damageProbabilityList1);
        initArray(prevDamageProbabilityList0);
        initArray(prevDamageProbabilityList1);
        initArray(resultDamageProbabilityList0);
        initArray(resultDamageProbabilityList1);
        
    }

    public void addCalculate(AdditionalMode additionalMode) {
        initArray(resultDamageProbabilityList0);
        initArray(resultDamageProbabilityList1);
        
        switch (additionalMode) {
        case ATTACK:
            saveDamageProbabilityList();
            _addCalculate();
            addresultDamageProbabilityList(1.0);
            break;
        case ATTACK_TWO_TIMES:
            saveDamageProbabilityList();
            break;
        case ATTACK_FIVE_TIMES:
            saveDamageProbabilityList();
            break;
        case ATTACK_TWO_TO_FIVE_TIMES:
            saveDamageProbabilityList();
            _addCalculate();
            _addCalculate();
            addresultDamageProbabilityList(3.0/8.0);
            
            _addCalculate();
            addresultDamageProbabilityList(3.0/8.0);
            
            _addCalculate();
            addresultDamageProbabilityList(1.0/8.0);
            
            _addCalculate();
            addresultDamageProbabilityList(1.0/8.0);
            break;
        case SUBSTITUTE:
            saveDamageProbabilityList();
            addSubstitute();
            addresultDamageProbabilityList(1.0);
            break;
        case PREVIOUS:
            prevDamageProbabilityList();
            addresultDamageProbabilityList(1.0);
            break;
        default:
            break;
        }
    }
    
    private void _addCalculate() {

        double[][] oldDamageProbabilityList0 = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
        double[][] oldDamageProbabilityList1 = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
        
        makePowerValue();
        makeAttackValue();
        makeDefenseValue();
        makeLevelValue();
        makeBaseDamageValue();
        calculateDamage();
        


        copyArray(damageProbabilityList0, oldDamageProbabilityList0);
        copyArray(damageProbabilityList1, oldDamageProbabilityList1);
        initArray(damageProbabilityList0);
        initArray(damageProbabilityList1);

        if (isFirstCalculate) {
            isFirstCalculate = false;
            for (int i = 0; i < 16; i++) {
                // 身代わりの処理をば
                // 初回なので必ず身代わりのHPはMAX
                // 初回ではオボンを使用してないので、必ずList0で計算

                if (oldDamageProbabilityList0[MAX_HP / 4][MAX_HP / 4] == 1.0) {
                    // 身代わりが存在する
                    int a = damageWithoutCritical[i] < MAX_HP / 4 ? MAX_HP / 4 - damageWithoutCritical[i] : 0;
                    damageProbabilityList0[MAX_HP / 4][a] += probabilityWithoutCritical;
                    
                    a = damageWithCritical[i] < MAX_HP / 4 ? MAX_HP / 4 - damageWithCritical[i] : 0;
                    damageProbabilityList0[MAX_HP / 4][a] += probabilityWithCritical;
                    
                } else {
                    // 身代わりが存在しない
                    damageProbabilityList0[damageWithoutCritical[i]][0] += probabilityWithoutCritical;
                    damageProbabilityList0[damageWithCritical[i]][0]    += probabilityWithCritical;
                }
            }
        } else {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < MAX_DAMAGE - damageWithCritical[i]; j++) {

                    damageProbabilityList0[j + damageWithoutCritical[i]][0] += oldDamageProbabilityList0[j][0] * probabilityWithoutCritical;
                    damageProbabilityList0[j + damageWithCritical[i]][0]    += oldDamageProbabilityList0[j][0] * probabilityWithCritical;

                    damageProbabilityList1[j + damageWithoutCritical[i]][0] += oldDamageProbabilityList1[j][0] * probabilityWithoutCritical;
                    damageProbabilityList1[j + damageWithCritical[i]][0]    += oldDamageProbabilityList1[j][0] * probabilityWithCritical;

                    for (int k = 1; k <= MAX_HP / 4; k++) {
                        if (oldDamageProbabilityList0[j][k] != 0.0) {
                            // 身代わりが存在する
                            int a = damageWithoutCritical[i] < k ? k - damageWithoutCritical[i] : 0;
                            damageProbabilityList0[j][a] += oldDamageProbabilityList0[j][k] * probabilityWithoutCritical;

                            a = damageWithCritical[i] < k ? k - damageWithCritical[i] : 0;
                            damageProbabilityList0[j][a] += oldDamageProbabilityList0[j][k] * probabilityWithCritical;

                        }
                        if (oldDamageProbabilityList1[j][k] != 0.0) {
                            // 身代わりが存在する
                            int a = damageWithoutCritical[i] < k ? k - damageWithoutCritical[i] : 0;
                            damageProbabilityList1[j][a] += oldDamageProbabilityList1[j][k] * probabilityWithoutCritical;

                            a = damageWithCritical[i] < k ? k - damageWithCritical[i] : 0;
                            damageProbabilityList1[j][a] += oldDamageProbabilityList1[j][k] * probabilityWithCritical;

                        }
                    }
                }
            }

        }

        useSitrusBerry();


    }

    /**
     * resultDamageProbabilityList に足し算
     * d は 確率の重み
     * @param d
     */
    private void addresultDamageProbabilityList(double d) {
        for (int i = 0; i < MAX_DAMAGE; i++) {
            for (int j = 0; j < MAX_DAMAGE / 4 + 1; j++) {
                resultDamageProbabilityList0[i][j] += damageProbabilityList0[i][j] * d;
                resultDamageProbabilityList1[i][j] += damageProbabilityList1[i][j] * d;
            }
        }
    }
    
    private void saveDamageProbabilityList() {
        copyArray(damageProbabilityList0, prevDamageProbabilityList0);
        copyArray(damageProbabilityList1, prevDamageProbabilityList1);
    }
    
    public void prevDamageProbabilityList() {
        /* next は何を計算したか保存しておいて再計算 */
        
        copyArray(prevDamageProbabilityList0 ,damageProbabilityList0);
        copyArray(prevDamageProbabilityList1 ,damageProbabilityList1);
        
    }
    
    private void viewArray(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if(array[i][j]!=0.0) {
                    
                    System.out.print(array[i][j]+" ");
                }
            }
            System.out.println("");
        }
    }
    
    /** 身代わりを設置 */
    public void addSubstitute() {
        if (isFirstCalculate) {
            for (int i = 0; i < MAX_DAMAGE; i++) {
                damageProbabilityList0[MAX_HP / 4][MAX_HP / 4] = 1.0;
            }
        } else {
            for (int i = 0; i < 3 * MAX_HP / 4 + 1; i++) {
                damageProbabilityList0[i + MAX_HP / 4][MAX_HP / 4] += damageProbabilityList0[i][0];
                damageProbabilityList0[i][0] = 0.0;
                damageProbabilityList1[i + MAX_HP / 4][MAX_HP / 4] += damageProbabilityList1[i][0];
                damageProbabilityList1[i][0] = 0.0;
            }
            useSitrusBerry();

        }
    }

    /** おぼんのみ もぐもぐ */
    private void useSitrusBerry() {
        if (items == Items.SITRUS_BERRY) {
            for (int i = MAX_HP / 2 + 1; i < MAX_DAMAGE; i++) {
                /* 死をマージしていいか怪しいかも、結局は死んでるんだけども */
                int a = i < MAX_HP ? i - MAX_HP / 4 : i;
                for (int j = 0; j <= MAX_HP / 4; j++) {
                    damageProbabilityList1[a][j] += damageProbabilityList0[i][j];
                    damageProbabilityList0[i][j] = 0.0;

                }
            }
        }
    }

    /**
     * array -> array2
     * 
     * @param array
     * @param array2
     */
    private void copyArray(double[][] array, double[][] array2) {
        for (int i = 0; i < MAX_DAMAGE; i++) {
            for (int j = 0; j < MAX_DAMAGE / 4 + 1; j++) {
                array2[i][j] = array[i][j];
            }
        }
    }

    private void initArray(double[][]array) {
        for (int i = 0; i < MAX_DAMAGE; i++) {
            for (int j = 0; j < MAX_DAMAGE / 4 + 1; j++) {
                array[i][j] = 0;
            }
        }
    }

    private final int MAX_DAMAGE = 2048;

    /**オボンのみを使用していない [ダメージ][身代わりのHP] */
    public double[][] damageProbabilityList0    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
    /**オボンのみを使用した [ダメージ][身代わりのHP] */
    public double[][] damageProbabilityList1    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];

    /** 状態保存用 */ 
    public double[][] prevDamageProbabilityList0    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
    public double[][] prevDamageProbabilityList1    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
    
    /** 計算結果 */ 
    public double[][] resultDamageProbabilityList0    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
    public double[][] resultDamageProbabilityList1    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
    

    private boolean isFirstCalculate;

    protected final int MAX_HP;

    
}
