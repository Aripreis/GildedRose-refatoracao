package com.gildedrose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    private Item updateOnce(String name, int sellIn, int quality) {
        Item[] items = new Item[] { new Item(name, sellIn, quality) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        return app.items[0];
    }

    @Nested
    @DisplayName("Itens comuns")
    class NormalItemsTests {

        @Test
        @DisplayName("Decrementa sellIn e quality em 1 antes da data de venda")
        void shouldDecreaseSellInAndQualityBeforeSellDate() {
            Item item = updateOnce("+5 Dexterity Vest", 10, 20);
            assertEquals(9, item.sellIn);
            assertEquals(19, item.quality);
        }

        @Test
        @DisplayName("Diminui quality duas vezes mais rápido após passar a data de venda")
        void shouldDegradeTwiceAsFastAfterSellDate() {
            Item item = updateOnce("Elixir of the Mongoose", 0, 10);
            assertEquals(-1, item.sellIn);
            assertEquals(8, item.quality);
        }

        @Test
        @DisplayName("Qualidade nunca fica negativa antes do vencimento")
        void shouldNeverHaveNegativeQuality() {
            Item item = updateOnce("Elixir of the Mongoose", 5, 0);
            assertEquals(4, item.sellIn);
            assertEquals(0, item.quality);
        }

        @Test
        @DisplayName("Qualidade nunca fica negativa mesmo após vencimento com quality = 1")
        void shouldNeverHaveNegativeQualityAfterSellDate() {
            Item item = updateOnce("Elixir of the Mongoose", 0, 1);
            assertEquals(-1, item.sellIn);
            assertEquals(0, item.quality);
        }
    }

    @Nested
    @DisplayName("Aged Brie (Queijo Brie envelhecido)")
    class AgedBrieTests {

        @Test
        @DisplayName("Aumenta quality em 1 a cada dia antes do vencimento")
        void shouldIncreaseQualityAsItAges() {
            Item item = updateOnce("Aged Brie", 2, 0);
            assertEquals(1, item.sellIn);
            assertEquals(1, item.quality);
        }

        @Test
        @DisplayName("Aumenta quality em 2 após a data de venda")
        void shouldIncreaseQualityTwiceAsFastAfterSellDate() {
            Item item = updateOnce("Aged Brie", 0, 10);
            assertEquals(-1, item.sellIn);
            assertEquals(12, item.quality);
        }

        @Test
        @DisplayName("Qualidade nunca ultrapassa 50")
        void shouldNeverExceedMaximumQuality() {
            Item item = updateOnce("Aged Brie", 5, 50);
            assertEquals(4, item.sellIn);
            assertEquals(50, item.quality);
        }

        @Test
        @DisplayName("Qualidade não ultrapassa 50 mesmo vencido com quality = 49")
        void shouldCapQualityAt50AfterSellDate() {
            Item item = updateOnce("Aged Brie", 0, 49);
            assertEquals(-1, item.sellIn);
            assertEquals(50, item.quality);
        }
    }

    @Nested
    @DisplayName("Sulfuras (Item lendário)")
    class SulfurasTests {

        @Test
        @DisplayName("Não altera sellIn nem quality (80)")
        void shouldNeverChangeSellInOrQuality() {
            Item item = updateOnce("Sulfuras, Hand of Ragnaros", 0, 80);
            assertEquals(0, item.sellIn);
            assertEquals(80, item.quality);
        }

        @Test
        @DisplayName("Mantém valores imutáveis mesmo com sellIn negativo")
        void shouldRemainUnchangedWithNegativeSellIn() {
            Item item = updateOnce("Sulfuras, Hand of Ragnaros", -1, 80);
            assertEquals(-1, item.sellIn);
            assertEquals(80, item.quality);
        }
    }

    @Nested
    @DisplayName("Backstage Passes (Entrada para os Bastidores)")
    class BackstagePassesTests {

        private static final String BACKSTAGE = "Backstage passes to a TAFKAL80ETC concert";

        @Test
        @DisplayName("Aumenta quality em 1 quando faltam mais de 10 dias")
        void shouldIncreaseBy1WhenMoreThan10DaysLeft() {
            Item item = updateOnce(BACKSTAGE, 15, 20);
            assertEquals(14, item.sellIn);
            assertEquals(21, item.quality);
        }

        @Test
        @DisplayName("Aumenta quality em 2 quando faltam 10 dias ou menos")
        void shouldIncreaseBy2When10DaysOrLess() {
            Item itemAt10 = updateOnce(BACKSTAGE, 10, 20);
            assertEquals(9, itemAt10.sellIn);
            assertEquals(22, itemAt10.quality);

            Item itemAt6 = updateOnce(BACKSTAGE, 6, 20);
            assertEquals(5, itemAt6.sellIn);
            assertEquals(22, itemAt6.quality);
        }

        @Test
        @DisplayName("Aumenta quality em 3 quando faltam 5 dias ou menos")
        void shouldIncreaseBy3When5DaysOrLess() {
            Item itemAt5 = updateOnce(BACKSTAGE, 5, 20);
            assertEquals(4, itemAt5.sellIn);
            assertEquals(23, itemAt5.quality);

            Item itemAt1 = updateOnce(BACKSTAGE, 1, 20);
            assertEquals(0, itemAt1.sellIn);
            assertEquals(23, itemAt1.quality);
        }

        @Test
        @DisplayName("Qualidade nunca ultrapassa 50 mesmo nos bônus de +2 e +3")
        void shouldNeverExceed50Quality() {
            Item itemAt10 = updateOnce(BACKSTAGE, 10, 49);
            assertEquals(9, itemAt10.sellIn);
            assertEquals(50, itemAt10.quality);

            Item itemAt5 = updateOnce(BACKSTAGE, 5, 49);
            assertEquals(4, itemAt5.sellIn);
            assertEquals(50, itemAt5.quality);
        }

        @Test
        @DisplayName("Zera quality quando a data de venda passa")
        void shouldDropQualityToZeroAfterConcert() {
            Item item = updateOnce(BACKSTAGE, 0, 40);
            assertEquals(-1, item.sellIn);
            assertEquals(0, item.quality);
        }
    }

    @Nested
    @DisplayName("Itens Conjurados (Conjured)")
    class ConjuredItemsTests {

        @Test
        @DisplayName("Diminui quality em 2 por dia (2x mais rápido que itens comuns) antes do vencimento")
        void shouldDegradeTwiceAsFastBeforeSellDate() {
            Item item = updateOnce("Conjured Mana Cake", 3, 6);
            assertEquals(2, item.sellIn);
            assertEquals(4, item.quality);
        }

        @Test
        @DisplayName("Diminui quality em 4 por dia após passar a data de venda")
        void shouldDegradeFourTimesAsFastAfterSellDate() {
            Item item = updateOnce("Conjured Mana Cake", 0, 10);
            assertEquals(-1, item.sellIn);
            assertEquals(6, item.quality);
        }

        @Test
        @DisplayName("Qualidade nunca fica negativa antes do vencimento (quality = 1)")
        void shouldNotDropBelowZeroBeforeSellDate() {
            Item item = updateOnce("Conjured Mana Cake", 3, 1);
            assertEquals(2, item.sellIn);
            assertEquals(0, item.quality);
        }

        @Test
        @DisplayName("Qualidade nunca fica negativa após o vencimento (quality = 3)")
        void shouldNotDropBelowZeroAfterSellDate() {
            Item item = updateOnce("Conjured Mana Cake", 0, 3);
            assertEquals(-1, item.sellIn);
            assertEquals(0, item.quality);
        }

        @Test
        @DisplayName("Aplica regra para qualquer item com prefixo Conjured")
        void shouldSupportAnyConjuredItemName() {
            Item item = updateOnce("Conjured Health Potion", 5, 10);
            assertEquals(4, item.sellIn);
            assertEquals(8, item.quality);
        }
    }

    @Test
    @DisplayName("Simulação de múltiplos dias com inventário misto")
    void shouldUpdateMultipleItemsOverMultipleDays() {
        Item[] items = new Item[] {
                new Item("+5 Dexterity Vest", 10, 20),
                new Item("Aged Brie", 2, 0),
                new Item("Sulfuras, Hand of Ragnaros", 0, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 11, 20),
                new Item("Conjured Mana Cake", 3, 6)
        };
        GildedRose app = new GildedRose(items);

        for (int day = 0; day < 3; day++) {
            app.updateQuality();
        }

        // +5 Dexterity Vest: sellIn 10 -> 7, quality 20 -> 17
        assertEquals(7, items[0].sellIn);
        assertEquals(17, items[0].quality);

        // Aged Brie: dia 1 (1,1), dia 2 (0,2), dia 3 (-1,4)
        assertEquals(-1, items[1].sellIn);
        assertEquals(4, items[1].quality);

        // Sulfuras: inalterado
        assertEquals(0, items[2].sellIn);
        assertEquals(80, items[2].quality);

        // Backstage: dia 1 (10, 21), dia 2 (9, 23), dia 3 (8, 25)
        assertEquals(8, items[3].sellIn);
        assertEquals(25, items[3].quality);

        // Conjured Mana Cake: dia 1 (2, 4), dia 2 (1, 2), dia 3 (0, 0)
        assertEquals(0, items[4].sellIn);
        assertEquals(0, items[4].quality);
    }
}
