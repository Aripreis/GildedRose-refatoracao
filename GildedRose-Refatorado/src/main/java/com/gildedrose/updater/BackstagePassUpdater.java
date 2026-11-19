package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Estratégia para "Backstage passes" (Entrada para os Bastidores):
 * - Aumenta a qualidade em 1 quando sellIn > 10.
 * - Aumenta a qualidade em 2 quando sellIn <= 10 e > 5.
 * - Aumenta a qualidade em 3 quando sellIn <= 5 e > 0.
 * - Zera a qualidade imediatamente quando a data de venda passa (sellIn < 0 após decremento).
 * - A qualidade nunca ultrapassa 50.
 */
public class BackstagePassUpdater extends AbstractItemUpdater {

    private static final int TEN_DAYS_THRESHOLD = 10;
    private static final int FIVE_DAYS_THRESHOLD = 5;

    @Override
    public void update(Item item) {
        increaseQuality(item, 1);

        if (item.sellIn <= TEN_DAYS_THRESHOLD) {
            increaseQuality(item, 1);
        }

        if (item.sellIn <= FIVE_DAYS_THRESHOLD) {
            increaseQuality(item, 1);
        }

        decrementSellIn(item);

        if (isExpired(item)) {
            item.quality = MIN_QUALITY;
        }
    }
}
