package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Estratégia para "Backstage passes" (Entrada para os Bastidores):
 * <ul>
 *     <li>+1 de qualidade quando faltam mais de 10 dias;</li>
 *     <li>+2 quando faltam 10 dias ou menos;</li>
 *     <li>+3 quando faltam 5 dias ou menos;</li>
 *     <li>a qualidade cai para 0 após o show (data de venda passada);</li>
 *     <li>a qualidade nunca ultrapassa 50.</li>
 * </ul>
 */
final class BackstagePassUpdater extends AbstractItemUpdater {

    private static final int TEN_DAYS = 10;
    private static final int FIVE_DAYS = 5;

    private static final int REGULAR_INCREASE = 1;
    private static final int TEN_DAYS_OR_LESS_INCREASE = 2;
    private static final int FIVE_DAYS_OR_LESS_INCREASE = 3;

    @Override
    protected void updateQuality(Item item) {
        increaseQuality(item, qualityIncreaseFor(item.sellIn));
    }

    @Override
    protected void updateQualityAfterExpiration(Item item) {
        item.quality = MIN_QUALITY;
    }

    private static int qualityIncreaseFor(int sellIn) {
        if (sellIn <= FIVE_DAYS) {
            return FIVE_DAYS_OR_LESS_INCREASE;
        }
        if (sellIn <= TEN_DAYS) {
            return TEN_DAYS_OR_LESS_INCREASE;
        }
        return REGULAR_INCREASE;
    }
}
