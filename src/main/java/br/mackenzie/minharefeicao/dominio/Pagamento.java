package br.mackenzie.minharefeicao.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** «entidade» Pagamento - Cenário 6. O pagamento é da Assinatura. */
public class Pagamento {

    private final BigDecimal valor;
    private final LocalDateTime dataPagamento;
    private String codigoAutorizacao;   // [0..1]
    private StatusPagamento status;
    /** Pagamento "0..*" --> "1" CartaoCredito : utiliza */
    private final CartaoCredito cartao;

    /** «cria» Pagamento(valor: Decimal, cartao: CartaoCredito) */
    public Pagamento(BigDecimal valor, CartaoCredito cartao) {
        this.valor = valor;
        this.cartao = cartao;
        this.dataPagamento = LocalDateTime.now();
        this.status = StatusPagamento.PENDENTE;
    }

    /** aprovar(codigoAutorizacao: String): void */
    public void aprovar(String codigoAutorizacao) {
        this.codigoAutorizacao = codigoAutorizacao;
        this.status = StatusPagamento.APROVADO;
    }

    /** recusar(): void */
    public void recusar() {
        this.status = StatusPagamento.RECUSADO;
    }

    public BigDecimal getValor() { return valor; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public String getCodigoAutorizacao() { return codigoAutorizacao; }
    public StatusPagamento getStatus() { return status; }
    public CartaoCredito getCartao() { return cartao; }
}
