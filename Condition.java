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
    
    /** 音技か(身代わり貫通するか) */
    protected boolean isSoundMove;

    
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
            boolean isHPMax, boolean isReflect, boolean isDoubleReflect, boolean inFriendGuard, boolean isSoundMove) {
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
        this.isSoundMove = isSoundMove;
    }
    

    public Condition() {
        this(false, false, false, false, false, false, false, false, false, false, false,false,false);
    }

    
    /**
     * isSoundMoveを取得します
     * @return isSoundMove
     */
    public boolean isSoundMove() {
        return this.isSoundMove;
    }


    /**
     * isSoundMoveを設定します
     * @param isSoundMove
     */
    public void setSoundMove(boolean isSoundMove) {
        this.isSoundMove = isSoundMove;
    }


    /**
     * isPlusWeatherを取得します
     * @return isPlusWeather
     */
    public boolean isPlusWeather() {
        return this.isPlusWeather;
    }

    /**
     * isPlusWeatherを設定します
     * @param isPlusWeather
     */
    public void setPlusWeather(boolean isPlusWeather) {
        this.isPlusWeather = isPlusWeather;
    }

    /**
     * isMinusWeatherを取得します
     * @return isMinusWeather
     */
    public boolean isMinusWeather() {
        return this.isMinusWeather;
    }

    /**
     * isMinusWeatherを設定します
     * @param isMinusWeather
     */
    public void setMinusWeather(boolean isMinusWeather) {
        this.isMinusWeather = isMinusWeather;
    }

    /**
     * isWaterSportを取得します
     * @return isWaterSport
     */
    public boolean isWaterSport() {
        return this.isWaterSport;
    }

    /**
     * isWaterSportを設定します
     * @param isWaterSport
     */
    public void setWaterSport(boolean isWaterSport) {
        this.isWaterSport = isWaterSport;
    }

    /**
     * isBurnを取得します
     * @return isBurn
     */
    public boolean isBurn() {
        return this.isBurn;
    }

    /**
     * isBurnを設定します
     * @param isBurn
     */
    public void setBurn(boolean isBurn) {
        this.isBurn = isBurn;
    }

    /**
     * isBenefitSandstormを取得します
     * @return isBenefitSandstorm
     */
    public boolean isBenefitSandstorm() {
        return this.isBenefitSandstorm;
    }

    /**
     * isBenefitSandstormを設定します
     * @param isBenefitSandstorm
     */
    public void setBenefitSandstorm(boolean isBenefitSandstorm) {
        this.isBenefitSandstorm = isBenefitSandstorm;
    }

    /**
     * isHelpingHandを取得します
     * @return isHelpingHand
     */
    public boolean isHelpingHand() {
        return this.isHelpingHand;
    }

    /**
     * isHelpingHandを設定します
     * @param isHelpingHand
     */
    public void setHelpingHand(boolean isHelpingHand) {
        this.isHelpingHand = isHelpingHand;
    }

    /**
     * isDoubleDamageを取得します
     * @return isDoubleDamage
     */
    public boolean isDoubleDamage() {
        return this.isDoubleDamage;
    }

    /**
     * isDoubleDamageを設定します
     * @param isDoubleDamage
     */
    public void setDoubleDamage(boolean isDoubleDamage) {
        this.isDoubleDamage = isDoubleDamage;
    }

    /**
     * isAttackBonusを取得します
     * @return isAttackBonus
     */
    public boolean isAttackBonus() {
        return this.isAttackBonus;
    }

    /**
     * isAttackBonusを設定します
     * @param isAttackBonus
     */
    public void setAttackBonus(boolean isAttackBonus) {
        this.isAttackBonus = isAttackBonus;
    }

    /**
     * isHPMaxを取得します
     * @return isHPMax
     */
    public boolean isHPMax() {
        return this.isHPMax;
    }

    /**
     * isHPMaxを設定します
     * @param isHPMax
     */
    public void setHPMax(boolean isHPMax) {
        this.isHPMax = isHPMax;
    }

    /**
     * isReflectを取得します
     * @return isReflect
     */
    public boolean isReflect() {
        return this.isReflect;
    }

    /**
     * isReflectを設定します
     * @param isReflect
     */
    public void setReflect(boolean isReflect) {
        this.isReflect = isReflect;
    }

    /**
     * isDoubleReflectを取得します
     * @return isDoubleReflect
     */
    public boolean isDoubleReflect() {
        return this.isDoubleReflect;
    }

    /**
     * isDoubleReflectを設定します
     * @param isDoubleReflect
     */
    public void setDoubleReflect(boolean isDoubleReflect) {
        this.isDoubleReflect = isDoubleReflect;
    }

    /**
     * isFriendGuardを取得します
     * @return isFriendGuard
     */
    public boolean isFriendGuard() {
        return this.isFriendGuard;
    }

    /**
     * isFriendGuardを設定します
     * @param isFriendGuard
     */
    public void setFriendGuard(boolean isFriendGuard) {
        this.isFriendGuard = isFriendGuard;
    }

}
