package br.mackenzie.minharefeicao.colecao;

import br.mackenzie.minharefeicao.dominio.Assinatura;
import br.mackenzie.minharefeicao.dominio.ItemPedido;
import br.mackenzie.minharefeicao.dominio.Pagamento;
import br.mackenzie.minharefeicao.dominio.Pedido;
import br.mackenzie.minharefeicao.dominio.PreferenciaAlimentar;
import br.mackenzie.minharefeicao.persistencia.ArquivoCSV;

import java.util.ArrayList;
import java.util.List;

/**
 * «coleção» ColecaoAssinaturas - grava as assinaturas confirmadas em dados/assinaturas.csv
 * e os pagamentos (aprovados e recusados) em dados/pagamentos.csv.
 */
public class ColecaoAssinaturas {

    private static final String ARQ_ASSINATURAS = "assinaturas.csv";
    private static final String CAB_ASSINATURAS = "protocolo;dataEmissao;celular;dataAceiteTermos;plano;status;"
            + "diaEntrega;primeiraEntrega;renovacaoAutomatica;statusPedido;valorTotal;endereco;itens;preferencias";
    private static final String ARQ_PAGAMENTOS = "pagamentos.csv";
    private static final String CAB_PAGAMENTOS = "protocolo;dataPagamento;valor;status;codigoAutorizacao;cartao;bandeira;titular";

    private final List<Assinatura> assinaturas = new ArrayList<>();

    /** adicionar(s: Assinatura): void */
    public void adicionar(Assinatura s) {
        assinaturas.add(s);
        Pedido pedido = s.getPedidoAtual();
        String protocolo = s.getProtocolo().getNumero();

        List<String> itens = new ArrayList<>();
        for (ItemPedido ip : pedido.getItens()) {
            itens.add(ip.getQuantidade() + "x " + ip.getItem().getNome());
        }
        List<String> prefs = new ArrayList<>();
        for (PreferenciaAlimentar p : s.getAssinante().getPreferencias()) {
            prefs.add(p.getNome());
        }
        ArquivoCSV.acrescentar(ARQ_ASSINATURAS, CAB_ASSINATURAS,
                protocolo,
                s.getProtocolo().getDataEmissao().withNano(0).toString(),
                s.getAssinante().getCelular(),
                String.valueOf(s.getAssinante().getDataAceiteTermos()),
                s.getPlano().getNome(),
                s.getStatus().name(),
                s.getDiaSemanaEntrega(),
                String.valueOf(s.getDataPrimeiraEntrega()),
                String.valueOf(s.isRenovacaoAutomatica()),
                pedido.getStatus().name(),
                pedido.getValorTotal().toPlainString(),
                pedido.getEndereco().toString(),
                String.join(ArquivoCSV.SEPARADOR_LISTA, itens),
                String.join(ArquivoCSV.SEPARADOR_LISTA, prefs));

        for (Pagamento pag : s.getPagamentos()) {
            ArquivoCSV.acrescentar(ARQ_PAGAMENTOS, CAB_PAGAMENTOS,
                    protocolo,
                    pag.getDataPagamento().withNano(0).toString(),
                    pag.getValor().toPlainString(),
                    pag.getStatus().name(),
                    pag.getCodigoAutorizacao(),
                    pag.getCartao().getNumeroMascarado(),   // número completo e CVV não são gravados
                    pag.getCartao().getBandeira(),
                    pag.getCartao().getNomeTitular());
        }
    }

    /** Consulta auxiliar (fora do caso de uso): lê as assinaturas já gravadas no arquivo. */
    public List<String[]> listarRegistros() {
        return ArquivoCSV.ler(ARQ_ASSINATURAS, false);
    }
}
