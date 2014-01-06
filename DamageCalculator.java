/**
 * http://pokemon-trainer.net/bbs/patio.cgi?mode=view&no=702&p=2
 * http://bbs10.aimix-z.com/mtpt.cgi?room=sonota&mode=view&no=54
 * http://www21.atpages.jp/maximster/2011/damage2011.html
 * http://www21.atpages.jp/maximster/2011/damagesource2011.txt
 * http://pokemon-trainer.net/bbs/patio.cgi?mode=view&no=702&p=2
 * http://pokemon-trainer.net/xy/dmcs/
 * http://pokemon-trainer.net/xy/dmcs/criticalhit.html
 */
/*
 * BW 【レベル定数】=【攻撃側のLv×0.4＋2(切捨)】
 * 
 * 【補正後威力】=【威力】×【特性1.2(切上)】×【道具1.2(五捨)】×【てだすけ1.5(切捨)】
 * ×【特性テク二シャン・ねつぼうそう等1.5(切捨)】×【道具1.5(切捨)】×【特性0.75,1.25,1.3(切上)】
 * ×【道具1.1(五捨)】×【特性かんそうはだ1.25(五捨)】×【みずあそび等1/3(切捨)】【特性もうか1.5,2.0(切捨)】
 * 
 * 【補正後攻撃】=【攻撃】×【ランク補正(切捨)】×【道具1.5,2.0(切捨)】
 * ×【特性0.5,こんじょう・はりきり・サンパワー・フラワーギフト・プラス・マイナス1.5(切捨)】
 * 
 * 【補正後防御】=【防御】×【ランク補正(切捨)】×【砂嵐1.5(切捨)】×【道具1.5(切捨)】×【特性1.5(切捨)】
 * 
 * 【ダメージ】= (【レベル定数】×【補正後威力】×【補正後攻撃】÷【補正後防御(切捨)】÷【50(切捨)】×【たいねつ等0.5(切捨)】＋2)
 * ×【全体技0.75(五捨)】×【天候0.5,1.5(切捨)】×【急所】×【乱数(切捨)】×【タイプ一致,てきおうりょく(切捨)】
 * ×【相性(切捨)】×【半減きのみ0.5(切捨)】×【いろめがね2.0】
 * ×【フィルター等0.75,マルチスケイル0.5,たつじんのおび1.2,いのちのたま1
 * .3,メトロノーム1.0→1.2→1.4→1.6→1.8→2.0, フレンドガード0.75,リフレクター等(最後に五捨六入)】
 */
package com.odanado.pokemon.calculator.damege;

/**
 * @author odan
 * 
 */
public class DamageCalculator {
    /**
     * 
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
    public DamageCalculator(int baseAttackValue, int attackRank, int baseDefenseValue, int defenseRank, int basePowerValue,int baseLevelValue, 
                            int criticalRand,double typeCompatibility, Abilities abilities, Items items, Condition condition) {

        this.baseAttackValue = baseAttackValue;
        this.attackRank = attackRank;

        this.baseDefenseValue = baseDefenseValue;
        this.defenseRank = defenseRank;

        this.basePowerValue = basePowerValue;
        this.baseLevelValue = baseLevelValue;
        
        this.criticalRand = criticalRand;
        this.typeCompatibility = typeCompatibility;
        
        this.abilities = abilities;
        this.items = items;
        this.condition = condition;
        

        probabilityWithoutCritical = 1.0/16.0 * (1 - CRITICAL_PROBABILITY[criticalRand+1]);
        probabilityWithCritical    = 1.0/16.0 * CRITICAL_PROBABILITY[criticalRand+1];

        makePowerValue();
        makeAttackValue();
        makeDefenseValue();
        makeLevelValue();
        makeBaseDamageValue();
        calculateDamage();
        
    }
    
    protected void calculateDamage() {
        /*
         * 確率は変わらないから、各乱数の計算を行ってから確率の重みをつける
         * 日本語難しい
         * つらい
         * 
         * ↑がんばって
         *  ×【急所】×【乱数(切捨)】×【タイプ一致,てきおうりょく(切捨)】
         *  ×【相性(切捨)】×【半減きのみ0.5(切捨)】×【いろめがね2.0】
         *  ×【フィルター等0.75,マルチスケイル0.5,たつじんのおび1.2,いのちのたま1.3,メトロノーム1.0→1.2→1.4→1.6→1.8→2.0, フレンドガード0.75,リフレクター等(最後に五捨六入)】
         */

        
        double random = 0.0;
        
        int criticalDamageValue = baseDamageValue;
        
        /* スナイパーか */
        if(abilities == Abilities.SNIPER) {
            criticalDamageValue *= 1.5;
        }
        
        /* 乱数と急所 */
        for(int i=0; i<16; i++) {
            random = (85.0 + i) / 100.0;
            damageWithoutCritical[i] = (int) (baseDamageValue * random);
            damageWithCritical[i]    = (int) (criticalDamageValue * 1.5);
            damageWithCritical[i]    = (int) (damageWithCritical[i] * random);
            
        }
        
        /* タイプ一致か(てきおうりょくか) */
        if(condition.isAttackBonus) {
            double attackBonus;
            if(abilities == Abilities.ADAPTABILITY) {
                attackBonus = 2.0;
            }
            else {
                attackBonus = 1.5;
            }
            for(int i=0; i<16; i++) {
                damageWithoutCritical[i] *= attackBonus;
                damageWithCritical[i]    *= attackBonus;                
            }
        }
        
        /* タイプ相性 */
        for(int i=0; i<16; i++) {
            damageWithoutCritical[i] *= typeCompatibility;
            damageWithCritical[i]    *= typeCompatibility;                
        }
        
        
        /* 半減の実 */
        if(items == Items.BERRIES && typeCompatibility >= 2.0) {
            for(int i=0; i<16; i++) {
                damageWithoutCritical[i] *= 0.5;
                damageWithCritical[i]    *= 0.5;                
            }
        }
        
        /* いろめがね */
        if(abilities == Abilities.TINTED_LENS) {
            for(int i=0; i<16; i++) {
                damageWithoutCritical[i] *= 2;
                damageWithCritical[i]    *= 2;                
            }
        }
        
        double value = 1.0;
        double reflect = 1.0;
        //【フィルター等0.75,マルチスケイル0.5,たつじんのおび1.2,いのちのたま1.3,メトロノーム1.0→1.2→1.4→1.6→1.8→2.0, フレンドガード0.75,リフレクター等(最後に五捨六入)】
        /* フィルターとマルスケ */
        if(abilities == Abilities.FILTER) value *= 0.75;
        if(abilities == Abilities.MULTISCALE && condition.isHPMax) value *= 0.5;
        
        /* たつじんのおびといのちのたま */
        if(items == Items.EXPERT_BELT) value *= 1.2;
        if(items == Items.LIFE_ORB) value *= 1.3;

        /* フレンドガード */
        if(condition.isFriendGuard) value *= 0.75;
        
        /* リフレクター */
        if(condition.isReflect) {
            reflect = 0.5;
        } 
        else if(condition.isDoubleReflect) {
            reflect = 2/3;
        }
        
        for(int i=0; i<16; i++) {
            damageWithoutCritical[i] = calcRoundHalfDown(damageWithoutCritical[i] * value * reflect);
            damageWithCritical[i]    = calcRoundHalfDown(damageWithCritical[i] * value);
        }
        /*
         *  //debug
        for(int i=0; i<16; i++) {
            System.out.println(String.format("%d, %d",damageWithCritical[i],damageWithoutCritical[i]));
        }
        */
    }

    /**
     * 身代わりのダメージ計算をします
     * と言っても、半減の実を無視するだけ
     * 
     * 
     */
    protected void calculateSubstituteDamage() {
        
        double random = 0.0;
        
        int criticalDamageValue = baseDamageValue;
        
        /* スナイパーか */
        if(abilities == Abilities.SNIPER) {
            criticalDamageValue *= 1.5;
        }
        
        /* 乱数と急所 */
        for(int i=0; i<16; i++) {
            random = (85.0 + i) / 100.0;
            SubstituteDamageWithoutCritical[i] = (int) (baseDamageValue * random);
            SubstituteDamageWithCritical[i]    = (int) (criticalDamageValue * 1.5);
            SubstituteDamageWithCritical[i]    = (int) (SubstituteDamageWithCritical[i] * random);
            
        }
        
        /* タイプ一致か(てきおうりょくか) */
        if(condition.isAttackBonus) {
            double attackBonus;
            if(abilities == Abilities.ADAPTABILITY) {
                attackBonus = 2.0;
            }
            else {
                attackBonus = 1.5;
            }
            for(int i=0; i<16; i++) {
                SubstituteDamageWithoutCritical[i] *= attackBonus;
                SubstituteDamageWithCritical[i]    *= attackBonus;                
            }
        }
        
        /* タイプ相性 */
        for(int i=0; i<16; i++) {
            SubstituteDamageWithoutCritical[i] *= typeCompatibility;
            SubstituteDamageWithCritical[i]    *= typeCompatibility;                
        }
        
        /* いろめがね */
        if(abilities == Abilities.TINTED_LENS) {
            for(int i=0; i<16; i++) {
                SubstituteDamageWithoutCritical[i] *= 2;
                SubstituteDamageWithCritical[i]    *= 2;                
            }
        }
        
        double value = 1.0;
        double reflect = 1.0;
        //【フィルター等0.75,マルチスケイル0.5,たつじんのおび1.2,いのちのたま1.3,メトロノーム1.0→1.2→1.4→1.6→1.8→2.0, フレンドガード0.75,リフレクター等(最後に五捨六入)】
        /* フィルターとマルスケ */
        if(abilities == Abilities.FILTER) value *= 0.75;
        if(abilities == Abilities.MULTISCALE && condition.isHPMax) value *= 0.5;
        
        /* たつじんのおびといのちのたま */
        if(items == Items.EXPERT_BELT) value *= 1.2;
        if(items == Items.LIFE_ORB) value *= 1.3;

        /* フレンドガード */
        if(condition.isFriendGuard) value *= 0.75;
        
        /* リフレクター */
        if(condition.isReflect) {
            reflect = 0.5;
        } 
        else if(condition.isDoubleReflect) {
            reflect = 2.0/3.0;
        }
        
        for(int i=0; i<16; i++) {
            SubstituteDamageWithoutCritical[i] = calcRoundHalfDown(SubstituteDamageWithoutCritical[i] * value * reflect);
            SubstituteDamageWithCritical[i]    = calcRoundHalfDown(SubstituteDamageWithCritical[i] * value);
        }
        
    }
    
    protected void makeBaseDamageValue() {
        /*
         * 【ダメージ】= (【レベル定数】×【補正後威力】×【補正後攻撃】÷【補正後防御(切捨)】÷【50(切捨)】×【たいねつ等0.5(切捨)】＋2)
         * ×【全体技0.75(五捨)】×【天候0.5,1.5(切捨)】×【急所】×【乱数(切捨)】×【タイプ一致,てきおうりょく(切捨)】
         * ×【相性(切捨)】×【半減きのみ0.5(切捨)】×【いろめがね2.0】
         * ×【フィルター等0.75,マルチスケイル0.5,たつじんのおび1.2,いのちのたま1.3,メトロノーム1.0→1.2→1.4→1.6→1.8→2.0, フレンドガード0.75,リフレクター等(最後に五捨六入)】
         */
        
        baseDamageValue = levelValue * powerValue * attackValue / defenseValue;
        baseDamageValue /= 50;
        
        /* たいねつ と あついしぼう と ファーコート */
        if(abilities == Abilities.THICK_FAT) baseDamageValue /= 2;
        if(abilities == Abilities.HEATPROOF) baseDamageValue /= 2;
        if(abilities == Abilities.FUR_COAT)  baseDamageValue /= 2;
        
        baseDamageValue += 2;
        
        /* ダブルダメージ */ 
        if(condition.isDoubleDamage) baseDamageValue = calcRoundHalfDown(baseDamageValue * 0.75);
        
        /* 天候 */
        if(condition.isPlusWeather) baseDamageValue *= 1.5;
        if(condition.isMinusWeather) baseDamageValue *= 0.5;
        
        
    }
    
    protected void makePowerValue() {
        powerValue = basePowerValue;
        /*
         *  【補正後威力】=【威力】×【特性1.2(切上)】×【道具1.2(五捨)】×【てだすけ1.5(切捨)】
         *  ×【特性テク二シャン・ねつぼうそう等1.5(切捨)】×【道具1.5(切捨)】×【特性0.75,1.25,1.3(切上)】
         *  ×【道具1.1(五捨)】×【特性かんそうはだ1.25(五捨)】×【みずあそび等1/3(切捨)】【特性もうか1.5,2.0(切捨)】
         */
        
        /* 特性 道具 1.2倍 */
        if(abilities == Abilities.IRON_FIST) powerValue = (int)Math.ceil(powerValue * 1.2);
        if(items == Items.PLATES) powerValue = calcRoundHalfDown(powerValue * 1.2);
        
        /* てだすけ */
        if(condition.isHelpingHand) powerValue *= 1.5;

        /* 特性 1.5倍 道具 1.3倍 */
        if(abilities == Abilities.TECHNICIAN) powerValue *= 1.5;
        if(abilities == Abilities.FLARE_BOOST) powerValue *= 1.5;
        if(items == Items.JEWELS) powerValue *= 1.3;

        /* 特性 0.75,1.25,1.3倍 */
        if(abilities == Abilities.RIVALRY_PLUS) powerValue = calcRoundHalfDown(powerValue * 1.25);
        if(abilities == Abilities.RIVALRY_MINUS) powerValue = calcRoundHalfDown(powerValue * 0.75);
        if(abilities == Abilities.SHEER_FORCE) powerValue = (int) Math.ceil(powerValue * 1.3);
        if(abilities == Abilities.SAND_FORCE) powerValue = (int) Math.ceil(powerValue * 1.3);
        
        /* 道具 1.1倍 */
        if(items == Items.FOCUS_BAND) powerValue = calcRoundHalfDown(powerValue * 1.1);
        if(items == Items.WISDOM_GLASSES) powerValue = calcRoundHalfDown(powerValue * 1.1); 
        
        /* かんそうはだ */
        if(abilities == Abilities.DRY_SKIN) powerValue = calcRoundHalfDown(powerValue * 1.25);
        
        /* みずあそびなど */
        if(condition.isWaterSport) powerValue /= 3;
        
        /* もうか、ヨガパワー */
        if(abilities == Abilities.BLAZE) powerValue *= 1.5;
        if(abilities == Abilities.PURE_POWER) powerValue *= 2;
        if(abilities == Abilities.HUGE_POWER) powerValue *= 2;
        
        
        
    }
    
    protected void makeDefenseValue() {
        /*
         * 【補正後防御】=【防御】×【ランク補正(切捨)】×【砂嵐1.5(切捨)】×【道具1.5(切捨)】×【特性1.5(切捨)】
         */
        
        defenseValue = baseDefenseValue;
        
        if(defenseRank >= 0) {
            defenseValue = defenseValue * (2 + defenseRank) / 2;
        }
        else {
            defenseValue = defenseValue * 2 / (2 + defenseRank);
        }
        
        /* 砂嵐の恩恵を受けているか */
        if(condition.isBenefitSandstorm) defenseValue *= 1.5;
        
        /* しんかのきせき と とつげきチョッキ */
        if(items == Items.EVOLUTION_STONE) defenseValue *= 1.5;
        if(items == Items.ASSAULT_VEST)    defenseValue *= 1.5;

        /* ふしぎなウロコ と フラワーギフト */
        if(abilities == Abilities.MARVEL_SCALE) defenseValue *= 1.5;
        if(abilities == Abilities.FLOWER_GIFT)  defenseValue *= 1.5;
        
    }
    
    protected void makeAttackValue() {
        /*
         * 【補正後攻撃】=【攻撃】×【ランク補正(切捨)】×【道具1.5,2.0(切捨)】
         * ×【特性0.5,こんじょう・はりきり・サンパワー・フラワーギフト・プラス・マイナス1.5(切捨)】
         */

        attackValue = baseAttackValue;
        
        /* やけど */
        if(condition.isBurn) attackValue *= 0.5;
        
        /* ランク補正 */
        if(attackRank >= 0) {
            attackValue = attackValue * (2 + attackRank) / 2;
        }
        else {
            attackValue = attackValue * 2 / (2 + attackRank);
        }
        
        /* こだわり系 と ふといホネ */
        if(items == Items.ADHERENCE_HEADBAND) attackValue *= 1.5;
        if(items == Items.ADHERENCE_GLASSES)  attackValue *= 1.5;
        if(items == Items.THICK_BONE)         attackValue *= 2.0;

        /* スロースタート と よわき */
        if(abilities == Abilities.SLOW_START) attackValue *= 0.5;
        if(abilities == Abilities.DEFEATIST)  attackValue *= 0.5;

        /* 特性 1.5倍 */
        if(abilities == Abilities.GUTS)        attackValue *= 1.5;
        if(abilities == Abilities.HUSTLE)      attackValue *= 1.5;
        if(abilities == Abilities.SOLAR_POWER) attackValue *= 1.5;
        if(abilities == Abilities.FLOWER_GIFT) attackValue *= 1.5;
        if(abilities == Abilities.PLUS)        attackValue *= 1.5;
        if(abilities == Abilities.MINUS)       attackValue *= 1.5; 
    }
    
    protected void makeLevelValue() {
        /*
         * 【レベル定数】=【攻撃側のLv×0.4＋2(切捨)】
         */
        
        levelValue = baseLevelValue;
        
        levelValue = levelValue * 2 / 5;
        
        levelValue += 2;
    } 
    

    
    protected int calcRoundHalfDown(double a) {
        java.math.BigDecimal value = new java.math.BigDecimal(a);
        
        value = value.setScale(1, java.math.RoundingMode.HALF_DOWN);
        
        return value.intValue();
    }
        

    /** 急所でないダメージの確率 */
    protected final double probabilityWithoutCritical;
    /** 急所の確率 */
    protected final double probabilityWithCritical;

    protected int[] damageWithoutCritical = new int[16];
    protected int[] damageWithCritical    = new int[16];
    
    protected int[] SubstituteDamageWithoutCritical = new int[16];
    protected int[] SubstituteDamageWithCritical    = new int[16];
    
    protected Abilities abilities;
    protected Items items;
    /** ポケモンの状態 */
    protected Condition condition;


    /** baseLevelValue : 入力として得たレベル */
    protected int baseLevelValue;
    /** levelValue : 補正などがかかったレベル */
    protected int levelValue;

    /*
     * baseAttackValue : 能力値としての攻撃値 attackValue : 補正などがかかった攻撃値
     */
    protected int baseAttackValue;
    protected int attackValue;
    protected int attackRank;

    /*
     * baseDefenseValue : 能力値としての防御値 defenseValue : 補正などがかかった防御値
     */
    protected int baseDefenseValue;
    protected int defenseValue;
    protected int defenseRank;

    /*
     * basePowerValue : 表記されている威力
     * powerValue     : 補正などがかかった威力
     */
    protected int basePowerValue;
    protected int powerValue;
    
    
    /** 急所の出る確率 */

    protected static final double[] CRITICAL_PROBABILITY = 
        {
            0.0,           // rank=-1  0 
            0.08333333333, // rank= 0  1/12 
            0.16666666666, // rank= 1  1/6
            0.5,           // rank= 2  1/2
            1              // rank= 3  1/1
        
        };
    
    /* 
     * BW
     */
    /* 
    protected static final double[] CRITICAL_PROBABILITY = 
        {
            0.0,           // rank=-1  0 
            1.0/16.0, // rank= 0  1/12 
            1.0/8.0, // rank= 1  1/6
            1.0/4.0,           // rank= 2  1/2
            1.0/3.0,              // rank= 3  1/1
            1.0/2.0,
            1.0
        
        };
    //*/
    
    /** 急所ランク <br> -1,0,1,2,3*/
    protected int criticalRand;
    
    /**
     * 基礎のダメージ
     * ランダム要素をかける直前の数値を持ちます
     */
    protected int baseDamageValue;
    
    /** タイプ相性 */
    protected double typeCompatibility;

    /**
     * abilitiesを取得します
     * @return abilities
     */
    public Abilities getAbilities() {
        return this.abilities;
    }

    /**
     * abilitiesを設定します
     * @param abilities
     */
    public void setAbilities(Abilities abilities) {
        this.abilities = abilities;
    }

    /**
     * itemsを取得します
     * @return items
     */
    public Items getItems() {
        return this.items;
    }

    /**
     * itemsを設定します
     * @param items
     */
    public void setItems(Items items) {
        this.items = items;
    }

    /**
     * conditionを取得します
     * @return condition
     */
    public Condition getCondition() {
        return this.condition;
    }

    /**
     * conditionを設定します
     * @param condition
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    /**
     * baseLevelValueを取得します
     * @return baseLevelValue
     */
    public int getBaseLevelValue() {
        return this.baseLevelValue;
    }

    /**
     * baseLevelValueを設定します
     * @param baseLevelValue
     */
    public void setBaseLevelValue(int baseLevelValue) {
        this.baseLevelValue = baseLevelValue;
    }

    /**
     * baseAttackValueを取得します
     * @return baseAttackValue
     */
    public int getBaseAttackValue() {
        return this.baseAttackValue;
    }

    /**
     * baseAttackValueを設定します
     * @param baseAttackValue
     */
    public void setBaseAttackValue(int baseAttackValue) {
        this.baseAttackValue = baseAttackValue;
    }

    /**
     * attackRankを取得します
     * @return attackRank
     */
    public int getAttackRank() {
        return this.attackRank;
    }

    /**
     * attackRankを設定します
     * @param attackRank
     */
    public void setAttackRank(int attackRank) {
        this.attackRank = attackRank;
    }

    /**
     * baseDefenseValueを取得します
     * @return baseDefenseValue
     */
    public int getBaseDefenseValue() {
        return this.baseDefenseValue;
    }

    /**
     * baseDefenseValueを設定します
     * @param baseDefenseValue
     */
    public void setBaseDefenseValue(int baseDefenseValue) {
        this.baseDefenseValue = baseDefenseValue;
    }

    /**
     * defenseRankを取得します
     * @return defenseRank
     */
    public int getDefenseRank() {
        return this.defenseRank;
    }

    /**
     * defenseRankを設定します
     * @param defenseRank
     */
    public void setDefenseRank(int defenseRank) {
        this.defenseRank = defenseRank;
    }

    /**
     * basePowerValueを取得します
     * @return basePowerValue
     */
    public int getBasePowerValue() {
        return this.basePowerValue;
    }

    /**
     * basePowerValueを設定します
     * @param basePowerValue
     */
    public void setBasePowerValue(int basePowerValue) {
        this.basePowerValue = basePowerValue;
    }

    /**
     * criticalRandを取得します
     * @return criticalRand
     */
    public int getCriticalRand() {
        return this.criticalRand;
    }

    /**
     * criticalRandを設定します
     * @param criticalRand
     */
    public void setCriticalRand(int criticalRand) {
        this.criticalRand = criticalRand;
    }

    /**
     * baseDamageValueを取得します
     * @return baseDamageValue
     */
    public int getBaseDamageValue() {
        return this.baseDamageValue;
    }

    /**
     * baseDamageValueを設定します
     * @param baseDamageValue
     */
    public void setBaseDamageValue(int baseDamageValue) {
        this.baseDamageValue = baseDamageValue;
    }

    /**
     * typeCompatibilityを取得します
     * @return typeCompatibility
     */
    public double getTypeCompatibility() {
        return this.typeCompatibility;
    }

    /**
     * typeCompatibilityを設定します
     * @param typeCompatibility
     */
    public void setTypeCompatibility(double typeCompatibility) {
        this.typeCompatibility = typeCompatibility;
    }


}
