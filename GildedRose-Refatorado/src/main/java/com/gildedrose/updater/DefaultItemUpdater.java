package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Estratégia padrão para itens comuns:
 * <ul>
 *     <li>a qualidade diminui {@value #NORMAL_DEGRADATION_RATE} por dia dentro do prazo;</li>
 *     <li>após a data de venda, a qualidade diminui duas vezes mais rápido;</li>
 *     <li>a qualidade nunca fica negativa.</li>
 * </ul>
 */
class DefaultItemUpdater extends AbstractItemUpdater {

    static final int NORMAL_DEGRADATION_RATE = 1;

    private final int degradationRate;

    DefaultItemUpdater() {
        this(NORMAL_DEGRADATION_RATE);
    }

    protected DefaultItemUpdater(int degradationRate) {
        this.degradationRate = degradationRate;
    }

    @Override
    protected void updateQuality(Item item) {
        decreaseQuality(item, degradationRate);
    }

    @Override
    protected void updateQualityAfterExpiration(Item item) {
        decreaseQuality(item, degradationRate);
    }
}
