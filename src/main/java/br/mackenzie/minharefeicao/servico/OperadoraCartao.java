package br.mackenzie.minharefeicao.servico;

import br.mackenzie.minharefeicao.dominio.CartaoCredito;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * «sistema externo» OperadoraCartao - ator secundário do caso de uso, aqui simulado.
 * Regras da simulação (cartões de teste no README):
 *  - número inválido (dígito verificador de Luhn) -> não autoriza;
 *  - cartão vencido ou validade inválida -> não autoriza;
 *  - número terminado em 0002 -> "limite insuficiente", não autoriza;
 *  - caso contrário devolve um código de autorização.
 */
public class OperadoraCartao {

    private static final SecureRandom GERADOR = new SecureRandom();

    /** autorizar(cartao: CartaoCredito, valor: Decimal): String (código de autorização ou null) */
    public String autorizar(CartaoCredito cartao, BigDecimal valor) {
        String numero = cartao.getNumero();
        if (numero.length() < 13 || !luhnValido(numero)) {
            return null;
        }
        if (!validadeOk(cartao.getValidade())) {
            return null;
        }
        if (cartao.getCvv() == null || !cartao.getCvv().matches("\\d{3,4}")) {
            return null;
        }
        if (numero.endsWith("0002") || valor == null || valor.signum() <= 0) {
            return null;
        }
        return "AUT" + String.format("%06d", GERADOR.nextInt(1_000_000));
    }

    private static boolean validadeOk(String validade) {
        try {
            YearMonth ym = YearMonth.parse(validade.trim(), DateTimeFormatter.ofPattern("MM/yy"));
            return !ym.isBefore(YearMonth.now());
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }

    private static boolean luhnValido(String numero) {
        int soma = 0;
        boolean dobrar = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int d = numero.charAt(i) - '0';
            if (dobrar) {
                d *= 2;
                if (d > 9) {
                    d -= 9;
                }
            }
            soma += d;
            dobrar = !dobrar;
        }
        return soma % 10 == 0;
    }
}
