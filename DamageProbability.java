/**
 * ダメージの確率を持つクラス
 */
package com.odanado.pokemon.calculator.damege;

/**
 * @author odan
 *
 */
public class DamageProbability {
    /**
     * 急所なしの確率
     */
    public double withoutCritical;
    /**
     * 急所ありの確率
     */
    public double withCritical;
    /**
     * うちオボン持ってるのん？
     */
    public boolean isUsedSitrusBerry;
    
    public DamageProbability(double withoutCritical, double withCritical ,boolean isUsedSitrusBerry) {
        this.withoutCritical = withoutCritical;
        this.withCritical = withCritical;
        this.isUsedSitrusBerry = isUsedSitrusBerry;
    }
    
}
