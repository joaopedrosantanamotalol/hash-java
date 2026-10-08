package com.hash;

public class THash<T,U> {
    T chave;
    U valor;

    THash(T chave, U valor){
        this.chave = chave;
        this.valor = valor;
    }
}
