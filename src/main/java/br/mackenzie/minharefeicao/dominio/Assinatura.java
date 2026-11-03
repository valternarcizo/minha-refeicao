package br.mackenzie.minharefeicao.dominio;

import br.mackenzie.minharefeicao.servico.GeradorProtocolo;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * «entidade» Assinatura - classe associativa entre Assinante e Plano.
 * Cenários 2, 3, 5 e 6. Padrão Criador: cria Pedido, Pagamento e Protocolo.
 */
public class Assinatura {

    private static final int DIAS_MINIMOS_PREPARO = 3;

    private String diaSemanaEntrega;
    private LocalDate dataPrimeiraEntrega;
    private boolean renovacaoAutomatica;
    private StatusAssinatura status;

    /** Assinatura "0..*" --> "1" Assinante */
    private final Assinante assinante;
    /** Assinatura "0..*" --> "1" Plano */
    private final Plano plano;
    /** Assinatura "1" -- "1..*" Pedido : gera */
    private final List<Pedido> pedidos = new ArrayList<>();
    /** Assinatura "1" --> "0..*" Pagamento : é paga por */
    private final List<Pagamento> pagamentos = new ArrayList<>();
    /** Assinatura "1" --> "0..1" Protocolo : gera */
    private Protocolo protocolo;

    /** «cria» Assinatura(assinante: Assinante, plano: Plano) - status = PENDENTE */
    public Assinatura(Assinante assinante, Plano plano) {
        this.assinante = assinante;
        this.plano = plano;
        this.status = StatusAssinatura.PENDENTE;
        this.renovacaoAutomatica = true;
        String frequencia = plano.getFrequenciaEntrega();
        definirDiaSemanaEntrega(frequencia);
    }

    /** -definirDiaSemanaEntrega(frequencia: String): void */
    private void definirDiaSemanaEntrega(String frequencia) {
        switch (frequencia.toUpperCase()) {
            case "QUINZENAL":
                this.diaSemanaEntrega = "Segunda-feira, a cada 15 dias";
                break;
            case "MENSAL":
                this.diaSemanaEntrega = "Primeira segunda-feira do mês";
                break;
            default:
                this.diaSemanaEntrega = "Toda segunda-feira";
        }
    }

    /** gerarPedido(): Pedido - «cria» Pedido */
    public Pedido gerarPedido() {
        Pedido pedido = new Pedido(this);
        pedidos.add(pedido);
        return pedido;
    }

    /** getPlano(): Plano */
    public Plano getPlano() { return plano; }

    /** aguardarPagamento(): void - status = AGUARDANDO_PAGAMENTO */
    public void aguardarPagamento() {
        exigirStatus(StatusAssinatura.PENDENTE, StatusAssinatura.AGUARDANDO_PAGAMENTO);
        this.status = StatusAssinatura.AGUARDANDO_PAGAMENTO;
    }

    /** registrarPagamento(valor: Decimal, cartao: CartaoCredito): Pagamento - «cria» Pagamento */
    public Pagamento registrarPagamento(BigDecimal valor, CartaoCredito cartao) {
        exigirStatus(StatusAssinatura.AGUARDANDO_PAGAMENTO);
        Pagamento pagamento = new Pagamento(valor, cartao);
        pagamentos.add(pagamento);
        return pagamento;
    }

    /** ativar(): void - status = ATIVA */
    public void ativar() {
        exigirStatus(StatusAssinatura.AGUARDANDO_PAGAMENTO);
        this.status = StatusAssinatura.ATIVA;
        this.dataPrimeiraEntrega = calcularDataPrimeiraEntrega();
    }

    /** -calcularDataPrimeiraEntrega(): Date - próxima segunda-feira com pelo menos 3 dias de preparo */
    private LocalDate calcularDataPrimeiraEntrega() {
        LocalDate base = LocalDate.now().plusDays(DIAS_MINIMOS_PREPARO);
        return base.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
    }

    /** gerarProtocolo(): Protocolo - usa GeradorProtocolo e «cria» Protocolo */
    public Protocolo gerarProtocolo() {
        String numero = GeradorProtocolo.gerarNumero();
        this.protocolo = new Protocolo(numero);
        return protocolo;
    }

    /** cancelar(): void - status = CANCELADA (FA3: assinante desiste) */
    public void cancelar() {
        if (status == StatusAssinatura.ATIVA) {
            throw new IllegalStateException("Uma assinatura ativa não é cancelada por este caso de uso.");
        }
        this.status = StatusAssinatura.CANCELADA;
    }

    private void exigirStatus(StatusAssinatura... permitidos) {
        for (StatusAssinatura s : permitidos) {
            if (status == s) {
                return;
            }
        }
        throw new IllegalStateException("Operação não permitida com a assinatura em " + status);
    }

    public Pedido getPedidoAtual() { return pedidos.isEmpty() ? null : pedidos.get(pedidos.size() - 1); }
    public Assinante getAssinante() { return assinante; }
    public String getDiaSemanaEntrega() { return diaSemanaEntrega; }
    public LocalDate getDataPrimeiraEntrega() { return dataPrimeiraEntrega; }
    public boolean isRenovacaoAutomatica() { return renovacaoAutomatica; }
    public StatusAssinatura getStatus() { return status; }
    public List<Pagamento> getPagamentos() { return Collections.unmodifiableList(pagamentos); }
    public Protocolo getProtocolo() { return protocolo; }
}
