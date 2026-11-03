package br.mackenzie.minharefeicao.colecao;

import br.mackenzie.minharefeicao.dominio.Acompanhamento;
import br.mackenzie.minharefeicao.dominio.Cardapio;
import br.mackenzie.minharefeicao.dominio.ItemCardapio;
import br.mackenzie.minharefeicao.dominio.PratoPrincipal;
import br.mackenzie.minharefeicao.dominio.PreferenciaAlimentar;
import br.mackenzie.minharefeicao.dominio.Sobremesa;
import br.mackenzie.minharefeicao.persistencia.ArquivoCSV;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** «coleção» ColecaoCardapios - carregada de dados/cardapio.csv. */
public class ColecaoCardapios {

    private final List<Cardapio> cardapios = new ArrayList<>();

    public ColecaoCardapios(ColecaoPreferencias colecaoPreferencias) {
        // id;tipo;nome;foto;preferencias (ids separados por |, só para PRATO)
        List<ItemCardapio> itens = new ArrayList<>();
        for (String[] r : ArquivoCSV.ler("cardapio.csv", true)) {
            int id = Integer.parseInt(r[0].trim());
            String tipo = r[1].trim().toUpperCase();
            switch (tipo) {
                case "PRATO":
                    List<PreferenciaAlimentar> prefs = new ArrayList<>();
                    for (String idPref : r[4].split(Pattern.quote(ArquivoCSV.SEPARADOR_LISTA))) {
                        if (!idPref.isBlank()) {
                            prefs.add(colecaoPreferencias.buscarPorId(Integer.parseInt(idPref.trim())));
                        }
                    }
                    itens.add(new PratoPrincipal(id, r[2], r[3], prefs));
                    break;
                case "ACOMPANHAMENTO":
                    itens.add(new Acompanhamento(id, r[2], r[3]));
                    break;
                case "SOBREMESA":
                    itens.add(new Sobremesa(id, r[2], r[3]));
                    break;
                default:
                    throw new IllegalStateException("Tipo de item desconhecido no cardápio: " + tipo);
            }
        }
        cardapios.add(new Cardapio("Cardápio da semana", itens));
    }

    /** obterCardapioVigente(): Cardapio */
    public Cardapio obterCardapioVigente() {
        return cardapios.get(cardapios.size() - 1);
    }
}
