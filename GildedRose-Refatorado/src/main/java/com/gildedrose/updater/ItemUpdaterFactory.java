package com.gildedrose.updater;

import com.gildedrose.Item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Seleciona a estratégia {@link ItemUpdater} adequada para cada {@link Item}.
 * <p>
 * Itens especiais são resolvidos pelo nome exato; itens cujo nome começa com
 * {@value #CONJURED_PREFIX} recebem a regra de conjurados; os demais recebem a regra padrão.
 * Para suportar uma nova categoria basta criar a estratégia e registrá-la aqui.
 */
public final class ItemUpdaterFactory {

    public static final String AGED_BRIE = "Aged Brie";
    public static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    public static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    public static final String CONJURED_PREFIX = "Conjured";

    private static final ItemUpdater DEFAULT_UPDATER = new DefaultItemUpdater();
    private static final ItemUpdater CONJURED_UPDATER = new ConjuredItemUpdater();
    private static final Map<String, ItemUpdater> UPDATERS_BY_NAME = createUpdatersByName();

    private ItemUpdaterFactory() {
        // Classe utilitária: não deve ser instanciada.
    }

    public static ItemUpdater forItem(Item item) {
        Objects.requireNonNull(item, "item must not be null");

        ItemUpdater specialUpdater = UPDATERS_BY_NAME.get(item.name);
        if (specialUpdater != null) {
            return specialUpdater;
        }
        if (isConjured(item)) {
            return CONJURED_UPDATER;
        }
        return DEFAULT_UPDATER;
    }

    private static boolean isConjured(Item item) {
        return item.name != null && item.name.startsWith(CONJURED_PREFIX);
    }

    private static Map<String, ItemUpdater> createUpdatersByName() {
        Map<String, ItemUpdater> updaters = new HashMap<>();
        updaters.put(AGED_BRIE, new AgedBrieUpdater());
        updaters.put(BACKSTAGE_PASS, new BackstagePassUpdater());
        updaters.put(SULFURAS, new SulfurasUpdater());
        return Collections.unmodifiableMap(updaters);
    }
}
