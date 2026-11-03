package br.mackenzie.minharefeicao.fronteira;

import br.mackenzie.minharefeicao.controle.ControladorAssinatura;
import br.mackenzie.minharefeicao.dominio.Assinatura;
import br.mackenzie.minharefeicao.dominio.Plano;
import br.mackenzie.minharefeicao.dominio.Protocolo;

import java.time.format.DateTimeFormatter;

/** «fronteira» TelaPagamento - Telas 10 e 11 do protótipo (Cenário 6). */
public class TelaPagamento {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ControladorAssinatura controlador;
    private final Console console;
    private String numero;
    private String nome;
    private String validade;
    private String cvv;
    private boolean concluida;
    private boolean cancelada;

    public TelaPagamento(ControladorAssinatura controlador, Console console) {
        this.controlador = controlador;
        this.console = console;
    }

    /** Estímulo do ator: informarDadosCartao(numero, nome, validade, cvv) */
    public void informarDadosCartao(String numero, String nome, String validade, String cvv) {
        this.numero = numero;
        this.nome = nome;
        this.validade = validade;
        this.cvv = cvv;
    }

    /** Estímulo do ator: finalizarAssinatura() */
    public void finalizarAssinatura() {
        Protocolo prot = controlador.finalizarAssinatura(numero, nome, validade, cvv);
        if (prot == null) {                                              // FA3
            console.erro("Pagamento não aprovado pela operadora do cartão.");
            console.erro("Informe outro cartão, tente novamente ou desista da assinatura.");
            console.info("Status da assinatura: " + controlador.getAssinaturaAtual().getStatus());
            return;
        }
        concluida = true;
        exibirConfirmacao(prot);
    }

    /** Estímulo do ator: cancelar() - FA3, assinante desiste */
    public void cancelar() {
        controlador.cancelarAssinatura();
        cancelada = true;
        console.info("Processo encerrado sem ativar o plano. Status da assinatura: "
                + controlador.getAssinaturaAtual().getStatus());
    }

    /** TelaPagamento ..> Protocolo : exibe (Tela 11) */
    private void exibirConfirmacao(Protocolo prot) {
        Assinatura s = controlador.getAssinaturaAtual();
        Plano plano = s.getPlano();
        console.tela("Tela 11 - Assinatura realizada com sucesso!");
        console.info("Agora é só esperar sua primeira entrega.");
        console.info("Plano:            " + plano.getNome() + " - " + plano.getQuantidadeRefeicoes() + " refeições");
        console.info("Entrega:          " + plano.getFrequenciaEntrega().toLowerCase() + " - " + s.getDiaSemanaEntrega());
        console.info("Endereço:         " + controlador.getPedidoAtual().getEndereco());
        console.info("Primeira entrega: " + s.getDataPrimeiraEntrega().format(DATA));
        console.info("Protocolo:        " + prot.getNumero());
        console.info("Status: assinatura " + s.getStatus() + " | pedido " + controlador.getPedidoAtual().getStatus());
    }

    public void exibir() {
        while (!concluida && !cancelada) {
            informarDadosCartao(console.ler("Número do cartão: "), console.ler("Nome no cartão: "),
                    console.ler("Validade (MM/AA): "), console.ler("CVV: "));
            finalizarAssinatura();
            if (!concluida) {
                String opcao = console.ler("[1] Informar outro cartão / tentar de novo   [2] Desistir: ");
                if (opcao.equals("2")) {
                    cancelar();
                }
            }
        }
    }

    public boolean isConcluida() { return concluida; }
    public boolean isCancelada() { return cancelada; }
}
