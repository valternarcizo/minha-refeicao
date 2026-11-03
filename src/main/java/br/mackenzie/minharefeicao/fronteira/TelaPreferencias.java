package br.mackenzie.minharefeicao.fronteira;

import br.mackenzie.minharefeicao.controle.ControladorAssinatura;
import br.mackenzie.minharefeicao.dominio.PratoPrincipal;
import br.mackenzie.minharefeicao.dominio.PreferenciaAlimentar;

import java.util.ArrayList;
import java.util.List;

/** «fronteira» TelaPreferencias - Tela 4 do protótipo (Cenário 3). */
public class TelaPreferencias {

    private final ControladorAssinatura controlador;
    private final Console console;
    private final List<PreferenciaAlimentar> marcadas = new ArrayList<>();
    private boolean concluida;

    public TelaPreferencias(ControladorAssinatura controlador, Console console) {
        this.controlador = controlador;
        this.console = console;
    }

    /** Estímulo do ator: marcarPreferencias(preferencias: String[]) */
    public void marcarPreferencias(String[] preferencias) {
        marcadas.clear();
        for (String escolha : preferencias) {
            PreferenciaAlimentar p = Opcoes.encontrar(controlador.getPreferenciasDisponiveis(), escolha,
                    PreferenciaAlimentar::getNome);
            if (p == null) {
                console.erro("Preferência inválida: " + escolha);
                marcadas.clear();
                return;
            }
            if (!marcadas.contains(p)) {
                marcadas.add(p);
            }
        }
        console.info("Marcadas: " + marcadas);
    }

    /** Estímulo do ator: continuar() */
    public void continuar() {
        if (marcadas.isEmpty()) {
            console.erro("Selecione pelo menos uma preferência alimentar.");
            return;
        }
        int[] ids = new int[marcadas.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = marcadas.get(i).getId();
        }
        controlador.registrarPreferencias(ids);
        concluida = true;
        exibirPratos();
    }

    /** TelaPreferencias ..> PratoPrincipal : exibe (Tela 5, até "limite" refeições) */
    private void exibirPratos() {
        console.tela("Tela 5 - Escolha seus pratos");
        console.info("Seu plano permite até " + controlador.getLimiteRefeicoes() + " refeições.");
        Opcoes.listar(console, controlador.getPratosDisponiveis(), PratoPrincipal::getNome);
    }

    public void exibir() {
        while (!concluida) {
            marcarPreferencias(Console.separar(console.ler("Preferências (ex.: 1,2): ")));
            continuar();
        }
    }
}
