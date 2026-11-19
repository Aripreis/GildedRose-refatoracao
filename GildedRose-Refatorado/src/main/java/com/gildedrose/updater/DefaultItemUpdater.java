package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Estratégia padrão para itens comuns:
 * - Decrementa sellIn em 1 por dia.
 * - Decrementa quality em 1 antes da data de venda e em 2 após o vencimento.
 * - A qualidade nunca fica negativa.
 */
public class DefaultItemUpdater extends AbstractItemUpdater {

    private static final int DAILY_DEGRADATION = 1;

    @Override
    public void update(Item item) {
        decreaseQuality(item, DAILY_DEGRADATION);
        decrementSellIn(item);
        if (isExpired(item)) {
            decreaseQuality(item, DAILY_DEGRADATION);
        }
    }
}
