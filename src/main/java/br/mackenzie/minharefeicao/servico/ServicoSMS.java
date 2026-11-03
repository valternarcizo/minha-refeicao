package br.mackenzie.minharefeicao.servico;

import java.util.HashMap;
import java.util.Map;

/**
 * «serviço» ServicoSMS - simula o envio do SMS: a mensagem é "entregue" no console
 * e fica guardada numa caixa de saída (usada pela demonstração automática).
 */
public class ServicoSMS {

    private final Map<String, String> caixaDeSaida = new HashMap<>();
    private boolean exibirNoConsole = true;

    /** enviarCodigo(celular: String, codigo: String): boolean */
    public boolean enviarCodigo(String celular, String codigo) {
        caixaDeSaida.put(celular.replaceAll("\\D", ""), codigo);
        if (exibirNoConsole) {
            System.out.println("    [SMS simulado para " + celular + "] Seu código da Minha Refeição é: " + codigo);
        }
        return true;
    }

    /** Simulação do celular do assinante: último código recebido. */
    public String ultimoCodigoEnviado(String celular) {
        return caixaDeSaida.get(celular.replaceAll("\\D", ""));
    }

    public void setExibirNoConsole(boolean exibir) {
        this.exibirNoConsole = exibir;
    }
}
