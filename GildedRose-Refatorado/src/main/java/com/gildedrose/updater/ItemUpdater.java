package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Estratégia responsável por atualizar os atributos {@code sellIn} e {@code quality}
 * de um {@link Item} ao final de cada dia.
 */
@FunctionalInterface
public interface ItemUpdater {

    void update(Item item);
}
