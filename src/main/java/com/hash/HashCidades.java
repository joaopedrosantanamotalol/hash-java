package com.hash;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HashCidades {

    private static final int BASE = 27;               // 0 = vazio, 1..26 = A..Z
    private static final int TAMANHO = BASE * BASE;   // 729 posições fixas

    private final List<String>[] tabela;
    private int quantidade;

    @SuppressWarnings("unchecked")
    public HashCidades() {
        tabela = (List<String>[]) new List[TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            tabela[i] = new ArrayList<>();
        }
    }

    // remove acentos e coloca em maiúsculas para comparar
    private static String normalizar(String texto) {
        String semAcento = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toUpperCase();
    }

    private static int digito(String texto, int posicao) {
        if (posicao >= texto.length()) return 0;
        char c = texto.charAt(posicao);
        return (c >= 'A' && c <= 'Z') ? (c - 'A' + 1) : 0; // operador ternário: se c é letra de A a Z, retorna sua posição (A=1 ... Z=26); senão, retorna 0
    }

    // função hash que PRESERVA a ordem alfabética
    public int hash(String cidade) {
        String n = normalizar(cidade);
        return digito(n, 0) * BASE + digito(n, 1);
    }

    public void inserir(String cidade) {
        List<String> lista = tabela[hash(cidade)];
        String chave = normalizar(cidade);

        // busca a posição de inserção mantendo a lista ordenada
        int i = 0;
        while (i < lista.size()) {
            int cmp = normalizar(lista.get(i)).compareTo(chave);
            if (cmp == 0) return;      // já existe
            if (cmp > 0) break;
            i++;
        }
        lista.add(i, cidade);
        quantidade++;
    }

    public boolean buscar(String cidade) {
        List<String> lista = tabela[hash(cidade)];
        String chave = normalizar(cidade);
        for (String c : lista) {
            if (normalizar(c).equals(chave)) return true;
        }
        return false;
    }

    // percorre a tabela em ordem de índice => saída já ordenada
    public List<String> listarEmOrdem() {
        List<String> resultado = new ArrayList<>();
        for (List<String> lista : tabela) {
            resultado.addAll(lista);
        }
        return Collections.unmodifiableList(resultado);
    }

    public int tamanho() {
        return quantidade;
    }
}