package br.mackenzie.minharefeicao.fronteira;

import br.mackenzie.minharefeicao.controle.ControladorAssinatura;
import br.mackenzie.minharefeicao.dominio.Plano;
import br.mackenzie.minharefeicao.dominio.PreferenciaAlimentar;

/** «fronteira» TelaPlanos - Tela 3 do protótipo (Cenário 2). */
public class TelaPlanos {

    private final ControladorAssinatura controlador;
    private final Console console;
    private boolean planoSelecionado;

    public TelaPlanos(ControladorAssinatura controlador, Console console) {
        this.controlador = controlador;
        this.console = console;
    }

    /** Estímulo do ator: selecionarPlano(plano: String) - número da opção ou nome do plano */
    public void selecionarPlano(String plano) {
        Plano p = Opcoes.encontrar(controlador.getPlanosDisponiveis(), plano, Plano::getNome);
        if (p == null) {
            console.erro("Plano inválido. Escolha uma das opções exibidas.");
            return;
        }
        controlador.selecionarPlano(p.getId());
        planoSelecionado = true;
        console.sucesso("Plano selecionado: " + p.getNome());
        exibirPreferencias();
    }

    /** TelaPlanos ..> PreferenciaAlimentar : exibe (Tela 4) */
    private void exibirPreferencias() {
        console.tela("Tela 4 - Suas preferências");
        Opcoes.listar(console, controlador.getPreferenciasDisponiveis(),
                (PreferenciaAlimentar pa) -> String.format("%-12s %s", pa.getNome(), pa.getDescricao()));
    }

    public void exibir() {
        while (!planoSelecionado) {
            selecionarPlano(console.ler("Número do plano: "));
        }
    }
}
