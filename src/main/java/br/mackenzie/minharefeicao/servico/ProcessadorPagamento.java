package br.mackenzie.minharefeicao.servico;

import br.mackenzie.minharefeicao.dominio.Pagamento;

/** «processador» ProcessadorPagamento - Cenário 6. */
public class ProcessadorPagamento {

    /** ProcessadorPagamento "1" --> "1" OperadoraCartao : autoriza com */
    private final OperadoraCartao operadora;

    public ProcessadorPagamento(OperadoraCartao operadora) {
        this.operadora = operadora;
    }

    /** processar(pag: Pagamento): boolean */
    public boolean processar(Pagamento pag) {
        String codigoAutorizacao = operadora.autorizar(pag.getCartao(), pag.getValor());
        if (codigoAutorizacao != null) {          // alt [operadora autorizou]
            pag.aprovar(codigoAutorizacao);
            return true;
        }
        pag.recusar();                            // [operadora não autorizou]
        return false;
    }
}
