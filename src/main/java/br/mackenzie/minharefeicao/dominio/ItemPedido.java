package br.mackenzie.minharefeicao.dominio;

/** «entidade» ItemPedido - parte da composição Pedido *-- ItemPedido (Cenário 4). */
public class ItemPedido {

    /** ItemPedido "0..*" --> "1" ItemCardapio : refere-se a */
    private final ItemCardapio item;
    private int quantidade;

    /** «cria» ItemPedido(item: ItemCardapio, quantidade: int) */
    public ItemPedido(ItemCardapio item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }

    void incrementar(int quantidadeExtra) {
        this.quantidade += quantidadeExtra;
    }

    public ItemCardapio getItem() { return item; }
    public int getQuantidade() { return quantidade; }
}
