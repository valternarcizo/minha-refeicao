package br.mackenzie.minharefeicao.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** «entidade» Assinante - Cenários 1 e 3. */
public class Assinante {

    private final String celular;
    private LocalDate dataAceiteTermos;
    /** Assinante "1" --> "0..*" CodigoConfirmacao : recebe */
    private final List<CodigoConfirmacao> codigos = new ArrayList<>();
    /** Assinante "0..*" --> "1..*" PreferenciaAlimentar : informa */
    private final List<PreferenciaAlimentar> preferencias = new ArrayList<>();

    /** «cria» Assinante(celular: String) */
    public Assinante(String celular) {
        this.celular = celular;
    }

    /** registrarAceiteTermos(data: Date): void */
    public void registrarAceiteTermos(LocalDate data) {
        this.dataAceiteTermos = data;
    }

    /** gerarCodigoConfirmacao(): CodigoConfirmacao - padrão Criador */
    public CodigoConfirmacao gerarCodigoConfirmacao() {
        CodigoConfirmacao cod = new CodigoConfirmacao();
        codigos.add(cod);
        return cod;
    }

    /** validarCodigo(codigo: String): boolean - delega ao código atual */
    public boolean validarCodigo(String codigo) {
        CodigoConfirmacao atual = getCodigoAtual();
        return atual != null && atual.verificar(codigo);
    }

    /** tentativasEsgotadas(): boolean */
    public boolean tentativasEsgotadas() {
        CodigoConfirmacao atual = getCodigoAtual();
        return atual != null && atual.tentativasEsgotadas();
    }

    /** encerrarCodigo(): void */
    public void encerrarCodigo() {
        CodigoConfirmacao atual = getCodigoAtual();
        if (atual != null) {
            atual.invalidar();
        }
    }

    /** adicionarPreferencia(pref: PreferenciaAlimentar): void */
    public void adicionarPreferencia(PreferenciaAlimentar pref) {
        if (pref != null && !preferencias.contains(pref)) {
            preferencias.add(pref);
        }
    }

    private CodigoConfirmacao getCodigoAtual() {
        return codigos.isEmpty() ? null : codigos.get(codigos.size() - 1);
    }

    public String getCelular() { return celular; }
    public LocalDate getDataAceiteTermos() { return dataAceiteTermos; }
    public List<PreferenciaAlimentar> getPreferencias() { return Collections.unmodifiableList(preferencias); }
}
