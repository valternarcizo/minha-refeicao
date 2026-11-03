package br.mackenzie.minharefeicao.dominio;

/** «entidade» Acompanhamento - especialização de ItemCardapio (Cenário 4). */
public class Acompanhamento extends ItemCardapio {

    public Acompanhamento(int id, String nome, String foto) {
        super(id, nome, foto);
    }

    @Override
    public String getCategoria() { return "Acompanhamento"; }
}
