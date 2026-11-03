package br.mackenzie.minharefeicao.dominio;

/** «entidade» CartaoCredito - Cenário 6. */
public class CartaoCredito {

    private final String numero;
    private final String nomeTitular;
    private final String validade;
    private final String cvv;
    private final String bandeira;

    /** «cria» CartaoCredito(numero, nomeTitular, validade, cvv) */
    public CartaoCredito(String numero, String nomeTitular, String validade, String cvv) {
        this.numero = numero == null ? "" : numero.replaceAll("\\D", "");
        this.nomeTitular = nomeTitular;
        this.validade = validade;
        this.cvv = cvv;
        this.bandeira = identificarBandeira();
    }

    /** -identificarBandeira(): String (mensagem para si mesmo no diagrama de sequência) */
    private String identificarBandeira() {
        String n = numero;
        if (n.matches("^(4011|4312|4389|4514|4576|5041|5066|5067|509|6277|6362|6363|650|6516|6550).*")) {
            return "Elo";
        }
        if (n.startsWith("34") || n.startsWith("37")) {
            return "American Express";
        }
        if (n.matches("^(5[1-5]|2[2-7]).*")) {
            return "Mastercard";
        }
        if (n.startsWith("4")) {
            return "Visa";
        }
        return "Desconhecida";
    }

    /** Número mascarado - o número completo e o CVV nunca são gravados em arquivo. */
    public String getNumeroMascarado() {
        String fim = numero.length() >= 4 ? numero.substring(numero.length() - 4) : numero;
        return "**** **** **** " + fim;
    }

    public String getNumero() { return numero; }
    public String getNomeTitular() { return nomeTitular; }
    public String getValidade() { return validade; }
    public String getCvv() { return cvv; }
    public String getBandeira() { return bandeira; }
}
