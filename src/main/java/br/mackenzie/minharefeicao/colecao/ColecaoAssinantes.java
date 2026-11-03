package br.mackenzie.minharefeicao.colecao;

import br.mackenzie.minharefeicao.dominio.Assinante;
import br.mackenzie.minharefeicao.persistencia.ArquivoCSV;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** «coleção» ColecaoAssinantes - persistida em dados/assinantes.csv. */
public class ColecaoAssinantes {

    private static final String ARQUIVO = "assinantes.csv";
    private static final String CABECALHO = "celular;dataCadastro";

    private final List<Assinante> assinantes = new ArrayList<>();

    public ColecaoAssinantes() {
        for (String[] r : ArquivoCSV.ler(ARQUIVO, false)) {
            assinantes.add(new Assinante(r[0]));
        }
    }

    /** buscarPorCelular(celular: String): Assinante - compara só os dígitos */
    public Assinante buscarPorCelular(String celular) {
        String procurado = somenteDigitos(celular);
        for (Assinante a : assinantes) {
            if (somenteDigitos(a.getCelular()).equals(procurado)) {
                return a;
            }
        }
        return null;
    }

    /** adicionar(a: Assinante): void */
    public void adicionar(Assinante a) {
        assinantes.add(a);
        ArquivoCSV.acrescentar(ARQUIVO, CABECALHO, a.getCelular(), LocalDate.now().toString());
    }

    private static String somenteDigitos(String s) {
        return s == null ? "" : s.replaceAll("\\D", "");
    }
}
