package com.gildedrose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.gildedrose.updater.ItemUpdaterFactory.AGED_BRIE;
import static com.gildedrose.updater.ItemUpdaterFactory.BACKSTAGE_PASS;
import static com.gildedrose.updater.ItemUpdaterFactory.SULFURAS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    private static final String NORMAL_ITEM = "+5 Dexterity Vest";
    private static final String CONJURED_ITEM = "Conjured Mana Cake";

    private static Item updateOnce(String name, int sellIn, int quality) {
        Item item = new Item(name, sellIn, quality);
        new GildedRose(new Item[] { item }).updateQuality();
        return item;
    }

    private static void assertItem(Item item, int expectedSellIn, int expectedQuality) {
        assertAll(item.name,
                () -> assertEquals(expectedSellIn, item.sellIn, "sellIn"),
                () -> assertEquals(expectedQuality, item.quality, "quality"));
    }

    @Nested
    @DisplayName("Itens comuns")
    class NormalItems {

        @ParameterizedTest(name = "sellIn={0}, quality={1} -> sellIn={2}, quality={3}")
        @CsvSource({
                "10, 20,  9, 19", // dentro do prazo: -1
                " 1, 20,  0, 19", // último dia do prazo: -1
                " 0, 10, -1,  8", // vencido: -2
                "-5, 10, -6,  8", // continua vencido: -2
                " 5,  0,  4,  0", // nunca negativa
                " 0,  1, -1,  0", // nunca negativa após vencimento
        })
        void degradesQuality(int sellIn, int quality, int expectedSellIn, int expectedQuality) {
            assertItem(updateOnce(NORMAL_ITEM, sellIn, quality), expectedSellIn, expectedQuality);
        }
    }

    @Nested
    @DisplayName("Aged Brie")
    class AgedBrie {

        @ParameterizedTest(name = "sellIn={0}, quality={1} -> sellIn={2}, quality={3}")
        @CsvSource({
                " 2,  0,  1,  1", // dentro do prazo: +1
                " 0, 10, -1, 12", // vencido: +2
                " 5, 50,  4, 50", // nunca ultrapassa 50
                " 0, 49, -1, 50", // nunca ultrapassa 50 após vencimento
        })
        void increasesQualityAsItAges(int sellIn, int quality, int expectedSellIn, int expectedQuality) {
            assertItem(updateOnce(AGED_BRIE, sellIn, quality), expectedSellIn, expectedQuality);
        }
    }

    @Nested
    @DisplayName("Sulfuras")
    class Sulfuras {

        @ParameterizedTest(name = "sellIn={0} permanece inalterado")
        @ValueSource(ints = { 10, 0, -1 })
        void neverChanges(int sellIn) {
            assertItem(updateOnce(SULFURAS, sellIn, 80), sellIn, 80);
        }
    }

    @Nested
    @DisplayName("Backstage passes")
    class BackstagePasses {

        @ParameterizedTest(name = "sellIn={0}, quality={1} -> sellIn={2}, quality={3}")
        @CsvSource({
                "15, 20, 14, 21", // mais de 10 dias: +1
                "11, 20, 10, 21", // limite: ainda +1
                "10, 20,  9, 22", // 10 dias ou menos: +2
                " 6, 20,  5, 22", // limite: ainda +2
                " 5, 20,  4, 23", // 5 dias ou menos: +3
                " 1, 20,  0, 23", // último dia: +3
                "10, 49,  9, 50", // nunca ultrapassa 50 (+2)
                " 5, 48,  4, 50", // nunca ultrapassa 50 (+3)
                " 0, 40, -1,  0", // após o show: 0
        })
        void increasesQualityAsConcertApproaches(int sellIn, int quality, int expectedSellIn, int expectedQuality) {
            assertItem(updateOnce(BACKSTAGE_PASS, sellIn, quality), expectedSellIn, expectedQuality);
        }
    }

    @Nested
    @DisplayName("Itens conjurados")
    class ConjuredItems {

        @ParameterizedTest(name = "sellIn={0}, quality={1} -> sellIn={2}, quality={3}")
        @CsvSource({
                " 3,  6,  2,  4", // dentro do prazo: -2 (2x item comum)
                " 0, 10, -1,  6", // vencido: -4 (2x item comum)
                " 3,  1,  2,  0", // nunca negativa
                " 0,  3, -1,  0", // nunca negativa após vencimento
        })
        void degradeTwiceAsFastAsNormalItems(int sellIn, int quality, int expectedSellIn, int expectedQuality) {
            assertItem(updateOnce(CONJURED_ITEM, sellIn, quality), expectedSellIn, expectedQuality);
        }

        @Test
        void applyToAnyItemNameStartingWithConjured() {
            assertItem(updateOnce("Conjured Health Potion", 5, 10), 4, 8);
        }
    }

    @Test
    @DisplayName("Simulação de múltiplos dias com inventário misto")
    void updatesMixedInventoryOverSeveralDays() {
        Item vest = new Item(NORMAL_ITEM, 10, 20);
        Item brie = new Item(AGED_BRIE, 2, 0);
        Item sulfuras = new Item(SULFURAS, 0, 80);
        Item backstage = new Item(BACKSTAGE_PASS, 11, 20);
        Item conjured = new Item(CONJURED_ITEM, 3, 6);
        GildedRose app = new GildedRose(new Item[] { vest, brie, sulfuras, backstage, conjured });

        for (int day = 0; day < 3; day++) {
            app.updateQuality();
        }

        assertItem(vest, 7, 17);      // -1 por dia
        assertItem(brie, -1, 4);      // +1, +1, +2 (vencido no 3º dia)
        assertItem(sulfuras, 0, 80);  // inalterado
        assertItem(backstage, 8, 25); // +1, +2, +2
        assertItem(conjured, 0, 0);   // -2, -2, -2
    }
}
