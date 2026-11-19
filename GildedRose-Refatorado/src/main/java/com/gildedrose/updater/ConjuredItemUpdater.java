package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Estratégia para itens "Conjured" (Conjurados Magicamente):
 * - Diminuem a qualidade duas vezes mais rápido que itens comuns:
 *   -2 por dia antes do vencimento e -4 por dia após o vencimento.
 * - A qualidade nunca fica negativa.
 */
public class ConjuredItemUpdater extends AbstractItemUpdater {

    private static final int DAILY_DEGRADATION = 2;

    @Override
    public void update(Item item) {
        decreaseQuality(item, DAILY_DEGRADATION);
        decrementSellIn(item);
        if (isExpired(item)) {
            decreaseQuality(item, DAILY_DEGRADATION);
        }
    }
}
