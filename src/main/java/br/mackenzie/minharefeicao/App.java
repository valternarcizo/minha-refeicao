package br.mackenzie.minharefeicao;

import br.mackenzie.minharefeicao.colecao.ColecaoAssinantes;
import br.mackenzie.minharefeicao.colecao.ColecaoAssinaturas;
import br.mackenzie.minharefeicao.colecao.ColecaoCardapios;
import br.mackenzie.minharefeicao.colecao.ColecaoPlanos;
import br.mackenzie.minharefeicao.colecao.ColecaoPreferencias;
import br.mackenzie.minharefeicao.controle.ControladorAssinatura;
import br.mackenzie.minharefeicao.fronteira.Console;
import br.mackenzie.minharefeicao.fronteira.TelaEndereco;
import br.mackenzie.minharefeicao.fronteira.TelaIdentificacao;
import br.mackenzie.minharefeicao.fronteira.TelaMontagemPedido;
import br.mackenzie.minharefeicao.fronteira.TelaPagamento;
import br.mackenzie.minharefeicao.fronteira.TelaPlanos;
import br.mackenzie.minharefeicao.fronteira.TelaPreferencias;
import br.mackenzie.minharefeicao.persistencia.ArquivoCSV;
import br.mackenzie.minharefeicao.servico.OperadoraCartao;
import br.mackenzie.minharefeicao.servico.ProcessadorPagamento;
import br.mackenzie.minharefeicao.servico.ServicoConsultaCEP;
import br.mackenzie.minharefeicao.servico.ServicoSMS;

import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicação Minha Refeição - caso de uso "Assinar Plano de Refeições".
 * Uso: java -cp out br.mackenzie.minharefeicao.App          (menu interativo)
 *      java -cp out br.mackenzie.minharefeicao.App --demo   (demonstração automática; --demo 3 roda só o roteiro 3)
 */
public class App {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("--demo")) {
            Demonstracao.executar(args.length > 1 ? Integer.parseInt(args[1]) : 0);
            return;
        }
        Scanner scanner = new Scanner(System.in, "UTF-8");
        Console console = new Console(scanner);
        while (true) {
            console.tela("Menu principal");
            console.info("[1] Assinar plano de refeições");
            console.info("[2] Consultar assinaturas gravadas (" + ArquivoCSV.diretorio().resolve("assinaturas.csv") + ")");
            console.info("[3] Executar demonstração automática (fluxo principal e alternativos)");
            console.info("[0] Sair");
            String opcao;
            try {
                opcao = console.ler("Opção: ");
            } catch (IllegalStateException fimDaEntrada) {
                return;
            }
            try {
                switch (opcao) {
                    case "1": assinarPlano(console); break;
                    case "2": consultarAssinaturas(console); break;
                    case "3": Demonstracao.executar(); break;
                    case "0": console.info("Até logo!"); return;
                    default: console.erro("Opção inválida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                console.erro(e.getMessage());
            }
        }
    }

    /** Monta o controlador com suas coleções e serviços (as coleções carregam os arquivos CSV). */
    public static ControladorAssinatura criarControlador(ServicoSMS servicoSMS) {
        ColecaoPreferencias preferencias = new ColecaoPreferencias();
        return new ControladorAssinatura(
                new ColecaoAssinantes(),
                new ColecaoPlanos(),
                preferencias,
                new ColecaoCardapios(preferencias),
                new ColecaoAssinaturas(),
                servicoSMS,
                new ServicoConsultaCEP(),
                new ProcessadorPagamento(new OperadoraCartao()));
    }

    /** Navega pelas telas na ordem do protótipo (Telas 1 a 11). */
    private static void assinarPlano(Console console) {
        ControladorAssinatura controlador = criarControlador(new ServicoSMS());
        new TelaIdentificacao(controlador, console).exibir();     // Cenário 1
        new TelaPlanos(controlador, console).exibir();            // Cenário 2
        new TelaPreferencias(controlador, console).exibir();      // Cenário 3
        new TelaMontagemPedido(controlador, console).exibir();    // Cenário 4
        new TelaEndereco(controlador, console).exibir();          // Cenário 5
        new TelaPagamento(controlador, console).exibir();         // Cenário 6
    }

    private static void consultarAssinaturas(Console console) {
        List<String[]> registros = new ColecaoAssinaturas().listarRegistros();
        console.tela("Assinaturas gravadas em CSV (" + registros.size() + ")");
        if (registros.isEmpty()) {
            console.info("Nenhuma assinatura gravada ainda.");
        }
        for (String[] r : registros) {
            console.info(String.format("%s | %s | %s | %s | primeira entrega %s | R$ %s", r[0], r[2], r[4], r[5], r[7], r[10]));
        }
    }
}
