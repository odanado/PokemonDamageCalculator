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
        
        damageProbabilityList0[0][0] = 1.0;
        resultDamageProbabilityList0[0][0] = 1.0;

        prevAdditionalMode[0] = AdditionalMode.NONE;
        prevAdditionalMode[1] = AdditionalMode.NONE;
        
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
            _addCalculate();
            _addCalculate();
            addresultDamageProbabilityList(1.0);
            break;
        case ATTACK_FIVE_TIMES:
            saveDamageProbabilityList();
            _addCalculate();
            _addCalculate();
            _addCalculate();
            _addCalculate();
            _addCalculate();
            addresultDamageProbabilityList(1.0);
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
        
        prevAdditionalMode[1] = prevAdditionalMode[0];
        prevAdditionalMode[0] = additionalMode;
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
        calculateSubstituteDamage();


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
                
                int a,b;
                
                if (oldDamageProbabilityList0[MAX_HP / 4][MAX_HP / 4] == 1.0) {
                    // 身代わりが存在する
                    a = condition.isSoundMove ? SubstituteDamageWithoutCritical[i] + MAX_HP / 4 : MAX_HP / 4;
                    b = SubstituteDamageWithoutCritical[i] < MAX_HP / 4 ? MAX_HP / 4 - SubstituteDamageWithoutCritical[i] : 0;
                    b = condition.isSoundMove ? MAX_HP / 4 : b;
                    
                    damageProbabilityList0[a][b] += probabilityWithoutCritical;

                    a = condition.isSoundMove ? SubstituteDamageWithCritical[i] + MAX_HP / 4 : MAX_HP / 4;
                    b = SubstituteDamageWithCritical[i] < MAX_HP / 4 ? MAX_HP / 4 - SubstituteDamageWithCritical[i] : 0;
                    b = condition.isSoundMove ? MAX_HP / 4 : b;
                    damageProbabilityList0[a][b] += probabilityWithCritical;
                    
                } else {
                    // 身代わりが存在しない
                    if(items == Items.BERRIES && typeCompatibility >= 2.0) {

                        damageProbabilityList1[damageWithoutCritical[i]][0] += probabilityWithoutCritical;
                        damageProbabilityList1[damageWithCritical[i]][0]    += probabilityWithCritical;
                    }
                    else {
                        damageProbabilityList0[damageWithoutCritical[i]][0] += probabilityWithoutCritical;
                        damageProbabilityList0[damageWithCritical[i]][0]    += probabilityWithCritical;
                        
                    }
                    
                }
            }
        } else {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < MAX_DAMAGE - damageWithCritical[i]; j++) {

                    
                    if(items == Items.BERRIES && typeCompatibility >= 2.0 && j + SubstituteDamageWithCritical[i] < 2048) {
                        damageProbabilityList1[j + damageWithoutCritical[i]][0] += oldDamageProbabilityList0[j][0] * probabilityWithoutCritical;
                        damageProbabilityList1[j + damageWithCritical[i]][0]    += oldDamageProbabilityList0[j][0] * probabilityWithCritical;
                        damageProbabilityList1[j + SubstituteDamageWithoutCritical[i]][0] += oldDamageProbabilityList1[j][0] * probabilityWithoutCritical;
                        damageProbabilityList1[j + SubstituteDamageWithCritical[i]][0]    += oldDamageProbabilityList1[j][0] * probabilityWithCritical;
                        
                    }
                    else {
                        damageProbabilityList0[j + damageWithoutCritical[i]][0] += oldDamageProbabilityList0[j][0] * probabilityWithoutCritical;
                        damageProbabilityList0[j + damageWithCritical[i]][0]    += oldDamageProbabilityList0[j][0] * probabilityWithCritical;
                        damageProbabilityList1[j + damageWithoutCritical[i]][0] += oldDamageProbabilityList1[j][0] * probabilityWithoutCritical;
                        damageProbabilityList1[j + damageWithCritical[i]][0]    += oldDamageProbabilityList1[j][0] * probabilityWithCritical;
                    }
                    

                    for (int k = 1; k <= MAX_HP / 4; k++) {
                        if (oldDamageProbabilityList0[j][k] != 0.0) {
                            // 身代わりが存在する
                            /* 身代わり貫通 */
                            int a = condition.isSoundMove ? j + SubstituteDamageWithoutCritical[i] : j;
                            int b = SubstituteDamageWithoutCritical[i] < k ? k - SubstituteDamageWithoutCritical[i] : 0;
                            b = condition.isSoundMove ? k : b;
                            
                            damageProbabilityList0[a][b] += oldDamageProbabilityList0[j][k] * probabilityWithoutCritical;

                            a = condition.isSoundMove ? j + SubstituteDamageWithCritical[i] : j;
                            b = SubstituteDamageWithCritical[i] < k ? k - SubstituteDamageWithCritical[i] : 0;
                            b = condition.isSoundMove ? k : b;
                            
                            damageProbabilityList0[a][b] += oldDamageProbabilityList0[j][k] * probabilityWithCritical;

                        }
                        
                        if (oldDamageProbabilityList1[j][k] != 0.0) {
                            // きのみ食後
                            // 身代わりが存在する
                            int a = condition.isSoundMove ? j + SubstituteDamageWithoutCritical[i] : j;
                            int b = SubstituteDamageWithoutCritical[i] < k ? k - SubstituteDamageWithoutCritical[i] : 0;
                            b = condition.isSoundMove ? k : b;
                            
                            damageProbabilityList1[a][b] += oldDamageProbabilityList1[j][k] * probabilityWithoutCritical;

                            a = condition.isSoundMove ? j + SubstituteDamageWithCritical[i] : j;
                            b = SubstituteDamageWithCritical[i] < k ? k - SubstituteDamageWithCritical[i] : 0;
                            b = condition.isSoundMove ? k : b;
                            
                            damageProbabilityList1[a][b] += oldDamageProbabilityList1[j][k] * probabilityWithCritical;

                        }
                    }
                }
            }

        }
        
        sturdy(oldDamageProbabilityList0[0][0]);
        
        useSitrusBerry();


    }


    /**
     * がんじょう 発動
     */
    void sturdy(double d) {
        System.out.println(d);
        if(d == 1.0) {
            for (int j = 0; j < MAX_HP / 4 + 1; j++) {
                for (int i = MAX_HP; i < MAX_DAMAGE; i++) {
                    damageProbabilityList0[MAX_HP - 1][j] += damageProbabilityList0[i][j];
                    damageProbabilityList0[i][j] = 0.0;
                }
            }
        }
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
        if(prevAdditionalMode[0] == AdditionalMode.PREVIOUS) {
            addCalculate(prevAdditionalMode[1]);
        }
        else {
            copyArray(prevDamageProbabilityList0 ,damageProbabilityList0);
            copyArray(prevDamageProbabilityList1 ,damageProbabilityList1);
        }
        
        if(prevAdditionalMode[1] == AdditionalMode.NONE) {
            isFirstCalculate = true;
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
                array[i][j] = 0.0;
            }
        }
    }

    public final int MAX_DAMAGE = 2048;

    /** きのみを使用していない [ダメージ][身代わりのHP] */
    private double[][] damageProbabilityList0    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
    /** きのみを使用した [ダメージ][身代わりのHP] */
    private double[][] damageProbabilityList1    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];

    /** 状態保存用 */ 
    public double[][] prevDamageProbabilityList0    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
    public double[][] prevDamageProbabilityList1    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
    
    /** 計算結果 */ 
    public double[][] resultDamageProbabilityList0    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];
    public double[][] resultDamageProbabilityList1    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1];

    private AdditionalMode[] prevAdditionalMode = new AdditionalMode[2];
    
    private boolean isFirstCalculate;

    protected final int MAX_HP;

    
}
