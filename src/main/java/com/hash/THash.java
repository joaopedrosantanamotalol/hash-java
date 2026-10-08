package com.hash;

// criação dos campos de entrada da tabela hash

// criação da classe THash com tipos Genéricos sendo T & U, indicando duas entradas de qualquer tipo
public class THash<T,U>  {

    T chave; // valor 1 de tipo genérico T
    U valor; // valor 2 do tipo genérico U

    // método construtor getters and setters
    THash(T chave, U valor){
        this.chave = chave;
        this.valor = valor;
    }

    public T getChave() {
        return chave;
    }

    public void setChave(T chave) {
        this.chave = chave;
    }

    public U getValor() {
        return valor;
    }

    public void setValor(U valor) {
        this.valor = valor;
    }

    

}
