package br.mackenzie.minharefeicao.colecao;

import br.mackenzie.minharefeicao.dominio.Plano;
import br.mackenzie.minharefeicao.persistencia.ArquivoCSV;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** «coleção» ColecaoPlanos - carregada de dados/planos.csv. */
public class ColecaoPlanos {

    private final List<Plano> planos = new ArrayList<>();

    public ColecaoPlanos() {
        // id;nome;quantidadeRefeicoes;frequenciaEntrega;valor;ativo
        for (String[] r : ArquivoCSV.ler("planos.csv", true)) {
            planos.add(new Plano(Integer.parseInt(r[0].trim()), r[1], Integer.parseInt(r[2].trim()),
                    r[3], new BigDecimal(r[4].trim()), Boolean.parseBoolean(r[5].trim())));
        }
    }

    /** listarPlanosAtivos(): Plano[] */
    public List<Plano> listarPlanosAtivos() {
        List<Plano> ativos = new ArrayList<>();
        for (Plano p : planos) {
            if (p.isAtivo()) {
                ativos.add(p);
            }
        }
        return ativos;
    }

    /** buscarPorId(planoId: int): Plano */
    public Plano buscarPorId(int planoId) {
        for (Plano p : planos) {
            if (p.getId() == planoId) {
                return p;
            }
        }
        return null;
    }
}
