package br.mackenzie.minharefeicao.fronteira;

import java.util.List;
import java.util.function.Function;

/** Apoio às telas: encontra uma opção exibida pelo número da lista ou pelo nome. */
final class Opcoes {

    private Opcoes() { }

    static <T> T encontrar(List<T> opcoes, String escolha, Function<T, String> nome) {
        if (escolha == null || escolha.isBlank()) {
            return null;
        }
        String e = escolha.trim();
        if (e.matches("\\d+")) {
            int n = Integer.parseInt(e);
            return (n >= 1 && n <= opcoes.size()) ? opcoes.get(n - 1) : null;
        }
        for (T o : opcoes) {
            if (nome.apply(o).equalsIgnoreCase(e)) {
                return o;
            }
        }
        for (T o : opcoes) {
            if (nome.apply(o).toLowerCase().startsWith(e.toLowerCase())) {
                return o;
            }
        }
        return null;
    }

    static <T> void listar(Console console, List<T> opcoes, Function<T, String> descricao) {
        for (int i = 0; i < opcoes.size(); i++) {
            console.info(String.format("[%d] %s", i + 1, descricao.apply(opcoes.get(i))));
        }
    }
}
