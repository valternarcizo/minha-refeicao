package br.mackenzie.minharefeicao.dominio;

import java.time.LocalDateTime;

/** «entidade» Protocolo - Cenário 6. */
public class Protocolo {

    private final String numero;
    private final LocalDateTime dataEmissao;

    /** «cria» Protocolo(numero: String) */
    public Protocolo(String numero) {
        this.numero = numero;
        this.dataEmissao = LocalDateTime.now();
    }

    public String getNumero() { return numero; }
    public LocalDateTime getDataEmissao() { return dataEmissao; }

    @Override
    public String toString() { return numero; }
}
