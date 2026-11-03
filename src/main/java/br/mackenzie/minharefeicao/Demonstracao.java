package br.mackenzie.minharefeicao;

import br.mackenzie.minharefeicao.controle.ControladorAssinatura;
import br.mackenzie.minharefeicao.fronteira.Console;
import br.mackenzie.minharefeicao.fronteira.TelaEndereco;
import br.mackenzie.minharefeicao.fronteira.TelaIdentificacao;
import br.mackenzie.minharefeicao.fronteira.TelaMontagemPedido;
import br.mackenzie.minharefeicao.fronteira.TelaPagamento;
import br.mackenzie.minharefeicao.fronteira.TelaPlanos;
import br.mackenzie.minharefeicao.fronteira.TelaPreferencias;
import br.mackenzie.minharefeicao.servico.ServicoSMS;

import java.util.Arrays;

/**
 * Demonstração automática: o "ator" envia às telas os mesmos estímulos dos diagramas
 * de sequência, exercitando o fluxo principal e os fluxos alternativos FA1, FA2 e FA3.
 */
public final class Demonstracao {

    private Demonstracao() { }

    /** Executa todos os roteiros. */
    public static void executar() {
        executar(0);
    }

    /** Executa um roteiro (1 a 5) ou todos (0). */
    public static void executar(int numero) {
        if (numero == 0 || numero == 1) roteiro("ROTEIRO 1 - Fluxo principal (Cenários 1 a 6)", "(11) 99999-9999", false, false, false, false);
        if (numero == 0 || numero == 2) roteiro("ROTEIRO 2 - FA1: código inválido 3 vezes e novo código", "(11) 98888-0002", true, false, false, false);
        if (numero == 0 || numero == 3) roteiro("ROTEIRO 3 - FA2: refeições acima do plano + CEP não encontrado", "(11) 97777-0003", false, true, false, false);
        if (numero == 0 || numero == 4) roteiro("ROTEIRO 4 - FA3: pagamento recusado e novo cartão aprovado", "(11) 96666-0004", false, false, true, false);
        if (numero == 0 || numero == 5) roteiro("ROTEIRO 5 - FA3: pagamento recusado e assinante desiste", "(11) 95555-0005", false, false, true, true);
        System.out.println();
        System.out.println("Demonstração concluída. Veja os arquivos dados/assinantes.csv, dados/assinaturas.csv e dados/pagamentos.csv.");
    }

    private static void roteiro(String titulo, String celular, boolean fa1, boolean fa2, boolean fa3, boolean desiste) {
        System.out.println();
        System.out.println("#".repeat(78));
        System.out.println("# " + titulo);
        System.out.println("#".repeat(78));

        Console console = new Console(null);
        ServicoSMS sms = new ServicoSMS();
        ControladorAssinatura controlador = App.criarControlador(sms);

        // Cenário 1 - Telas 1 e 2
        TelaIdentificacao telaIdentificacao = new TelaIdentificacao(controlador, console);
        console.tela("Tela 1 - Bem-vindo!");
        estimulo(console, "informarCelular(\"" + celular + "\")");
        telaIdentificacao.informarCelular(celular);
        if (fa1) {
            String errado = sms.ultimoCodigoEnviado(celular).equals("0000") ? "1111" : "0000";
            for (int i = 1; i <= 3; i++) {
                estimulo(console, "informarCodigo(\"" + errado + "\")  // tentativa " + i);
                telaIdentificacao.informarCodigo(errado);
            }
            estimulo(console, "informarCelular(\"" + celular + "\")  // pede um novo código");
            telaIdentificacao.informarCelular(celular);
        }
        String codigo = sms.ultimoCodigoEnviado(celular);
        estimulo(console, "informarCodigo(\"" + codigo + "\")");
        telaIdentificacao.informarCodigo(codigo);

        // Cenário 2 - Tela 3
        String plano = fa2 ? "Plano Semanal" : "Plano Mensal";
        estimulo(console, "selecionarPlano(\"" + plano + "\")");
        new TelaPlanos(controlador, console).selecionarPlano(plano);

        // Cenário 3 - Tela 4
        TelaPreferencias telaPreferencias = new TelaPreferencias(controlador, console);
        String[] prefs = {"Tradicional", "Vegetariana"};
        estimulo(console, "marcarPreferencias(" + Arrays.toString(prefs) + ")");
        telaPreferencias.marcarPreferencias(prefs);
        estimulo(console, "continuar()");
        telaPreferencias.continuar();

        // Cenário 4 - Telas 5 a 8
        TelaMontagemPedido telaMontagem = new TelaMontagemPedido(controlador, console);
        if (fa2) {
            String[] excesso = repetir("Frango grelhado com arroz integral", 4, "Penne ao molho sugo com berinjela", 3);
            estimulo(console, "selecionarPratos(4x Frango grelhado, 3x Penne)  // 7 refeições");
            telaMontagem.selecionarPratos(excesso);
            String[] ajustado = repetir("Frango grelhado com arroz integral", 3, "Penne ao molho sugo com berinjela", 2);
            estimulo(console, "selecionarPratos(3x Frango grelhado, 2x Penne)  // seleção ajustada");
            telaMontagem.selecionarPratos(ajustado);
        } else {
            String[] pratos = repetir("Frango grelhado com arroz integral", 8, "Carne moída com purê de batata", 6,
                    "Penne ao molho sugo com berinjela", 6);
            estimulo(console, "selecionarPratos(8x Frango grelhado, 6x Carne moída, 6x Penne)");
            telaMontagem.selecionarPratos(pratos);
        }
        String[] acomp = {"Arroz integral", "Feijão carioca"};
        estimulo(console, "selecionarAcompanhamentos(" + Arrays.toString(acomp) + ")");
        telaMontagem.selecionarAcompanhamentos(acomp);
        String[] sobremesas = {"Mousse de chocolate zero açúcar", "Iogurte natural com granola"};
        estimulo(console, "selecionarSobremesas(" + Arrays.toString(sobremesas) + ")");
        telaMontagem.selecionarSobremesas(sobremesas);
        estimulo(console, "confirmarResumo()");
        telaMontagem.confirmarResumo();

        // Cenário 5 - Tela 9
        TelaEndereco telaEndereco = new TelaEndereco(controlador, console);
        if (fa2) {
            estimulo(console, "informarCEP(\"99999-999\")  // CEP inexistente");
            telaEndereco.informarCEP("99999-999");
        }
        estimulo(console, "informarCEP(\"01234-567\")");
        telaEndereco.informarCEP("01234-567");
        estimulo(console, "informarNumeroComplemento(\"123\", \"Apto 101\")");
        telaEndereco.informarNumeroComplemento("123", "Apto 101");
        estimulo(console, "continuar()");
        telaEndereco.continuar();

        // Cenário 6 - Telas 10 e 11
        TelaPagamento telaPagamento = new TelaPagamento(controlador, console);
        if (fa3) {
            estimulo(console, "informarDadosCartao(\"5555 5555 5555 0002\", \"Maria Silva Santos\", \"12/30\", \"123\")");
            telaPagamento.informarDadosCartao("5555 5555 5555 0002", "Maria Silva Santos", "12/30", "123");
            estimulo(console, "finalizarAssinatura()");
            telaPagamento.finalizarAssinatura();
            if (desiste) {
                estimulo(console, "cancelar()");
                telaPagamento.cancelar();
                return;
            }
        }
        estimulo(console, "informarDadosCartao(\"4111 1111 1111 1111\", \"Maria Silva Santos\", \"12/30\", \"123\")");
        telaPagamento.informarDadosCartao("4111 1111 1111 1111", "Maria Silva Santos", "12/30", "123");
        estimulo(console, "finalizarAssinatura()");
        telaPagamento.finalizarAssinatura();
    }

    private static void estimulo(Console console, String texto) {
        console.ator(texto);
    }

    /** repetir("A", 2, "B", 1) -> [A, A, B] (uma posição por refeição). */
    private static String[] repetir(Object... nomeEQuantidade) {
        java.util.List<String> lista = new java.util.ArrayList<>();
        for (int i = 0; i < nomeEQuantidade.length; i += 2) {
            for (int q = 0; q < (Integer) nomeEQuantidade[i + 1]; q++) {
                lista.add((String) nomeEQuantidade[i]);
            }
        }
        return lista.toArray(new String[0]);
    }
}
