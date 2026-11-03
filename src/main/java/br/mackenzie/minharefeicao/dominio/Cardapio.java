package br.mackenzie.minharefeicao.dominio;

import java.util.ArrayList;
import java.util.List;

/** «entidade» Cardapio - Cenários 3 e 4. */
public class Cardapio {

    private final String nome;
    /** Cardapio "0..*" o-- "1..*" ItemCardapio : oferece (agregação) */
    private final List<ItemCardapio> itens;

    public Cardapio(String nome, List<ItemCardapio> itens) {
        this.nome = nome;
        this.itens = new ArrayList<>(itens);
    }

    /** listarPratos(prefs: PreferenciaAlimentar[]): PratoPrincipal[] */
    public List<PratoPrincipal> listarPratos(List<PreferenciaAlimentar> prefs) {
        List<PratoPrincipal> pratos = new ArrayList<>();
        for (ItemCardapio item : itens) {
            if (item instanceof PratoPrincipal) {
                PratoPrincipal pp = (PratoPrincipal) item;
                if (pp.atende(prefs)) {          // loop [para cada prato do cardápio]
                    pratos.add(pp);
                }
            }
        }
        return pratos;
    }

    /** buscarItem(id: int): ItemCardapio */
    public ItemCardapio buscarItem(int id) {
        for (ItemCardapio item : itens) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /** listarAcompanhamentos(): Acompanhamento[] */
    public List<Acompanhamento> listarAcompanhamentos() {
        List<Acompanhamento> lista = new ArrayList<>();
        for (ItemCardapio item : itens) {
            if (item instanceof Acompanhamento) {
                lista.add((Acompanhamento) item);
            }
        }
        return lista;
    }

    /** listarSobremesas(): Sobremesa[] */
    public List<Sobremesa> listarSobremesas() {
        List<Sobremesa> lista = new ArrayList<>();
        for (ItemCardapio item : itens) {
            if (item instanceof Sobremesa) {
                lista.add((Sobremesa) item);
            }
        }
        return lista;
    }

    public String getNome() { return nome; }
}
