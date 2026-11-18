package com.hash;

// Par Chave/Valor Guardado Dentro de Cada Posição da Tabela Hash.
public class THash<T,U>  {

    private final T chave;
    private U valor;

    THash(T chave, U valor){
        this.chave = chave;
        this.valor = valor;
    }

    public T getChave() {
        return chave;
    }

    public U getValor() {
        return valor;
    }

    public void setValor(U valor) {
        this.valor = valor;
    }

}