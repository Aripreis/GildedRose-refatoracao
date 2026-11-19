package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Template Method que define o ciclo diário de atualização de um {@link Item}:
 * <ol>
 *     <li>atualiza a qualidade conforme a regra da categoria;</li>
 *     <li>decrementa o prazo de venda ({@code sellIn});</li>
 *     <li>aplica a regra adicional de qualidade caso o item esteja vencido.</li>
 * </ol>
 * Também concentra as regras de limite de qualidade (entre 0 e 50).
 */
abstract class AbstractItemUpdater implements ItemUpdater {

    static final int MIN_QUALITY = 0;
    static final int MAX_QUALITY = 50;

    @Override
    public final void update(Item item) {
        updateQuality(item);
        item.sellIn--;
        if (isExpired(item)) {
            updateQualityAfterExpiration(item);
        }
    }

    /** Regra de qualidade aplicada todos os dias, antes do decremento de {@code sellIn}. */
    protected abstract void updateQuality(Item item);

    /** Regra adicional de qualidade aplicada quando a data de venda já passou. */
    protected abstract void updateQualityAfterExpiration(Item item);

    /**
     * Aumenta a qualidade sem ultrapassar {@link #MAX_QUALITY}.
     * Itens que já estão no limite (ou acima dele) não são alterados.
     */
    protected final void increaseQuality(Item item, int amount) {
        if (item.quality < MAX_QUALITY) {
            item.quality = Math.min(MAX_QUALITY, item.quality + amount);
        }
    }

    /**
     * Diminui a qualidade sem ficar abaixo de {@link #MIN_QUALITY}.
     * Itens que já estão no limite (ou abaixo dele) não são alterados.
     */
    protected final void decreaseQuality(Item item, int amount) {
        if (item.quality > MIN_QUALITY) {
            item.quality = Math.max(MIN_QUALITY, item.quality - amount);
        }
    }

    private static boolean isExpired(Item item) {
        return item.sellIn < 0;
    }
}
