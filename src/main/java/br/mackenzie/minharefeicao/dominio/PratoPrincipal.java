package br.mackenzie.minharefeicao.dominio;

import java.util.ArrayList;
import java.util.List;

/** «entidade» PratoPrincipal - especialização de ItemCardapio (Cenário 3). */
public class PratoPrincipal extends ItemCardapio {

    /** PratoPrincipal "0..*" --> "1..*" PreferenciaAlimentar : atende a */
    private final List<PreferenciaAlimentar> preferenciasAtendidas;

    public PratoPrincipal(int id, String nome, String foto, List<PreferenciaAlimentar> preferenciasAtendidas) {
        super(id, nome, foto);
        this.preferenciasAtendidas = new ArrayList<>(preferenciasAtendidas);
    }

    /** atende(prefs: PreferenciaAlimentar[]): boolean */
    public boolean atende(List<PreferenciaAlimentar> prefs) {
        for (PreferenciaAlimentar p : prefs) {
            if (preferenciasAtendidas.contains(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getCategoria() { return "Prato principal"; }
}
