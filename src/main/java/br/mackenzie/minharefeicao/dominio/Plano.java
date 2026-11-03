package br.mackenzie.minharefeicao.dominio;

import java.math.BigDecimal;

/** «entidade» Plano - Cenários 1, 2, 3 e 4. */
public class Plano {

    private final int id;
    private final String nome;
    private final int quantidadeRefeicoes;
    private final String frequenciaEntrega;
    private final BigDecimal valor;
    private final boolean ativo;

    public Plano(int id, String nome, int quantidadeRefeicoes, String frequenciaEntrega,
                 BigDecimal valor, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.quantidadeRefeicoes = quantidadeRefeicoes;
        this.frequenciaEntrega = frequenciaEntrega;
        this.valor = valor;
        this.ativo = ativo;
    }

    /** getFrequenciaEntrega(): String */
    public String getFrequenciaEntrega() { return frequenciaEntrega; }

    /** getQuantidadeRefeicoes(): Integer */
    public int getQuantidadeRefeicoes() { return quantidadeRefeicoes; }

    /** getValor(): Decimal */
    public BigDecimal getValor() { return valor; }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public boolean isAtivo() { return ativo; }

    @Override
    public String toString() { return nome; }
}
