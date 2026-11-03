package br.mackenzie.minharefeicao.servico;

import br.mackenzie.minharefeicao.dominio.EnderecoEntrega;
import br.mackenzie.minharefeicao.persistencia.ArquivoCSV;

import java.util.HashMap;
import java.util.Map;

/**
 * «serviço» ServicoConsultaCEP - simula um serviço de CEP (como o ViaCEP)
 * usando a base local dados/ceps.csv.
 */
public class ServicoConsultaCEP {

    private final Map<String, String[]> base = new HashMap<>();

    public ServicoConsultaCEP() {
        // cep;rua;bairro;cidade;estado
        for (String[] r : ArquivoCSV.ler("ceps.csv", true)) {
            base.put(r[0].replaceAll("\\D", ""), r);
        }
    }

    /** preencherEndereco(endereco: EnderecoEntrega): boolean */
    public boolean preencherEndereco(EnderecoEntrega endereco) {
        String[] r = base.get(endereco.getCep() == null ? "" : endereco.getCep().replaceAll("\\D", ""));
        if (r == null) {
            return false;                                   // alt [CEP não encontrado]
        }
        endereco.definirLogradouro(r[1], r[2], r[3], r[4]);
        return true;
    }
}
