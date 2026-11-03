package br.mackenzie.minharefeicao.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** «entidade» Pedido - Cenários 3, 4, 5 e 6. */
public class Pedido {

    private BigDecimal valorTotal = BigDecimal.ZERO;
    private final LocalDateTime dataPedido;
    private StatusPedido status;
    /** Assinatura "1" -- "1..*" Pedido : gera */
    private final Assinatura assinatura;
    /** Pedido "1" *-- "1..*" ItemPedido : contém (composição: os itens nascem e morrem com o pedido) */
    private final List<ItemPedido> itens = new ArrayList<>();
    /** Pedido "0..*" --> "1" EnderecoEntrega : é entregue em (associação simples) */
    private EnderecoEntrega endereco;

    /** «cria» Pedido(assinatura: Assinatura) - status = EM_MONTAGEM */
    public Pedido(Assinatura assinatura) {
        this.assinatura = assinatura;
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.EM_MONTAGEM;
    }

    /** cabeNoPlano(quantidade: int): boolean - FA2 (Especialista na Informação) */
    public boolean cabeNoPlano(int quantidade) {
        Plano plano = assinatura.getPlano();
        return quantidade > 0 && quantidade <= plano.getQuantidadeRefeicoes();
    }

    /**
     * adicionarItem(item: ItemCardapio, quantidade: int): void
     * «cria» ItemPedido - se o item já estiver no pedido, só soma a quantidade.
     */
    public void adicionarItem(ItemCardapio item, int quantidade) {
        for (ItemPedido ip : itens) {
            if (ip.getItem().getId() == item.getId()) {
                ip.incrementar(quantidade);
                return;
            }
        }
        itens.add(new ItemPedido(item, quantidade));
    }

    /** calcularValorTotal(): Decimal - o valor do pedido é o valor do plano */
    public BigDecimal calcularValorTotal() {
        Plano plano = assinatura.getPlano();
        this.valorTotal = plano.getValor();
        return valorTotal;
    }

    /** definirEndereco(endereco: EnderecoEntrega): void */
    public void definirEndereco(EnderecoEntrega endereco) {
        this.endereco = endereco;
    }

    /** getValorTotal(): Decimal */
    public BigDecimal getValorTotal() { return valorTotal; }

    /** aprovar(): void - status = APROVADO */
    public void aprovar() {
        this.status = StatusPedido.APROVADO;
    }

    /** Total de refeições (pratos principais) do pedido. */
    public int getQuantidadeRefeicoes() {
        int total = 0;
        for (ItemPedido ip : itens) {
            if (ip.getItem() instanceof PratoPrincipal) {
                total += ip.getQuantidade();
            }
        }
        return total;
    }

    public List<ItemPedido> getItens() { return Collections.unmodifiableList(itens); }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public StatusPedido getStatus() { return status; }
    public Assinatura getAssinatura() { return assinatura; }
    public EnderecoEntrega getEndereco() { return endereco; }
}
