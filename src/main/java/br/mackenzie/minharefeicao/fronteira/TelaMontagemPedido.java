package br.mackenzie.minharefeicao.fronteira;

import br.mackenzie.minharefeicao.controle.ControladorAssinatura;
import br.mackenzie.minharefeicao.dominio.ItemCardapio;
import br.mackenzie.minharefeicao.dominio.ItemPedido;
import br.mackenzie.minharefeicao.dominio.Pedido;
import br.mackenzie.minharefeicao.dominio.Plano;

import java.util.ArrayList;
import java.util.List;

/** «fronteira» TelaMontagemPedido - Telas 5, 6, 7 e 8 do protótipo (Cenário 4). */
public class TelaMontagemPedido {

    private final ControladorAssinatura controlador;
    private final Console console;
    private boolean pratosSelecionados;

    public TelaMontagemPedido(ControladorAssinatura controlador, Console console) {
        this.controlador = controlador;
        this.console = console;
    }

    /** Estímulo do ator: selecionarPratos(pratos: String[]) - cada posição é uma refeição */
    public void selecionarPratos(String[] pratos) {
        int[] ids = resolver(controlador.getPratosDisponiveis(), pratos);
        if (ids == null) {
            return;
        }
        if (!controlador.selecionarPratos(ids)) {                       // FA2
            console.erro("Seu plano permite até " + controlador.getLimiteRefeicoes()
                    + " refeições e você selecionou " + ids.length + ". Ajuste a seleção.");
            return;
        }
        pratosSelecionados = true;
        console.sucesso(ids.length + " refeições adicionadas ao pedido.");
        console.tela("Tela 6 - Escolha seus acompanhamentos");
        Opcoes.listar(console, controlador.getAcompanhamentosDisponiveis(), ItemCardapio::getNome);
    }

    /** Estímulo do ator: selecionarAcompanhamentos(itens: String[]) */
    public void selecionarAcompanhamentos(String[] itens) {
        int[] ids = resolver(controlador.getAcompanhamentosDisponiveis(), itens);
        if (ids == null) {
            return;
        }
        controlador.selecionarAcompanhamentos(ids);
        console.tela("Tela 7 - Escolha suas sobremesas");
        Opcoes.listar(console, controlador.getSobremesasDisponiveis(), ItemCardapio::getNome);
    }

    /** Estímulo do ator: selecionarSobremesas(itens: String[]) */
    public void selecionarSobremesas(String[] itens) {
        int[] ids = resolver(controlador.getSobremesasDisponiveis(), itens);
        if (ids == null) {
            return;
        }
        Pedido resumo = controlador.selecionarSobremesas(ids);
        exibirResumo(resumo);
    }

    /** Estímulo do ator: confirmarResumo() */
    public void confirmarResumo() {
        console.tela("Tela 9 - Endereço de entrega");
        console.info("Informe o endereço onde deseja receber suas refeições.");
    }

    /** TelaMontagemPedido ..> Pedido : exibe resumo (Tela 8) */
    private void exibirResumo(Pedido pedido) {
        Plano plano = pedido.getAssinatura().getPlano();
        console.tela("Tela 8 - Resumo do pedido");
        console.info(plano.getNome() + " (" + plano.getQuantidadeRefeicoes() + " refeições) | Entrega "
                + plano.getFrequenciaEntrega().toLowerCase() + ": " + pedido.getAssinatura().getDiaSemanaEntrega());
        String categoria = "";
        for (ItemPedido ip : pedido.getItens()) {
            if (!ip.getItem().getCategoria().equals(categoria)) {
                categoria = ip.getItem().getCategoria();
                console.info(categoria + ":");
            }
            console.info(String.format("   %2dx %s", ip.getQuantidade(), ip.getItem().getNome()));
        }
        console.info("Refeições no pedido: " + pedido.getQuantidadeRefeicoes() + " de " + plano.getQuantidadeRefeicoes());
        console.info("Total do plano: " + Console.moeda(pedido.getValorTotal()));
        console.info("Status do pedido: " + pedido.getStatus());
    }

    /** Converte as escolhas exibidas (número ou nome) nos ids dos itens do cardápio. */
    private int[] resolver(List<? extends ItemCardapio> opcoes, String[] escolhas) {
        int[] ids = new int[escolhas.length];
        for (int i = 0; i < escolhas.length; i++) {
            ItemCardapio item = Opcoes.encontrar(opcoes, escolhas[i], ItemCardapio::getNome);
            if (item == null) {
                console.erro("Opção inválida: " + escolhas[i]);
                return null;
            }
            ids[i] = item.getId();
        }
        return ids;
    }

    /** Converte "1x8, 2x6, 5" em ["1","1",...,"2",...,"5"] (uma posição por refeição). */
    static String[] expandirQuantidades(String texto) {
        List<String> refeicoes = new ArrayList<>();
        for (String parte : Console.separar(texto)) {
            String[] pq = parte.toLowerCase().split("x");
            int qtd = 1;
            if (pq.length == 2 && pq[1].trim().matches("\\d+")) {
                qtd = Integer.parseInt(pq[1].trim());
            }
            for (int i = 0; i < qtd; i++) {
                refeicoes.add(pq[0].trim());
            }
        }
        return refeicoes.toArray(new String[0]);
    }

    public void exibir() {
        while (!pratosSelecionados) {
            String[] pratos = expandirQuantidades(
                    console.ler("Pratos x quantidade de refeições (ex.: 1x8, 2x6, 5x6): "));
            if (pratos.length == 0) {
                console.erro("Selecione pelo menos um prato.");
                continue;
            }
            selecionarPratos(pratos);
        }
        selecionarAcompanhamentos(Console.separar(console.ler("Acompanhamentos (ex.: 1,3) ou Enter para nenhum: ")));
        selecionarSobremesas(Console.separar(console.ler("Sobremesas (ex.: 1,5) ou Enter para nenhuma: ")));
        console.ler("Pressione Enter para confirmar o resumo e continuar...");
        confirmarResumo();
    }
}
