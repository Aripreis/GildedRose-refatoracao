package com.gildedrose;

import com.gildedrose.updater.ItemUpdaterFactory;

/**
 * Ponto de entrada do sistema de estoque: ao final de cada dia,
 * atualiza {@code sellIn} e {@code quality} de todos os itens.
 * A regra específica de cada item é delegada a uma estratégia
 * obtida via {@link ItemUpdaterFactory}.
 */
class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            ItemUpdaterFactory.forItem(item).update(item);
        }
    }
}
