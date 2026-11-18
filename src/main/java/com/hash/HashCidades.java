package com.hash;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Tabela Hash de Cidades, Com Capacidade Fixa, Que já Devolve a Lista em Ordem Alfabética.
// O índice é Calculado a Partir das Duas Primeiras Letras do Nome (a-z Vira Base 27).
public class HashCidades {

    private static final Logger logger =
            LogManager.getLogger(HashCidades.class);

    private static final int BASE = 27;               // 0 = Vazio, 1..26 = A..Z
    private static final int TAMANHO = BASE * BASE;   // 729 Posições Fixas

    private final List<String>[] tabela;
    private int quantidade;

    @SuppressWarnings("unchecked")
    public HashCidades() {
        tabela = (List<String>[]) new List[TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            tabela[i] = new ArrayList<>();
        }
        quantidade = 0;

        logger.info("Tabela hash de cidades criada. Capacidade fixa: {}", TAMANHO);
    }

    // Tira Acento e Deixa Maiúsculo Para Poder Comparar os Nomes Direito.
    private static String normalizar(String texto) {
        String semAcento = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return semAcento.toUpperCase();
    }

    // Posição de uma Letra no Alfabeto (A=1 ... Z=26); 0 Para Quem Não é Letra.
    private static int digito(String texto, int posicao) {
        if (posicao >= texto.length()) return 0;
        char c = texto.charAt(posicao);
        return (c >= 'A' && c <= 'Z') ? (c - 'A' + 1) : 0;
    }

    // Hash Que Preserva a Ordem Alfabética: Usa Só as Duas Primeiras Letras do Nome.
    public int hash(String cidade) {
        if (cidade == null || cidade.isBlank()) {
            logger.error("Não é possível calcular o hash de uma cidade nula ou vazia");
            throw new IllegalArgumentException("A cidade não pode ser nula ou vazia");
        }

        String n = normalizar(cidade);
        int indice = digito(n, 0) * BASE + digito(n, 1);

        logger.debug("Índice calculado para a cidade '{}': {}", cidade, indice);

        return indice;
    }

    public void inserir(String cidade) {
        long inicio = System.nanoTime();

        logger.debug("Solicitação de inserção da cidade '{}'", cidade);

        int indice = hash(cidade);
        List<String> lista = tabela[indice];
        String chave = normalizar(cidade);

        if (!lista.isEmpty()) {
            logger.debug("Colisão detectada no índice {}. Tamanho da lista: {}",
                    indice, lista.size());
        }

        // Insere na Posição Certa Para a Lista Ficar Sempre Ordenada.
        int i = 0;
        while (i < lista.size()) {
            int cmp = normalizar(lista.get(i)).compareTo(chave);
            if (cmp == 0) {
                logger.warn("Cidade '{}' já existe no índice {}. Inserção ignorada",
                        cidade, indice);
                return;
            }
            if (cmp > 0) break;
            i++;
        }

        lista.add(i, cidade);
        quantidade++;

        logger.info("Cidade inserida. Cidade='{}', índice={}, posição na lista={}, quantidade={}",
                cidade, indice, i, quantidade);
        logger.info("Tempo de inserção de '{}': {} ns", cidade, System.nanoTime() - inicio);
    }

    public boolean buscar(String cidade) {
        long inicio = System.nanoTime();

        logger.debug("Buscando a cidade '{}'", cidade);

        int indice = hash(cidade);
        String chave = normalizar(cidade);

        for (String c : tabela[indice]) {
            if (normalizar(c).equals(chave)) {
                logger.info("Cidade '{}' encontrada no índice {}", cidade, indice);
                logger.info("Tempo de busca de '{}': {} ns", cidade, System.nanoTime() - inicio);
                return true;
            }
        }

        logger.warn("Cidade '{}' não encontrada na tabela hash", cidade);
        logger.info("Tempo de busca de '{}': {} ns", cidade, System.nanoTime() - inicio);
        return false;
    }

    // Percorre a Tabela em Ordem de índice; Como Cada índice já Sai Ordenado, o Resultado Sai Pronto.
    public List<String> listarEmOrdem() {
        long inicio = System.nanoTime();

        List<String> resultado = new ArrayList<>();
        for (List<String> lista : tabela) {
            resultado.addAll(lista);
        }

        logger.info("Listagem em ordem alfabética concluída. Cidades: {}", resultado.size());
        logger.info("Tempo de listagem: {} ns", System.nanoTime() - inicio);

        return Collections.unmodifiableList(resultado);
    }

    public int tamanho() {
        logger.debug("Quantidade atual de cidades: {}", quantidade);
        return quantidade;
    }
}