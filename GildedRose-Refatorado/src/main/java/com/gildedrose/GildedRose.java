package com.gildedrose;

import com.gildedrose.updater.ItemUpdaterFactory;

public class GildedRose {
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
