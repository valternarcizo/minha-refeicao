# Minha Refeição

Projeto de Software (Universidade Presbiteriana Mackenzie, 2026/2), caso de uso **Assinar Plano de Refeições**.

| Integrante | RA |
|---|---|
| Gustavo Domingues | 10743453 |
| Valter Augusto | 10736233 |

A documentação das entregas (protótipo, diagrama de classes de domínio, diagramas de sequência, diagramas de classes de projeto e implementação) está na [Wiki do repositório](https://github.com/valternarcizo/minha-refeicao/wiki).

A **Entrega 4 (N2)** é a implementação em Java do caso de uso, feita a partir dos diagramas de sequência e do [Diagrama de Classes de Projeto Consolidado](https://github.com/valternarcizo/minha-refeicao/wiki/Diagrama-de-Classes-de-Projeto-Consolidado). A rastreabilidade entre os diagramas e o código está na página [N2 - Entrega 4 - Implementação do Cenário do Caso de Uso](https://github.com/valternarcizo/minha-refeicao/wiki/N2-%E2%80%90-Entrega-4-%E2%80%90-Implementa%C3%A7%C3%A3o-do-Cen%C3%A1rio-do-Caso-de-Uso).

## Requisitos

- **JDK 17 ou superior** (testado com JDK 17 e 21). Não há bibliotecas externas.
- Terminal com UTF-8, para os acentos aparecerem corretamente (os scripts já configuram isso).

Para conferir a versão do Java: `java -version` e `javac -version`.

## Como executar

Os comandos devem ser executados **na pasta raiz do projeto** (a pasta onde está este README), porque o programa lê e grava os arquivos da pasta `dados/`.

### Opção 1: scripts prontos

Windows:

```bat
compilar-e-executar.bat
```

Linux / macOS:

```bash
bash compilar-e-executar.sh
```

Para rodar a demonstração automática (fluxo principal e fluxos alternativos), acrescente `--demo`. Para rodar só um roteiro, use `--demo 1` a `--demo 5`.

```bash
bash compilar-e-executar.sh --demo
bash compilar-e-executar.sh --demo 4
```

### Opção 2: comandos manuais

```bash
# 1) compilar (Linux/macOS)
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")

# 1) compilar (Windows PowerShell)
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src\main\java -Filter *.java).FullName

# 2) executar o menu interativo
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out br.mackenzie.minharefeicao.App

# 2) ou executar a demonstração automática
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out br.mackenzie.minharefeicao.App --demo
```

### Opção 3: IDE (IntelliJ, Eclipse ou VS Code)

Abra a pasta do projeto, marque `src/main/java` como pasta de código-fonte e execute a classe `br.mackenzie.minharefeicao.App`. Confira se o diretório de trabalho (*working directory*) da execução é a pasta raiz do projeto.

## Menu da aplicação

```
[1] Assinar plano de refeições          -> percorre as Telas 1 a 11 do protótipo
[2] Consultar assinaturas gravadas      -> lê dados/assinaturas.csv
[3] Executar demonstração automática    -> roteiros 1 a 5 (fluxo principal, FA1, FA2 e FA3)
[0] Sair
```

O envio de SMS, a consulta de CEP e a operadora de cartão são **simulados**: o código de confirmação aparece no console como `[SMS simulado ...]`.

## Dados de teste

| O que testar | Como |
|---|---|
| Fluxo principal | celular `(11) 99999-9999`, plano `3`, preferências `1,2`, pratos `1x8, 2x6, 5x6`, CEP `01234-567`, cartão `4111 1111 1111 1111`, validade `12/30`, CVV `123` |
| **FA1** - código inválido | digite um código errado 3 vezes: a validação é encerrada e um novo código é enviado |
| **FA2** - refeições acima do plano | no Plano Semanal (5 refeições), escolha `1x4, 2x3` (7 refeições) |
| CEP não encontrado | qualquer CEP fora de `dados/ceps.csv`, por exemplo `99999-999` |
| **FA3** - pagamento não autorizado | cartão `5555 5555 5555 0002` (limite insuficiente), número inválido (ex.: `4111 1111 1111 1112`) ou validade vencida; depois informe outro cartão ou escolha `2` para desistir |

Cartões aceitos pela operadora simulada: `4111 1111 1111 1111` (Visa), `5555 5555 5555 4444` (Mastercard), com validade futura no formato `MM/AA` e CVV de 3 dígitos.

## Persistência (arquivos CSV em `dados/`)

| Arquivo | Conteúdo | Quem lê / grava |
|---|---|---|
| `planos.csv` | planos (o plano inativo não aparece na Tela 3) | `ColecaoPlanos` (leitura) |
| `preferencias.csv` | preferências alimentares | `ColecaoPreferencias` (leitura) |
| `cardapio.csv` | pratos, acompanhamentos e sobremesas, e as preferências que cada prato atende | `ColecaoCardapios` (leitura) |
| `ceps.csv` | base de CEPs usada pelo serviço de CEP simulado | `ServicoConsultaCEP` (leitura) |
| `assinantes.csv` | assinantes cadastrados (criado na execução) | `ColecaoAssinantes` (leitura e gravação) |
| `assinaturas.csv` | assinaturas confirmadas com plano, status, itens, endereço e protocolo (criado na execução) | `ColecaoAssinaturas` (gravação) |
| `pagamentos.csv` | tentativas de pagamento aprovadas e recusadas (criado na execução) | `ColecaoAssinaturas` (gravação) |

Somente as coleções e o serviço de CEP usam a classe técnica `persistencia.ArquivoCSV`; as entidades não conhecem arquivos. O número completo do cartão e o CVV não são gravados (só os 4 últimos dígitos). Para recomeçar do zero, apague `assinantes.csv`, `assinaturas.csv` e `pagamentos.csv`.

## Estrutura do projeto

```
minha-refeicao/
├── compilar-e-executar.bat / .sh
├── dados/                          arquivos CSV (persistência)
├── docs/                           protótipo, diagramas (draw.io) e checklists
└── src/main/java/br/mackenzie/minharefeicao/
    ├── App.java                    menu e montagem dos objetos
    ├── Demonstracao.java           roteiros automáticos (fluxo principal e alternativos)
    ├── fronteira/                  «fronteira»  TelaIdentificacao, TelaPlanos, TelaPreferencias,
    │                                            TelaMontagemPedido, TelaEndereco, TelaPagamento
    ├── controle/                   «controle»   ControladorAssinatura
    ├── colecao/                    «coleção»    ColecaoAssinantes, ColecaoPlanos, ColecaoPreferencias,
    │                                            ColecaoCardapios, ColecaoAssinaturas
    ├── servico/                    «serviço»    ServicoSMS, ServicoConsultaCEP, ProcessadorPagamento,
    │                                            GeradorProtocolo, OperadoraCartao (sistema externo)
    ├── dominio/                    «entidade»   Assinante, CodigoConfirmacao, Plano, Assinatura, Pedido,
    │                                            ItemPedido, ItemCardapio (+3 subclasses), Cardapio,
    │                                            PreferenciaAlimentar, EnderecoEntrega, Pagamento,
    │                                            CartaoCredito, Protocolo e as 3 enumerações de status
    └── persistencia/               ArquivoCSV (leitura e gravação dos arquivos)
```

Os arquivos de apoio (protótipo em PDF e diagramas em draw.io) estão na pasta `docs/`.
