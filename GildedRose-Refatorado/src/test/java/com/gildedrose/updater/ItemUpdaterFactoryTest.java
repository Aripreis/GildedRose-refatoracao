package com.gildedrose.updater;

import com.gildedrose.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemUpdaterFactoryTest {

    private static ItemUpdater updaterFor(String name) {
        return ItemUpdaterFactory.forItem(new Item(name, 0, 0));
    }

    @Test
    void resolvesSpecialItemsByExactName() {
        assertInstanceOf(AgedBrieUpdater.class, updaterFor(ItemUpdaterFactory.AGED_BRIE));
        assertInstanceOf(BackstagePassUpdater.class, updaterFor(ItemUpdaterFactory.BACKSTAGE_PASS));
        assertInstanceOf(SulfurasUpdater.class, updaterFor(ItemUpdaterFactory.SULFURAS));
    }

    @ParameterizedTest
    @ValueSource(strings = { "Conjured Mana Cake", "Conjured Health Potion" })
    void resolvesConjuredItemsByPrefix(String name) {
        assertInstanceOf(ConjuredItemUpdater.class, updaterFor(name));
    }

    @ParameterizedTest
    @ValueSource(strings = { "+5 Dexterity Vest", "Elixir of the Mongoose", "Aged Brie Deluxe" })
    void fallsBackToDefaultForRegularItems(String name) {
        assertInstanceOf(DefaultItemUpdater.class, updaterFor(name));
    }

    @Test
    void rejectsNullItem() {
        assertThrows(NullPointerException.class, () -> ItemUpdaterFactory.forItem(null));
    }
}
