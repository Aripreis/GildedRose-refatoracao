package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Estratégia para "Aged Brie" (Queijo Brie envelhecido):
 * - Aumenta a qualidade em 1 por dia enquanto dentro do prazo e em 2 após o vencimento.
 * - A qualidade nunca ultrapassa 50.
 */
public class AgedBrieUpdater extends AbstractItemUpdater {

    private static final int DAILY_APPRECIATION = 1;

    @Override
    public void update(Item item) {
        increaseQuality(item, DAILY_APPRECIATION);
        decrementSellIn(item);
        if (isExpired(item)) {
            increaseQuality(item, DAILY_APPRECIATION);
        }
    }
}
