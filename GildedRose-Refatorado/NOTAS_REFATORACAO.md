# Notas de Revisão — Clean Code

Revisão da primeira versão refatorada (commit `e9311ff`) com foco em Clean Code.
Nenhuma regra de negócio mudou: a equivalência com o código legado foi revalidada (ver [Verificação](#verificação)).

## Resumo das alterações

| # | Ponto identificado | Alteração | Princípio |
| :-: | :--- | :--- | :--- |
| 1 | Todas as estratégias repetiam a mesma sequência: *atualizar qualidade → decrementar `sellIn` → se vencido, atualizar de novo*. | [`AbstractItemUpdater`](src/main/java/com/gildedrose/updater/AbstractItemUpdater.java) virou um **Template Method**: `update()` é `final` e define o ciclo; as subclasses só implementam `updateQuality()` e `updateQualityAfterExpiration()`. | DRY, Template Method |
| 2 | `increaseQuality`/`decreaseQuality` usavam um laço `for` incrementando de 1 em 1 para respeitar os limites. | Substituído por `Math.min`/`Math.max` com guarda, mais legível e sem laço. A guarda preserva o comportamento legado para valores fora da faixa (ex.: `quality = 52` não é rebaixada para 50). | Legibilidade |
| 3 | [`ConjuredItemUpdater`](src/main/java/com/gildedrose/updater/ConjuredItemUpdater.java) era cópia de [`DefaultItemUpdater`](src/main/java/com/gildedrose/updater/DefaultItemUpdater.java) com outro número. | `DefaultItemUpdater` recebe a taxa de degradação no construtor; `ConjuredItemUpdater` apenas declara `NORMAL_DEGRADATION_RATE * 2`, expressando literalmente o requisito "duas vezes mais rápido". | DRY, código autoexplicativo |
| 4 | [`BackstagePassUpdater`](src/main/java/com/gildedrose/updater/BackstagePassUpdater.java) somava `+1` três vezes com `if`s cumulativos e o literal `1` repetido. | Novo método `qualityIncreaseFor(sellIn)` retorna 3, 2 ou 1 com constantes nomeadas, espelhando a tabela do requisito. | Nomes significativos, sem números mágicos |
| 5 | [`ItemUpdaterFactory`](src/main/java/com/gildedrose/updater/ItemUpdaterFactory.java) usava uma cadeia de `if`s e devolvia a regra padrão silenciosamente para `item == null`. | Registro imutável `Map<nome, estratégia>` + regra de prefixo `Conjured`; `item` nulo agora falha rápido com `NullPointerException` (o legado também lançava NPE). | OCP, fail-fast |
| 6 | Estratégias concretas e a classe base eram `public`, expondo detalhes de implementação. | Passaram a ser *package-private* (e `final` quando não há herança). A API pública do pacote é apenas `ItemUpdater` + `ItemUpdaterFactory`. | Encapsulamento |
| 7 | `GildedRose` tinha sido tornada `public` sem necessidade. | Restaurada a visibilidade original (*package-private*), como no código do Goblin. Adicionado Javadoc. | Mínima alteração da API |
| 8 | Testes com cenários duplicados (vários `assertEquals` copiados por método) e strings de nomes repetidas. | [`GildedRoseTest`](src/test/java/com/gildedrose/GildedRoseTest.java) reescrito com `@ParameterizedTest` + `@CsvSource` (uma linha por regra/limite, comentada), helper único `assertItem` com `assertAll`, e uso das constantes da fábrica. Novos casos de limite (`sellIn` 11, 6, 1; Backstage com `quality` 48). | Testes legíveis (F.I.R.S.T.) |
| 9 | A seleção de estratégia não tinha teste próprio. | Novo [`ItemUpdaterFactoryTest`](src/test/java/com/gildedrose/updater/ItemUpdaterFactoryTest.java): nomes exatos, prefixo `Conjured`, fallback padrão (incluindo `"Aged Brie Deluxe"`) e `null`. | Cobertura |
| 10 | `build.gradle` usava `main = ...` em `JavaExec` (removido no Gradle 8, versão usada pelo wrapper do projeto original) e `sourceCompatibility` no topo (obsoleto). | Trocado por `mainClass`, bloco `java { }`, `tasks.register` e dependência agregada `junit-jupiter` na mesma versão do Maven (5.8.2). | Build reproduzível |

## Verificação

- **JUnit 5 (console launcher 1.8.2)**, código compilado com `javac --release 8 -Xlint:all` sem avisos: **35 testes, 35 aprovados**.
- **Golden Master** contra o [`GildedRose.java` legado](../GildedRose-Refactoring-Kata-main/Java/src/main/java/com/gildedrose/GildedRose.java): 5 itens × `sellIn` de −3 a 16 × 13 qualidades (incluindo valores inválidos −1, 51, 52 e 80) × 25 dias = **32.500 estados idênticos**.
- `TexttestFixture` executada: `Conjured Mana Cake` vai de `6 → 4 → 2 → 0`.
