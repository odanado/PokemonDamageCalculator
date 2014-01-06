/**
 * 　ジュエル系：威力に1.5倍、切り捨て。
　こだわりハチマキ：攻撃に1.5倍、切り捨て。
　ものしりメガネ：威力に1.1倍、四捨五入。
　いのちのたま：半減木の実の後に1.3倍。四捨五入。
　たつじんのおび：半減木の実の後に1.2倍、四捨五入。
　プレート系：威力に1.2倍、四捨五入。
　しんかのきせき：防御・特防に1.5倍、切り捨て。
　半減木の実：タイプ相性の後に0.5倍。

 */
package com.odanado.pokemon.calculator.damege;

/**
 * @author odan
 * 
 */
public enum Items {

    /** 手ぶら... */
    NONE,
    
    /** ジュエル系 <br> 威力に1.3倍(切捨) */
    JEWELS,
    
    /** こだわりハチマキ <br> 攻撃に1.5倍(切捨) */
    ADHERENCE_HEADBAND,
    
    /** こだわりメガネ <br> 攻撃に1.5倍(切捨) */
    ADHERENCE_GLASSES,
    
    /** ふといホネ <br> 攻撃に2.0倍(切捨) */
    THICK_BONE,
    
    /** ちからのハチマキ  <br> 威力に1.1倍(五捨)*/
    FOCUS_BAND,
    
    /** ものしりメガネ  <br> 威力に1.1倍(五捨) */
    WISDOM_GLASSES,
    
    /** いのちのたま <br> ダメージに1.3倍(最後に五捨) */
    LIFE_ORB,
    
    /** たつじんのおび <br> ダメージに1.2倍(最後に五捨) */
    EXPERT_BELT,
    
    /** プレート系 <br> 威力に1.2倍(五捨) */
    PLATES,
    
    /** しんかのきせき <br> 防御に1.5倍(切捨) */
    EVOLUTION_STONE,
    
    /** とつげきチョッキ <br> 防御に1.5倍(切捨) */
    ASSAULT_VEST,
    
    /** 半減の実 <br> ダメージに0.5倍(切捨)*/
    BERRIES,
    
    /** オボンのみ <br> 回復 */
    SITRUS_BERRY,
    
    /** きあいのタスキ <br> HPMAX時HP下限1 */
    FOCUS_SASH,
    
}
