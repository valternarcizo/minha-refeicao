package br.mackenzie.minharefeicao.dominio;

/**
 * «abstract» ItemCardapio - superclasse da generalização
 * PratoPrincipal / Acompanhamento / Sobremesa.
 */
public abstract class ItemCardapio {

    private final int id;
    private final String nome;
    private final String foto;

    protected ItemCardapio(int id, String nome, String foto) {
        this.id = id;
        this.nome = nome;
        this.foto = foto;
    }

    /** Cada subclasse informa sua categoria (polimorfismo). */
    public abstract String getCategoria();

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getFoto() { return foto; }

    @Override
    public String toString() { return nome; }
}
