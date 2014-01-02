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

        initArray(damageProbabilityList);
    }

    public void addCalculate() {

        makePowerValue();
        makeAttackValue();
        makeDefenseValue();
        makeLevelValue();
        makeBaseDamageValue();
        calculateDamage();
        

        double[][][] oldDamageProbabilityList = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1][2];

        copyArray(oldDamageProbabilityList, damageProbabilityList);
        initArray(damageProbabilityList);

        if (isFirstCalculate) {
            isFirstCalculate = false;
            for (int i = 0; i < 16; i++) {
                // 身代わりの処理をば
                // 初回なので必ず身代わりのHPはMAX
                // 初回ではオボンを使用してないので、必ず3つ目は0

                if (oldDamageProbabilityList[damageWithoutCritical[i]][MAX_HP / 4][0] == 1.0) {
                    // 身代わりが存在する
                    int a = damageWithoutCritical[i] < MAX_HP / 4 ? damageWithoutCritical[i] - MAX_HP / 4 : 0;
                    damageProbabilityList[MAX_HP / 4][a][0] += probabilityWithoutCritical;
                } else {
                    // 身代わりが存在しない
                    damageProbabilityList[damageWithoutCritical[i]][0][0] += probabilityWithoutCritical;
                }
                if (oldDamageProbabilityList[damageWithCritical[i]][MAX_HP / 4][0] == 1.0) {
                    int a = damageWithCritical[i] < MAX_HP / 4 ? damageWithCritical[i] - MAX_HP / 4 : 0;
                    damageProbabilityList[MAX_HP / 4][a][0] += probabilityWithCritical;
                } else {
                    damageProbabilityList[damageWithCritical[i]][0][0] += probabilityWithCritical;
                }
            }
        } else {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < MAX_DAMAGE - damageWithCritical[i]; j++) {

                    damageProbabilityList[j + damageWithoutCritical[i]][0][0] += oldDamageProbabilityList[j][0][0] * probabilityWithoutCritical;
                    damageProbabilityList[j + damageWithCritical[i]][0][0] += oldDamageProbabilityList[j][0][0] * probabilityWithCritical;

                    damageProbabilityList[j + damageWithoutCritical[i]][0][1] += oldDamageProbabilityList[j][0][1] * probabilityWithoutCritical;
                    damageProbabilityList[j + damageWithCritical[i]][0][1] += oldDamageProbabilityList[j][0][1] * probabilityWithCritical;

                    for (int k = 1; k <= MAX_HP / 4; k++) {
                        for (int l = 0; l <= 1; l++) {
                            if (oldDamageProbabilityList[j][k][l] != 0.0) {
                                // 身代わりが存在する
                                int a = damageWithoutCritical[i] < k ? damageWithoutCritical[i] - k : 0;
                                damageProbabilityList[j][a][l] += oldDamageProbabilityList[j][k][l] * probabilityWithoutCritical;

                                a = damageWithCritical[i] < k ? damageWithCritical[i] - k : 0;
                                damageProbabilityList[j][a][l] += oldDamageProbabilityList[j][k][l] * probabilityWithCritical;

                            }
                        }
                    }
                }
            }

        }

        useSitrusBerry();

    }

    /** 身代わりを設置 */
    public void addSubstitute() {
        if (isFirstCalculate) {
            for (int i = 0; i < MAX_DAMAGE; i++) {
                damageProbabilityList[i][MAX_HP / 4][0] = 1.0;
            }
        } else {
            for (int i = MAX_HP / 4 + 1; i < MAX_HP; i++) {
                damageProbabilityList[i + MAX_HP / 4][MAX_HP / 4][0] += damageProbabilityList[i][0][0];
                damageProbabilityList[i][0][0] = 0.0;
                damageProbabilityList[i + MAX_HP / 4][MAX_HP / 4][1] += damageProbabilityList[i][0][1];
                damageProbabilityList[i][0][1] = 0.0;
            }
            useSitrusBerry();

        }
    }

    /** おぼんのみ もぐもぐ */
    private void useSitrusBerry() {
        if (items == Items.SITRUS_BERRY) {
            for (int i = MAX_HP / 2 + 1; i < MAX_HP/2; i++) {
                for (int j = 0; j <= MAX_HP / 4; j++) {
                    damageProbabilityList[i - MAX_HP / 4][j][1] += damageProbabilityList[i][j][0];
                    damageProbabilityList[i][j][0] = 0.0;

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
    private void copyArray(double[][][] array, double[][][] array2) {
        for (int i = 0; i < MAX_DAMAGE; i++) {
            for (int j = 0; j < MAX_DAMAGE / 4 + 1; j++) {
                array[i][j][0] = array2[i][j][0];
                array[i][j][1] = array2[i][j][1];
            }
        }
    }

    private void initArray(double[][][] array) {
        for (int i = 0; i < MAX_DAMAGE; i++) {
            for (int j = 0; j < MAX_DAMAGE / 4 + 1; j++) {
                array[i][j][0] = 0;
                array[i][j][0] = 0;
            }
        }
    }

    private final int MAX_DAMAGE = 2048;

    /** [ダメージ][身代わりのHP][オボンを使用した] */
    public double[][][] damageProbabilityList    = new double[MAX_DAMAGE][MAX_DAMAGE / 4 + 1][2];

    private boolean isFirstCalculate;

    protected final int MAX_HP;

}
