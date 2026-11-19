package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Classe base com operações utilitárias compartilhadas entre as estratégias de atualização,
 * garantindo os limites de qualidade (entre 0 e 50) e controle de vencimento.
 */
public abstract class AbstractItemUpdater implements ItemUpdater {

    protected static final int MIN_QUALITY = 0;
    protected static final int MAX_QUALITY = 50;

    protected void increaseQuality(Item item, int amount) {
        for (int i = 0; i < amount; i++) {
            if (item.quality < MAX_QUALITY) {
                item.quality++;
            }
        }
    }

    protected void decreaseQuality(Item item, int amount) {
        for (int i = 0; i < amount; i++) {
            if (item.quality > MIN_QUALITY) {
                item.quality--;
            }
        }
    }

    protected void decrementSellIn(Item item) {
        item.sellIn--;
    }

    protected boolean isExpired(Item item) {
        return item.sellIn < 0;
    }
}
