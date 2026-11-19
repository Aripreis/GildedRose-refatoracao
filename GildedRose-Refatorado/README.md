# Gilded Rose — Refatoração em Java

Este diretório contém a versão refatorada do projeto **Gilded Rose** em Java, criada de forma isolada a partir dos arquivos originais localizados em `GildedRose-Refactoring-Kata-main/Java`.

## Objetivos da Refatoração

1. **Eliminar a complexidade ciclomática excessiva** do método `updateQuality()` original (condicionais aninhadas e strings mágicas repetidas).
2. **Aplicar princípios SOLID** (especialmente Responsabilidade Única — SRP — e Aberto/Fechado — OCP) por meio dos padrões **Strategy** e **Factory**.
3. **Preservar intactos a classe `Item` e o atributo `Item[] items` em `GildedRose`**, mantendo total compatibilidade com o código legado e com a especificação original.
4. **Implementar a nova categoria de itens `Conjured` (Conjurados Magicamente)**, que perdem qualidade duas vezes mais rápido que os itens comuns.
5. **Garantir segurança nas alterações com testes unitários abrangentes** em JUnit 5.

## Estrutura do Projeto Refatorado

```text
GildedRose-Refatorado/
├── pom.xml
├── build.gradle
├── settings.gradle
├── gradle.properties
├── README.md
└── src/
    ├── main/java/com/gildedrose/
    │   ├── Item.java                        # Mantida inalterada (regra original)
    │   ├── GildedRose.java                  # Delega a atualização diária às estratégias
    │   └── updater/
    │       ├── ItemUpdater.java             # Interface Strategy para atualização de itens
    │       ├── AbstractItemUpdater.java     # Operações comuns e limites de qualidade (0..50)
    │       ├── DefaultItemUpdater.java      # Itens comuns (-1/dia; -2/dia após vencimento)
    │       ├── AgedBrieUpdater.java         # Aged Brie (+1/dia; +2/dia após vencimento, máx 50)
    │       ├── BackstagePassUpdater.java    # Backstage Passes (+1, +2 se <=10, +3 se <=5, zera se vencido)
    │       ├── SulfurasUpdater.java         # Sulfuras (lendário, imutável, quality 80)
    │       ├── ConjuredItemUpdater.java     # Itens Conjurados (-2/dia; -4/dia após vencimento)
    │       └── ItemUpdaterFactory.java      # Seleciona a estratégia adequada para cada item
    └── test/java/com/gildedrose/
        ├── GildedRoseTest.java              # Suíte completa de testes unitários (JUnit 5)
        └── TexttestFixture.java             # Simulação diária do estoque
```

## Regras de Negócio Atendidas

| Categoria | Comportamento de `sellIn` | Comportamento de `quality` | Limites |
| :--- | :--- | :--- | :--- |
| **Item Comum** | `-1` por dia | `-1` antes do vencimento; `-2` após o vencimento | Mínimo `0` |
| **Aged Brie** | `-1` por dia | `+1` antes do vencimento; `+2` após o vencimento | Máximo `50` |
| **Sulfuras** | Inalterado | Inalterado | Imutável (`80`) |
| **Backstage Passes** | `-1` por dia | `+1` (`sellIn > 10`); `+2` (`sellIn <= 10`); `+3` (`sellIn <= 5`); `0` após vencimento | Entre `0` e `50` |
| **Conjured** | `-1` por dia | `-2` antes do vencimento; `-4` após o vencimento | Mínimo `0` |

## Como Compilar e Executar

### Via Maven

```bash
mvn test
```

### Via Gradle

```bash
gradle test
gradle texttest
```

### Via `javac` / `java` (sem dependências externas para executar a simulação)

```bash
javac -d out src/main/java/com/gildedrose/*.java src/main/java/com/gildedrose/updater/*.java src/test/java/com/gildedrose/TexttestFixture.java
java -cp out com.gildedrose.TexttestFixture 5
```
