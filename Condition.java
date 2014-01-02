/**
 * ポケモン対戦における"状態"を表します
 * 
 * 状態異常であるやけどや、天候、みずあそびなど
 * 
 * http://bulbapedia.bulbagarden.net/wiki/Status_condition
 * 
 */
package com.odanado.pokemon.calculator.damege;

/**
 * @author odan
 * 
 */
public class Condition {
    /** 攻撃に有利な天候かどうか <br> ダメージに1.5倍(切捨) */
    protected boolean isPlusWeather = false;
    
    /** 攻撃に不利な天候かどうか <br> ダメージに0.5倍(切捨) */
    protected boolean isMinusWeather = false;
    
    /** みずあそび状態かどうか(どろあそびなども含む) <br> 威力に1/3(切捨) */
    protected boolean isWaterSport = false;
    
    /** やけど状態かどうか <br> 攻撃に0.5倍*/
    protected boolean isBurn = false;

    /** 砂嵐の恩恵を受けているかどうか <br> 防御に0.5倍 */
    protected boolean isBenefitSandstorm = false;

    /** てだすけを受けているか */
    protected boolean isHelpingHand = false;

    /** ダブルダメージか <br> ダメージに0.75倍(五捨) */
    protected boolean isDoubleDamage = false;

    /** タイプ一致か */
    protected boolean isAttackBonus = false;

    /** HPがMAXか */
    protected boolean isHPMax = false;

    /** リフレクターか */
    protected boolean isReflect = false;

    /** ダブルバトルのリフレクターか */
    protected boolean isDoubleReflect = false;
    
    /** フレンドガードか */
    protected boolean isFriendGuard = false;

    
    /**
     * 
     * @param isPlusWeather 有利な天候か
     * @param isMinusWeather 不利な天候か
     * @param isWaterSport みずあそび状態か
     * @param isBurn やけど状態か
     * @param isBenefitSandstorm 砂嵐の恩恵を受けているか
     * @param isHelpingHand てだすけを受けているか
     * @param isDoubleDamage ダブルダメージか
     * @param isAttackBonus タイプ一致か
     * @param isHPMax HPがMaxか
     * @param isReflect リフレクターか
     * @param isDoubleReflect リフレクター(ダブル)か
     */
    public Condition(boolean isPlusWeather, boolean isMinusWeather, boolean isWaterSport, boolean isBurn, 
            boolean isBenefitSandstorm, boolean isHelpingHand, boolean isDoubleDamage, boolean isAttackBonus, 
            boolean isHPMax, boolean isReflect, boolean isDoubleReflect, boolean inFriendGuard) {
        super();
        this.isPlusWeather = isPlusWeather;
        this.isMinusWeather = isMinusWeather;
        this.isWaterSport = isWaterSport;
        this.isBurn = isBurn;
        this.isBenefitSandstorm = isBenefitSandstorm;
        this.isHelpingHand = isHelpingHand;
        this.isDoubleDamage = isDoubleDamage;
        this.isAttackBonus = isAttackBonus;
        this.isHPMax = isHPMax;
        this.isReflect = isReflect;
        this.isDoubleReflect = isDoubleReflect;
        this.isFriendGuard = inFriendGuard;
    }
    
    public Condition() {
        this(false, false, false, false, false, false, false, false, false, false, false,false);
    }

}
