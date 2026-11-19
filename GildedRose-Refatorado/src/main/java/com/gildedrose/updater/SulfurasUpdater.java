package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Estratégia para "Sulfuras" (item lendário):
 * não possui prazo de venda a decrementar e sua qualidade (80) permanece imutável.
 */
final class SulfurasUpdater implements ItemUpdater {

    @Override
    public void update(Item item) {
        // Item lendário: sellIn e quality nunca são alterados.
    }
}
