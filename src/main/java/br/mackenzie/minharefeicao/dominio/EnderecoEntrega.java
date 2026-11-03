package br.mackenzie.minharefeicao.dominio;

/** «entidade» EnderecoEntrega - Cenário 5. */
public class EnderecoEntrega {

    private final String cep;
    private String rua;
    private String numero;
    private String complemento;   // [0..1]
    private String bairro;
    private String cidade;
    private String estado;

    /** «cria» EnderecoEntrega(cep: String) */
    public EnderecoEntrega(String cep) {
        this.cep = cep;
    }

    /** definirLogradouro(rua, bairro, cidade, estado): void - chamado pelo ServicoConsultaCEP */
    public void definirLogradouro(String rua, String bairro, String cidade, String estado) {
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    /** completar(numero: String, complemento: String): void */
    public void completar(String numero, String complemento) {
        this.numero = numero == null ? null : numero.trim();
        this.complemento = (complemento == null || complemento.isBlank()) ? null : complemento.trim();
    }

    /** validar(): boolean */
    public boolean validar() {
        return preenchido(cep) && preenchido(rua) && preenchido(numero)
                && preenchido(bairro) && preenchido(cidade)
                && estado != null && estado.trim().length() == 2;
    }

    private static boolean preenchido(String s) {
        return s != null && !s.isBlank();
    }

    public String getCep() { return cep; }
    public String getRua() { return rua; }
    public String getNumero() { return numero; }
    public String getComplemento() { return complemento; }
    public String getBairro() { return bairro; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        String compl = complemento == null ? "" : " - " + complemento;
        return rua + ", " + numero + compl + " - " + bairro + ", " + cidade + " - " + estado + " (CEP " + cep + ")";
    }
}
