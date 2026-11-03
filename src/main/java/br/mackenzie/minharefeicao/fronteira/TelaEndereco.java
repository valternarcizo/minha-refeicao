package br.mackenzie.minharefeicao.fronteira;

import br.mackenzie.minharefeicao.controle.ControladorAssinatura;
import br.mackenzie.minharefeicao.dominio.EnderecoEntrega;
import br.mackenzie.minharefeicao.dominio.Plano;

import java.math.BigDecimal;

/** «fronteira» TelaEndereco - Tela 9 do protótipo (Cenário 5). */
public class TelaEndereco {

    private final ControladorAssinatura controlador;
    private final Console console;
    private boolean cepEncontrado;
    private boolean concluida;
    private String numero;
    private String complemento;

    public TelaEndereco(ControladorAssinatura controlador, Console console) {
        this.controlador = controlador;
        this.console = console;
    }

    /** Estímulo do ator: informarCEP(cep: String) */
    public void informarCEP(String cep) {
        EnderecoEntrega endereco = controlador.informarCEP(cep);
        if (endereco == null) {
            cepEncontrado = false;
            console.erro("CEP " + cep + " não encontrado. Confira o CEP digitado.");
            return;
        }
        cepEncontrado = true;
        console.info("Rua:    " + endereco.getRua());
        console.info("Bairro: " + endereco.getBairro());
        console.info("Cidade: " + endereco.getCidade() + " - " + endereco.getEstado());
    }

    /** Estímulo do ator: informarNumeroComplemento(numero: String, complemento: String) */
    public void informarNumeroComplemento(String numero, String complemento) {
        this.numero = numero;
        this.complemento = complemento;
    }

    /** Estímulo do ator: continuar() */
    public void continuar() {
        BigDecimal valorTotal = controlador.confirmarEndereco(numero, complemento);
        if (valorTotal == null) {
            console.erro("Endereço incompleto: informe o número.");
            return;
        }
        concluida = true;
        Plano plano = controlador.getAssinaturaAtual().getPlano();
        console.sucesso("Endereço de entrega registrado.");
        console.tela("Tela 10 - Pagamento");
        console.info("Total do pedido: " + Console.moeda(valorTotal));
        console.info(plano.getNome() + " (" + plano.getQuantidadeRefeicoes() + " refeições) | Entrega "
                + plano.getFrequenciaEntrega().toLowerCase() + " | Renovação automática | Cancele quando quiser");
        console.info("Status da assinatura: " + controlador.getAssinaturaAtual().getStatus());
    }

    public void exibir() {
        while (!cepEncontrado) {
            informarCEP(console.ler("CEP (ex.: 01234-567): "));
        }
        while (!concluida) {
            informarNumeroComplemento(console.ler("Número: "), console.ler("Complemento (opcional): "));
            continuar();
        }
    }
}
