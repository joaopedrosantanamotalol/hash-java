package com.hash;

// Nó da Lista Duplamente Ligada: Guarda um Registro e Aponta Para o Vizinho de Cada Lado.
public class NoDuplo {

    private RegistroImagem dado;
    private NoDuplo anterior;
    private NoDuplo proximo;

    public NoDuplo(RegistroImagem dado) {
        this.dado = dado;
        this.anterior = null;
        this.proximo = null;
    }

    public RegistroImagem getDado() {
        return dado;
    }

    public void setDado(RegistroImagem dado) {
        this.dado = dado;
    }

    public NoDuplo getAnterior() {
        return anterior;
    }

    public void setAnterior(NoDuplo anterior) {
        this.anterior = anterior;
    }

    public NoDuplo getProximo() {
        return proximo;
    }

    public void setProximo(NoDuplo proximo) {
        this.proximo = proximo;
    }
}