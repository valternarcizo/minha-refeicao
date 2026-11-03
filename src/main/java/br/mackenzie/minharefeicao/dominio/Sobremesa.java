package br.mackenzie.minharefeicao.dominio;

/** «entidade» Sobremesa - especialização de ItemCardapio (Cenário 4). */
public class Sobremesa extends ItemCardapio {

    public Sobremesa(int id, String nome, String foto) {
        super(id, nome, foto);
    }

    @Override
    public String getCategoria() { return "Sobremesa"; }
}
