package br.mackenzie.minharefeicao.dominio;

/** «entidade» PreferenciaAlimentar - Cenário 3. */
public class PreferenciaAlimentar {

    private final int id;
    private final String nome;
    private final String descricao;

    public PreferenciaAlimentar(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    @Override
    public boolean equals(Object o) {
        return o instanceof PreferenciaAlimentar && ((PreferenciaAlimentar) o).id == id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }

    @Override
    public String toString() { return nome; }
}
