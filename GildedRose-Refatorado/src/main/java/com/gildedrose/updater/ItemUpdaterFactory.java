package com.gildedrose.updater;

import com.gildedrose.Item;

/**
 * Fábrica responsável por selecionar a estratégia {@link ItemUpdater} adequada
 * conforme o tipo/nome do {@link Item}.
 */
public final class ItemUpdaterFactory {

    public static final String AGED_BRIE = "Aged Brie";
    public static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    public static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    public static final String CONJURED_PREFIX = "Conjured";

    private static final ItemUpdater DEFAULT_UPDATER = new DefaultItemUpdater();
    private static final ItemUpdater AGED_BRIE_UPDATER = new AgedBrieUpdater();
    private static final ItemUpdater BACKSTAGE_PASS_UPDATER = new BackstagePassUpdater();
    private static final ItemUpdater SULFURAS_UPDATER = new SulfurasUpdater();
    private static final ItemUpdater CONJURED_UPDATER = new ConjuredItemUpdater();

    private ItemUpdaterFactory() {
    }

    public static ItemUpdater forItem(Item item) {
        if (item == null || item.name == null) {
            return DEFAULT_UPDATER;
        }
        if (AGED_BRIE.equals(item.name)) {
            return AGED_BRIE_UPDATER;
        }
        if (BACKSTAGE_PASS.equals(item.name)) {
            return BACKSTAGE_PASS_UPDATER;
        }
        if (SULFURAS.equals(item.name)) {
            return SULFURAS_UPDATER;
        }
        if (item.name.startsWith(CONJURED_PREFIX)) {
            return CONJURED_UPDATER;
        }
        return DEFAULT_UPDATER;
    }
}
