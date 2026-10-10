package com.hash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHash<T, U> {

    private static final Logger logger =
            LogManager.getLogger(NetHash.class);

    private List<THash<T, U>>[] tabela;

    private int quantidade;

    private static final double FATOR_CARGA = 0.75;

    @SuppressWarnings("unchecked")
    public NetHash(int tamanho) {

        if (tamanho <= 0) {
            logger.error("Tentativa de criar tabela com tamanho inválido: {}", tamanho);
            throw new IllegalArgumentException(
                    "não podemos ter indice menor que 0");
        }

        tabela = criarTabela(tamanho);
        quantidade = 0;

        logger.info("Tabela hash criada. Capacidade inicial: {}", tamanho);
    }

    @SuppressWarnings("unchecked")
    private List<THash<T, U>>[] criarTabela(int tamanho) {

        List<THash<T, U>>[] novaTabela =
                (List<THash<T, U>>[]) new List[tamanho];

        for (int i = 0; i < tamanho; i++) {
            novaTabela[i] = new ArrayList<>();
        }

        logger.debug("Nova estrutura de tabela criada com {} posições", tamanho);

        return novaTabela;
    }

    public int tamanho() {
        logger.debug("Capacidade atual da tabela: {}", tabela.length);
        return tabela.length;
    }

    private int calcularIndice(T chave) {

        if (chave == null) {
            logger.error("Não é possível calcular o índice de uma chave nula");
            throw new IllegalArgumentException("A chave não pode ser nula");
        }

        int indice = Math.floorMod(chave.hashCode(), tabela.length);

        logger.debug("Índice calculado para a chave '{}': {}", chave, indice);

        return indice;
    }

    private void redimensionar() {

        int capacidadeAntiga = tabela.length;
        List<THash<T, U>>[] tabelaAntiga = tabela;

        tabela = criarTabela(capacidadeAntiga * 2);

        logger.info(
                "Redimensionando tabela hash de {} para {} posições",
                capacidadeAntiga,
                tabela.length
        );

        for (List<THash<T, U>> lista : tabelaAntiga) {

            for (THash<T, U> entrada : lista) {

                int novoIndice = calcularIndice(entrada.getChave());

                tabela[novoIndice].add(entrada);

                logger.debug(
                        "Entrada realocada para o índice {}",
                        novoIndice
                );
            }
        }

        logger.info(
                "Redimensionamento concluído. Capacidade: {}, elementos: {}",
                tabela.length,
                quantidade
        );
    }

    private boolean precisaRedimensionar() {

        boolean necessario =
                quantidade >= tabela.length * FATOR_CARGA;

        logger.debug(
                "Verificação do fator de carga: quantidade={}, capacidade={}, necessário={}",
                quantidade,
                tabela.length,
                necessario
        );

        return necessario;
    }

    public void put(T chave, U valor) {

        logger.debug("Solicitação de inserção/atualização para a chave '{}'", chave);

        int indice = calcularIndice(chave);

        List<THash<T, U>> lista = tabela[indice];

        for (THash<T, U> entrada : lista) {

            if (entrada.getChave().equals(chave)) {

                entrada.setValor(valor);

                logger.info(
                        "Valor atualizado para a chave '{}' no índice {}",
                        chave,
                        indice
                );

                return;
            }
        }

        if (!lista.isEmpty()) {
            logger.debug(
                    "Colisão detectada no índice {}. Tamanho da lista: {}",
                    indice,
                    lista.size()
            );
        }

        lista.add(new THash<>(chave, valor));
        quantidade++;

        logger.info(
                "Entrada inserida. Chave='{}', índice={}, quantidade={}",
                chave,
                indice,
                quantidade
        );

        if (precisaRedimensionar()) {
            redimensionar();
        }
    }

    public U get(T chave) {

        logger.debug("Consultando valor da chave '{}'", chave);

        int indice = calcularIndice(chave);

        List<THash<T, U>> lista = tabela[indice];

        for (THash<T, U> entrada : lista) {

            if (entrada.getChave().equals(chave)) {

                logger.info(
                        "Chave '{}' encontrada no índice {}",
                        chave,
                        indice
                );

                return entrada.getValor();
            }
        }

        logger.warn("Chave '{}' não encontrada na tabela hash", chave);

        return null;
    }

    public boolean containsKey(T chave) {

        logger.debug("Verificando existência da chave '{}'", chave);

        int indice = calcularIndice(chave);

        List<THash<T, U>> lista = tabela[indice];

        for (THash<T, U> entrada : lista) {

            if (entrada.getChave().equals(chave)) {

                logger.debug("Chave '{}' encontrada", chave);

                return true;
            }
        }

        logger.debug("Chave '{}' não encontrada", chave);

        return false;
    }

    public boolean isEmpty() {

        boolean vazio = quantidade == 0;

        logger.debug("Verificação de tabela vazia: {}", vazio);

        return vazio;
    }

    public void remove(T chave) {

        logger.debug("Solicitação de remoção da chave '{}'", chave);

        int indice = calcularIndice(chave);

        List<THash<T, U>> lista = tabela[indice];

        Iterator<THash<T, U>> iterator = lista.iterator();

        while (iterator.hasNext()) {

            THash<T, U> entrada = iterator.next();

            if (entrada.getChave().equals(chave)) {

                iterator.remove();
                quantidade--;

                logger.info(
                        "Entrada removida. Chave='{}', índice={}, quantidade={}",
                        chave,
                        indice,
                        quantidade
                );

                return;
            }
        }

        logger.warn("Tentativa de remover chave inexistente: '{}'", chave);
    }
}