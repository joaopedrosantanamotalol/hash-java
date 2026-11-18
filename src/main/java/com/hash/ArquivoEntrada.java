package com.hash;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Acesso ao Arquivo entrada.txt: só Esta Classe Sabe Como Ele é Gravado e Lido.
public class ArquivoEntrada {

    public static final String NOME_PADRAO = "entrada.txt";

    private final Path caminho;

    public ArquivoEntrada() {
        this(NOME_PADRAO);
    }

    public ArquivoEntrada(String caminho_arquivo) {
        this.caminho = Paths.get(caminho_arquivo);
    }

    public Path getCaminho() {
        return caminho;
    }

    public boolean existe() {
        return Files.exists(caminho);
    }

    // Grava (ou Regrava) o Arquivo Inteiro a Partir do Deque Atual.
    public void gravar(DequeRegistros deque) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (RegistroImagem registro : deque.paraLista()) {
            linhas.add(registro.paraLinha());
        }
        Files.write(caminho, linhas, StandardCharsets.UTF_8);
    }

    // Lê o Arquivo e Monta o Deque de Novo, Preservando a ordem_insercao Gravada (Importante Para a Regra Lifo).
    public DequeRegistros carregar() throws IOException {
        if (!existe()) {
            throw new IOException("Arquivo " + caminho + " não encontrado. Execute a operação Create primeiro.");
        }
        DequeRegistros deque = new DequeRegistros();
        List<String> linhas = Files.readAllLines(caminho, StandardCharsets.UTF_8);
        int numero_linha = 0;
        for (String linha : linhas) {
            numero_linha++;
            if (linha.isBlank()) {
                continue;
            }
            try {
                deque.anexarMantendoOrdem(RegistroImagem.deLinha(linha));
            } catch (IllegalArgumentException erro) {
                throw new IOException("Erro na linha " + numero_linha + " de " + caminho
                        + ": " + erro.getMessage(), erro);
            }
        }
        return deque;
    }
}