package br.mackenzie.minharefeicao.colecao;

import br.mackenzie.minharefeicao.dominio.PreferenciaAlimentar;
import br.mackenzie.minharefeicao.persistencia.ArquivoCSV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** «coleção» ColecaoPreferencias - carregada de dados/preferencias.csv. */
public class ColecaoPreferencias {

    private final List<PreferenciaAlimentar> preferencias = new ArrayList<>();

    public ColecaoPreferencias() {
        // id;nome;descricao
        for (String[] r : ArquivoCSV.ler("preferencias.csv", true)) {
            preferencias.add(new PreferenciaAlimentar(Integer.parseInt(r[0].trim()), r[1], r[2]));
        }
    }

    /** listarTodas(): PreferenciaAlimentar[] */
    public List<PreferenciaAlimentar> listarTodas() {
        return Collections.unmodifiableList(preferencias);
    }

    /** buscarPorId(id: int): PreferenciaAlimentar */
    public PreferenciaAlimentar buscarPorId(int id) {
        for (PreferenciaAlimentar p : preferencias) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
}
