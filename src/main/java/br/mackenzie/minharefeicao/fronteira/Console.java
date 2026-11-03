package br.mackenzie.minharefeicao.fronteira;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Apoio às classes de fronteira: leitura do teclado e formatação da saída no console. */
public class Console {

    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private final Scanner scanner;

    public Console(Scanner scanner) {
        this.scanner = scanner;
    }

    public String ler(String rotulo) {
        System.out.print("  > " + rotulo);
        if (scanner == null || !scanner.hasNextLine()) {
            throw new IllegalStateException("Entrada do teclado encerrada.");
        }
        return scanner.nextLine().trim();
    }

    public void tela(String titulo) {
        String linha = "=".repeat(Math.max(60, titulo.length() + 4));
        System.out.println();
        System.out.println(linha);
        System.out.println("  MINHA REFEIÇÃO | " + titulo);
        System.out.println(linha);
    }

    public void info(String mensagem) { System.out.println("  " + mensagem); }

    public void sucesso(String mensagem) { System.out.println("  [OK] " + mensagem); }

    public void erro(String mensagem) { System.out.println("  [!] " + mensagem); }

    /** Usado pela demonstração para mostrar o estímulo do ator. */
    public void ator(String estimulo) { System.out.println("  (Assinante) " + estimulo); }

    public static String moeda(BigDecimal valor) {
        return MOEDA.format(valor).replace(' ', ' ');
    }

    /** Separa "1, 2,3" em ["1","2","3"]. */
    public static String[] separar(String texto) {
        List<String> partes = new ArrayList<>();
        if (texto != null) {
            for (String p : texto.split(",")) {
                if (!p.isBlank()) {
                    partes.add(p.trim());
                }
            }
        }
        return partes.toArray(new String[0]);
    }
}
