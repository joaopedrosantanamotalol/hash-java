package com.hash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// criação da tabela em si e não sua tabela
public class NetHash<T, U> {

    private List<THash<T, U>>[] tabela;

    private int quantidade;

    private static final double FATOR_CARGA = 0.75;

    @SuppressWarnings("unchecked")
    public NetHash(int tamanho) {

        if(tamanho <= 0){
            throw new IllegalArgumentException("não podemos ter indice menor que 0");
        }

        tabela = criarTabela(tamanho);
        quantidade = 0;

    }

    @SuppressWarnings("uncheked")
    private List<THash<T, U>>[] criarTabela(int tamanho) {
        List<THash<T, U>>[] novaTabela = (List<THash<T, U>>[]) new List[tamanho];

        for (int i = 0; i < tamanho; i++) {
            novaTabela[i] = new ArrayList<>();
        }

        return novaTabela;

    }

    public int tamanho(){
        return tabela.length;
    }

    // implementação da função para calcular indice
    private int calcularIndice(T chave) {
         return Math.floorMod(chave.hashCode(), tabela.length);
    }

    private void redimensionar() {
        List<THash<T, U>>[] tabelaAntiga = tabela;

        tabela = criarTabela(tabelaAntiga.length * 2);

        for (int i = 0; i < tabela.length; i++) {
            tabela[i] = new ArrayList<>();
        }

        for (List<THash<T, U>> lista : tabelaAntiga) {

            for (THash<T, U> entrada : lista) {

                int novoIndice = calcularIndice(entrada.getChave());

                tabela[novoIndice].add(entrada);
            }
        }
    }

    private boolean precisaRedimensionar() {
        return quantidade >= tabela.length * FATOR_CARGA;
    }

    public void put(T chave, U valor) {
        int indice = calcularIndice(chave);

        List<THash<T, U>> lista = tabela[indice];

        for (THash<T, U> entrada : lista) {

            if (entrada.getChave().equals(chave)) {
                entrada.setValor(valor);
                return;
            }
        }

        lista.add(new THash<>(chave, valor));
        quantidade++;

        if (precisaRedimensionar())
            redimensionar();
    }

    public U get(T chave) {

        int indice = calcularIndice(chave);

        List<THash<T, U>> lista = tabela[indice];

        for (THash<T, U> entrada : lista) {

            if (entrada.getChave().equals(chave)) {
                return entrada.getValor();
            }
        }

        return null;
    }

    public boolean containsKey(T chave){
        int indice = calcularIndice(chave);

        List<THash<T,U>> lista = tabela[indice];

        for(THash<T,U> entrada: lista){
            if (entrada.getChave().equals(chave)) return true;
        }
        return false;

    }

    public boolean isEmpty(){
        return quantidade == 0;
    }

    public void remove(T chave) {
        int indice = calcularIndice(chave);

        List<THash<T, U>> lista = tabela[indice];

        Iterator<THash<T, U>> iterator = lista.iterator();

        while (iterator.hasNext()) {
            THash<T, U> entrada = iterator.next();

            if (entrada.getChave().equals(chave)) {
                iterator.remove();
                quantidade--;
                return;
            }
        }

    }

}
