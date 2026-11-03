package br.mackenzie.minharefeicao.fronteira;

import br.mackenzie.minharefeicao.controle.ControladorAssinatura;
import br.mackenzie.minharefeicao.dominio.Plano;

/** «fronteira» TelaIdentificacao - Telas 1 e 2 do protótipo (Cenário 1). */
public class TelaIdentificacao {

    private final ControladorAssinatura controlador;
    private final Console console;
    private boolean codigoValidado;

    public TelaIdentificacao(ControladorAssinatura controlador, Console console) {
        this.controlador = controlador;
        this.console = console;
    }

    /** Estímulo do ator: informarCelular(celular: String) */
    public void informarCelular(String celular) {
        controlador.identificarAssinante(celular);
        console.tela("Tela 2 - Verifique seu celular");
        console.info("Enviamos um código por SMS para " + celular + ".");
    }

    /** Estímulo do ator: informarCodigo(codigo: String) */
    public void informarCodigo(String codigo) {
        if (controlador.validarCodigo(codigo)) {
            codigoValidado = true;
            console.sucesso("Código confirmado.");
            exibirPlanos();
        } else if (controlador.isCodigoEncerrado()) {
            console.erro("Código inválido. Limite de 3 tentativas atingido: a validação foi encerrada.");
            console.erro("Solicite o envio de um novo código.");
        } else {
            console.erro("Código inválido. Confira o SMS e tente novamente.");
        }
    }

    /** TelaIdentificacao ..> Plano : exibe (Tela 3 com os planos ativos) */
    private void exibirPlanos() {
        console.tela("Tela 3 - Escolha seu plano");
        Opcoes.listar(console, controlador.getPlanosDisponiveis(), TelaIdentificacao::descrever);
    }

    static String descrever(Plano p) {
        return String.format("%-16s %2d refeições | entrega %-9s | %s", p.getNome(), p.getQuantidadeRefeicoes(),
                p.getFrequenciaEntrega().toLowerCase(), Console.moeda(p.getValor()));
    }

    /** Navegação interativa pelo teclado. */
    public void exibir() {
        console.tela("Tela 1 - Bem-vindo!");
        console.info("Assine seu plano de refeições e receba suas marmitas em casa.");
        console.info("Ao continuar, você concorda com os Termos de Uso e a Política de Privacidade.");
        String celular = console.ler("Número de celular (ex.: (11) 99999-9999): ");
        while (!celular.replaceAll("\\D", "").matches("\\d{10,11}")) {
            console.erro("Informe o celular com DDD (10 ou 11 dígitos).");
            celular = console.ler("Número de celular: ");
        }
        informarCelular(celular);
        while (!codigoValidado) {
            String codigo = console.ler("Código recebido (ou R para reenviar): ");
            if (codigo.equalsIgnoreCase("R")) {
                informarCelular(celular);
                continue;
            }
            informarCodigo(codigo);
            if (!codigoValidado && controlador.isCodigoEncerrado()) {
                console.ler("Pressione Enter para receber um novo código...");
                informarCelular(celular);
            }
        }
    }

    public boolean isCodigoValidado() { return codigoValidado; }
}
