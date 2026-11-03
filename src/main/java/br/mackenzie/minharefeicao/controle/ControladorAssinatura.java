package br.mackenzie.minharefeicao.controle;

import br.mackenzie.minharefeicao.colecao.ColecaoAssinantes;
import br.mackenzie.minharefeicao.colecao.ColecaoAssinaturas;
import br.mackenzie.minharefeicao.colecao.ColecaoCardapios;
import br.mackenzie.minharefeicao.colecao.ColecaoPlanos;
import br.mackenzie.minharefeicao.colecao.ColecaoPreferencias;
import br.mackenzie.minharefeicao.dominio.Acompanhamento;
import br.mackenzie.minharefeicao.dominio.Assinante;
import br.mackenzie.minharefeicao.dominio.Assinatura;
import br.mackenzie.minharefeicao.dominio.Cardapio;
import br.mackenzie.minharefeicao.dominio.CartaoCredito;
import br.mackenzie.minharefeicao.dominio.CodigoConfirmacao;
import br.mackenzie.minharefeicao.dominio.EnderecoEntrega;
import br.mackenzie.minharefeicao.dominio.ItemCardapio;
import br.mackenzie.minharefeicao.dominio.Pagamento;
import br.mackenzie.minharefeicao.dominio.Pedido;
import br.mackenzie.minharefeicao.dominio.Plano;
import br.mackenzie.minharefeicao.dominio.PratoPrincipal;
import br.mackenzie.minharefeicao.dominio.PreferenciaAlimentar;
import br.mackenzie.minharefeicao.dominio.Protocolo;
import br.mackenzie.minharefeicao.dominio.Sobremesa;
import br.mackenzie.minharefeicao.servico.ProcessadorPagamento;
import br.mackenzie.minharefeicao.servico.ServicoConsultaCEP;
import br.mackenzie.minharefeicao.servico.ServicoSMS;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * «controle» ControladorAssinatura - controlador do caso de uso "Assinar Plano de Refeições"
 * (padrão GRASP Controlador). Cada método público corresponde a uma mensagem que uma tela
 * envia ao controlador nos diagramas de sequência dos Cenários 1 a 6.
 */
public class ControladorAssinatura {

    // Associações navegáveis do diagrama de classes de projeto
    private final ColecaoAssinantes colecaoAssinantes;
    private final ColecaoPlanos colecaoPlanos;
    private final ColecaoPreferencias colecaoPreferencias;
    private final ColecaoCardapios colecaoCardapios;
    private final ColecaoAssinaturas colecaoAssinaturas;
    private final ServicoSMS servicoSMS;
    private final ServicoConsultaCEP servicoConsultaCEP;
    private final ProcessadorPagamento processadorPagamento;

    // Atributos do controlador (objetos em uso no caso de uso)
    private Assinante assinanteAtual;
    private Assinatura assinaturaAtual;
    private Pedido pedidoAtual;
    private Cardapio cardapioAtual;
    private EnderecoEntrega enderecoAtual;

    // Retornos que o diagrama de sequência devolve à tela (C-->>T) e a tela consulta para exibir
    private List<Plano> planosDisponiveis = Collections.emptyList();
    private List<PreferenciaAlimentar> preferenciasDisponiveis = Collections.emptyList();
    private List<PratoPrincipal> pratosDisponiveis = Collections.emptyList();
    private int limiteRefeicoes;
    private List<Acompanhamento> acompanhamentosDisponiveis = Collections.emptyList();
    private List<Sobremesa> sobremesasDisponiveis = Collections.emptyList();
    private boolean codigoEncerrado;

    public ControladorAssinatura(ColecaoAssinantes colecaoAssinantes, ColecaoPlanos colecaoPlanos,
                                 ColecaoPreferencias colecaoPreferencias, ColecaoCardapios colecaoCardapios,
                                 ColecaoAssinaturas colecaoAssinaturas, ServicoSMS servicoSMS,
                                 ServicoConsultaCEP servicoConsultaCEP, ProcessadorPagamento processadorPagamento) {
        this.colecaoAssinantes = colecaoAssinantes;
        this.colecaoPlanos = colecaoPlanos;
        this.colecaoPreferencias = colecaoPreferencias;
        this.colecaoCardapios = colecaoCardapios;
        this.colecaoAssinaturas = colecaoAssinaturas;
        this.servicoSMS = servicoSMS;
        this.servicoConsultaCEP = servicoConsultaCEP;
        this.processadorPagamento = processadorPagamento;
    }

    // ===================== Cenário 1 - Identificar Assinante =====================

    /** identificarAssinante(celular: String): void */
    public void identificarAssinante(String celular) {
        Assinante a = colecaoAssinantes.buscarPorCelular(celular);
        if (a == null) {                                   // alt [assinante não cadastrado]
            a = new Assinante(celular);                    // «cria»
            colecaoAssinantes.adicionar(a);
        }
        assinanteAtual = a;
        codigoEncerrado = false;
        a.registrarAceiteTermos(LocalDate.now());
        CodigoConfirmacao cod = a.gerarCodigoConfirmacao();
        servicoSMS.enviarCodigo(celular, cod.getCodigo());
    }

    /** validarCodigo(codigo: String): boolean */
    public boolean validarCodigo(String codigo) {
        exigir(assinanteAtual != null, "Informe o celular antes de validar o código.");
        if (assinanteAtual.validarCodigo(codigo)) {       // alt [código válido]
            planosDisponiveis = colecaoPlanos.listarPlanosAtivos();
            return true;
        }
        if (assinanteAtual.tentativasEsgotadas()) {        // alt [numeroTentativas = 3]  (FA1)
            assinanteAtual.encerrarCodigo();
            codigoEncerrado = true;
        }
        return false;                                      // [numeroTentativas < 3]
    }

    // ===================== Cenário 2 - Selecionar Plano =====================

    /** selecionarPlano(planoId: int): void */
    public void selecionarPlano(int planoId) {
        exigir(assinanteAtual != null, "Assinante não identificado.");
        Plano p = colecaoPlanos.buscarPorId(planoId);
        exigir(p != null && p.isAtivo(), "Plano inválido: " + planoId);
        assinaturaAtual = new Assinatura(assinanteAtual, p);     // «cria» - status = PENDENTE
        preferenciasDisponiveis = colecaoPreferencias.listarTodas();
    }

    // ===================== Cenário 3 - Informar Preferências =====================

    /** registrarPreferencias(idsPreferencias: int[]): void */
    public void registrarPreferencias(int[] idsPreferencias) {
        exigir(assinaturaAtual != null, "Selecione um plano antes.");
        exigir(idsPreferencias != null && idsPreferencias.length > 0, "Informe ao menos uma preferência.");
        for (int id : idsPreferencias) {                   // loop [para cada id em idsPreferencias]
            PreferenciaAlimentar pref = colecaoPreferencias.buscarPorId(id);
            exigir(pref != null, "Preferência inválida: " + id);
            assinanteAtual.adicionarPreferencia(pref);
        }
        pedidoAtual = assinaturaAtual.gerarPedido();       // status = EM_MONTAGEM
        Plano p = assinaturaAtual.getPlano();
        limiteRefeicoes = p.getQuantidadeRefeicoes();
        cardapioAtual = colecaoCardapios.obterCardapioVigente();
        pratosDisponiveis = cardapioAtual.listarPratos(assinanteAtual.getPreferencias());
    }

    // ===================== Cenário 4 - Montar Refeições e Revisar Pedido =====================

    /** selecionarPratos(idsPratos: int[]): boolean - cada id corresponde a uma refeição */
    public boolean selecionarPratos(int[] idsPratos) {
        exigir(pedidoAtual != null, "Informe as preferências antes.");
        if (!pedidoAtual.cabeNoPlano(idsPratos.length)) {  // alt [quantidade > quantidadeRefeicoes] (FA2)
            return false;
        }
        for (int id : idsPratos) {                         // loop [para cada id em idsPratos]
            ItemCardapio item = buscarItemDoTipo(id, PratoPrincipal.class);
            pedidoAtual.adicionarItem(item, 1);
        }
        acompanhamentosDisponiveis = cardapioAtual.listarAcompanhamentos();
        return true;
    }

    /** selecionarAcompanhamentos(ids: int[]): void */
    public void selecionarAcompanhamentos(int[] ids) {
        exigir(pedidoAtual != null, "Pedido não iniciado.");
        for (int id : ids) {
            pedidoAtual.adicionarItem(buscarItemDoTipo(id, Acompanhamento.class), 1);
        }
        sobremesasDisponiveis = cardapioAtual.listarSobremesas();
    }

    /** selecionarSobremesas(ids: int[]): Pedido */
    public Pedido selecionarSobremesas(int[] ids) {
        exigir(pedidoAtual != null, "Pedido não iniciado.");
        for (int id : ids) {
            pedidoAtual.adicionarItem(buscarItemDoTipo(id, Sobremesa.class), 1);
        }
        pedidoAtual.calcularValorTotal();
        return pedidoAtual;                                // resumo: Pedido
    }

    // ===================== Cenário 5 - Informar Endereço de Entrega =====================

    /** informarCEP(cep: String): EnderecoEntrega (null se o CEP não for encontrado) */
    public EnderecoEntrega informarCEP(String cep) {
        EnderecoEntrega endereco = new EnderecoEntrega(cep);       // «cria»
        if (servicoConsultaCEP.preencherEndereco(endereco)) {      // alt [CEP encontrado]
            enderecoAtual = endereco;
            return endereco;
        }
        enderecoAtual = null;                                      // [CEP não encontrado]
        return null;
    }

    /** confirmarEndereco(numero: String, complemento: String): Decimal (null se o endereço for inválido) */
    public BigDecimal confirmarEndereco(String numero, String complemento) {
        exigir(enderecoAtual != null, "Informe um CEP válido antes.");
        enderecoAtual.completar(numero, complemento);
        if (!enderecoAtual.validar()) {
            return null;
        }
        pedidoAtual.definirEndereco(enderecoAtual);
        assinaturaAtual.aguardarPagamento();               // status = AGUARDANDO_PAGAMENTO
        return pedidoAtual.getValorTotal();
    }

    // ===================== Cenário 6 - Pagamento e Confirmação =====================

    /** finalizarAssinatura(numero, nomeTitular, validade, cvv): Protocolo (null se não autorizado) */
    public Protocolo finalizarAssinatura(String numero, String nomeTitular, String validade, String cvv) {
        exigir(pedidoAtual != null && pedidoAtual.getEndereco() != null, "Informe o endereço antes.");
        CartaoCredito cartao = new CartaoCredito(numero, nomeTitular, validade, cvv);   // «cria»
        BigDecimal valor = pedidoAtual.getValorTotal();
        Pagamento pag = assinaturaAtual.registrarPagamento(valor, cartao);
        boolean aprovado = processadorPagamento.processar(pag);
        if (aprovado) {                                    // alt [pagamento aprovado]
            assinaturaAtual.ativar();                      // status = ATIVA
            pedidoAtual.aprovar();                         // status = APROVADO
            Protocolo prot = assinaturaAtual.gerarProtocolo();
            colecaoAssinaturas.adicionar(assinaturaAtual);
            return prot;
        }
        return null;                                       // [pagamento não autorizado] (FA3)
    }

    /** cancelarAssinatura(): void - opt [assinante desiste] (FA3) */
    public void cancelarAssinatura() {
        exigir(assinaturaAtual != null, "Não há assinatura em andamento.");
        assinaturaAtual.cancelar();                        // status = CANCELADA
    }

    // ===================== Auxiliares =====================

    private ItemCardapio buscarItemDoTipo(int id, Class<? extends ItemCardapio> tipo) {
        ItemCardapio item = cardapioAtual.buscarItem(id);
        exigir(tipo.isInstance(item), "Item " + id + " não é um(a) " + tipo.getSimpleName() + " do cardápio.");
        return item;
    }

    private static void exigir(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    // Consultas usadas pelas telas para exibir os retornos do controlador
    public List<Plano> getPlanosDisponiveis() { return planosDisponiveis; }
    public List<PreferenciaAlimentar> getPreferenciasDisponiveis() { return preferenciasDisponiveis; }
    public List<PratoPrincipal> getPratosDisponiveis() { return pratosDisponiveis; }
    public int getLimiteRefeicoes() { return limiteRefeicoes; }
    public List<Acompanhamento> getAcompanhamentosDisponiveis() { return acompanhamentosDisponiveis; }
    public List<Sobremesa> getSobremesasDisponiveis() { return sobremesasDisponiveis; }
    public boolean isCodigoEncerrado() { return codigoEncerrado; }
    public Assinatura getAssinaturaAtual() { return assinaturaAtual; }
    public Pedido getPedidoAtual() { return pedidoAtual; }
}
