package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Estratégia para "Aged Brie" (Queijo Brie envelhecido):
 * <ul>
 *     <li>a qualidade aumenta {@value #APPRECIATION_RATE} por dia dentro do prazo;</li>
 *     <li>após a data de venda, aumenta mais {@value #APPRECIATION_RATE} (total de 2 por dia);</li>
 *     <li>a qualidade nunca ultrapassa 50.</li>
 * </ul>
 */
final class AgedBrieUpdater extends AbstractItemUpdater {

    private static final int APPRECIATION_RATE = 1;

    @Override
    protected void updateQuality(Item item) {
        increaseQuality(item, APPRECIATION_RATE);
    }

    @Override
    protected void updateQualityAfterExpiration(Item item) {
        increaseQuality(item, APPRECIATION_RATE);
    }
}
