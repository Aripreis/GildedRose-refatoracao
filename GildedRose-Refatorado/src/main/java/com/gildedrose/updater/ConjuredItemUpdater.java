package com.gildedrose.updater;

/**
 * Estratégia para itens "Conjured" (Conjurados Magicamente):
 * perdem qualidade duas vezes mais rápido que os itens comuns
 * (-2 por dia dentro do prazo e -4 por dia após o vencimento),
 * sem nunca ficar com qualidade negativa.
 */
final class ConjuredItemUpdater extends DefaultItemUpdater {

    private static final int CONJURED_DEGRADATION_MULTIPLIER = 2;

    ConjuredItemUpdater() {
        super(NORMAL_DEGRADATION_RATE * CONJURED_DEGRADATION_MULTIPLIER);
    }
}
