package br.mackenzie.minharefeicao.servico;

import java.security.SecureRandom;
import java.time.Year;

/** «serviço» GeradorProtocolo - método estático gerarNumero() (sublinhado no diagrama). */
public final class GeradorProtocolo {

    private static final SecureRandom GERADOR = new SecureRandom();

    private GeradorProtocolo() { }

    /** gerarNumero(): String - formato MR-AAAA-NNNNNN (igual à Tela 11 do protótipo) */
    public static String gerarNumero() {
        return String.format("MR-%d-%06d", Year.now().getValue(), GERADOR.nextInt(1_000_000));
    }
}
