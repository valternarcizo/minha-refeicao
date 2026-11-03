package br.mackenzie.minharefeicao.persistencia;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe técnica de persistência: lê e grava arquivos CSV (separador ';') na pasta "dados".
 * É usada apenas pelas coleções e pelo ServicoConsultaCEP - as entidades não conhecem arquivos.
 * A pasta pode ser trocada com -Dminharefeicao.dados=OUTRA_PASTA.
 */
public final class ArquivoCSV {

    public static final String SEPARADOR = ";";
    public static final String SEPARADOR_LISTA = "|";

    private ArquivoCSV() { }

    public static Path diretorio() {
        return Paths.get(System.getProperty("minharefeicao.dados", "dados"));
    }

    /** Lê todas as linhas de dados (ignora o cabeçalho, linhas vazias e linhas iniciadas por #). */
    public static List<String[]> ler(String nomeArquivo, boolean obrigatorio) {
        Path arquivo = diretorio().resolve(nomeArquivo);
        List<String[]> registros = new ArrayList<>();
        if (!Files.exists(arquivo)) {
            if (obrigatorio) {
                throw new IllegalStateException("Arquivo de dados não encontrado: " + arquivo.toAbsolutePath()
                        + "\nExecute o programa a partir da pasta raiz do projeto.");
            }
            return registros;
        }
        try {
            List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
            for (int i = 1; i < linhas.size(); i++) {          // linha 0 = cabeçalho
                String linha = linhas.get(i).replace("﻿", "");
                if (linha.isBlank() || linha.startsWith("#")) {
                    continue;
                }
                registros.add(linha.split(SEPARADOR, -1));
            }
            return registros;
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao ler " + arquivo, e);
        }
    }

    /** Acrescenta uma linha no final do arquivo, criando o arquivo com cabeçalho se ele não existir. */
    public static void acrescentar(String nomeArquivo, String cabecalho, String... campos) {
        Path arquivo = diretorio().resolve(nomeArquivo);
        try {
            Files.createDirectories(arquivo.getParent() == null ? Paths.get(".") : arquivo.getParent());
            if (!Files.exists(arquivo)) {
                Files.writeString(arquivo, cabecalho + System.lineSeparator(), StandardCharsets.UTF_8);
            }
            StringBuilder linha = new StringBuilder();
            for (int i = 0; i < campos.length; i++) {
                if (i > 0) {
                    linha.append(SEPARADOR);
                }
                linha.append(limpar(campos[i]));
            }
            linha.append(System.lineSeparator());
            Files.writeString(arquivo, linha.toString(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao gravar " + arquivo, e);
        }
    }

    private static String limpar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace(SEPARADOR, ",").replace("\r", " ").replace("\n", " ");
    }
}
