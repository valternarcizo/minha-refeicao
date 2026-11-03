package br.mackenzie.minharefeicao.dominio;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * «entidade» CodigoConfirmacao - Cenário 1 (FA1).
 * É o Especialista na Informação sobre as tentativas de validação.
 */
public class CodigoConfirmacao {

    private static final int MAX_TENTATIVAS = 3;
    private static final SecureRandom GERADOR = new SecureRandom();

    private final String codigo;
    private final LocalDateTime dataEnvio;
    private boolean confirmado;
    private int numeroTentativas;
    private boolean invalidado;

    /** «cria» CodigoConfirmacao(): gera um código aleatório de 4 dígitos. */
    public CodigoConfirmacao() {
        this.codigo = String.format("%04d", GERADOR.nextInt(10000));
        this.dataEnvio = LocalDateTime.now();
        this.confirmado = false;
        this.numeroTentativas = 0;
    }

    /** verificar(codigo: String): boolean */
    public boolean verificar(String codigoInformado) {
        if (invalidado || confirmado) {
            return confirmado;
        }
        registrarTentativa();
        if (codigo.equals(codigoInformado == null ? "" : codigoInformado.trim())) {
            confirmado = true;
        }
        return confirmado;
    }

    /** -registrarTentativa(): void (mensagem para si mesmo no diagrama de sequência) */
    private void registrarTentativa() {
        numeroTentativas++;
    }

    /** tentativasEsgotadas(): boolean */
    public boolean tentativasEsgotadas() {
        return !confirmado && numeroTentativas >= MAX_TENTATIVAS;
    }

    /** invalidar(): void */
    public void invalidar() {
        this.invalidado = true;
    }

    public String getCodigo() { return codigo; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public boolean isConfirmado() { return confirmado; }
    public int getNumeroTentativas() { return numeroTentativas; }
}
