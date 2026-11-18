package com.hash;

import java.util.ArrayList;
import java.util.List;

// Deque (Fila de Duas Pontas) Feito Com Lista Duplamente Ligada.
// Entra no Início ou no Fim (Fifo); Sai Sempre o Mais Recente, Pela ordem_insercao (Lifo).
// Essa Ordem Fica Gravada no Arquivo, Então a Regra Lifo Sobrevive Mesmo Fechando o Programa.
public class DequeRegistros {

    private NoDuplo inicio;
    private NoDuplo fim;
    private int tamanho;
    private long ultima_ordem;

    public DequeRegistros() {
        inicio = null;
        fim = null;
        tamanho = 0;
        ultima_ordem = 0;
    }

    public boolean estaVazio() {
        return tamanho == 0;
    }

    public int tamanho() {
        return tamanho;
    }

    // Inclusão Nova no Início: Recebe a Próxima ordem_insercao.
    public void inserirNoInicio(RegistroImagem registro) {
        registro.setOrdemInsercao(++ultima_ordem);
        ligarNoInicio(new NoDuplo(registro));
    }

    // Inclusão Nova no Fim: Recebe a Próxima ordem_insercao.
    public void inserirNoFim(RegistroImagem registro) {
        registro.setOrdemInsercao(++ultima_ordem);
        ligarNoFim(new NoDuplo(registro));
    }

    // Usado ao Carregar o Arquivo: Preserva a ordem_insercao Que já Estava Gravada.
    public void anexarMantendoOrdem(RegistroImagem registro) {
        if (registro.getOrdemInsercao() > ultima_ordem) {
            ultima_ordem = registro.getOrdemInsercao();
        }
        ligarNoFim(new NoDuplo(registro));
    }

    private void ligarNoInicio(NoDuplo no) {
        if (inicio == null) {
            inicio = no;
            fim = no;
        } else {
            no.setProximo(inicio);
            inicio.setAnterior(no);
            inicio = no;
        }
        tamanho++;
    }

    private void ligarNoFim(NoDuplo no) {
        if (fim == null) {
            inicio = no;
            fim = no;
        } else {
            no.setAnterior(fim);
            fim.setProximo(no);
            fim = no;
        }
        tamanho++;
    }

    // Remoção Lifo: Percorre o Deque Todo Procurando a Maior ordem_insercao e Tira Esse Nó.
    public RegistroImagem removerLifo() {
        if (inicio == null) {
            return null;
        }
        NoDuplo alvo = inicio;
        NoDuplo atual = inicio.getProximo();
        while (atual != null) {
            if (atual.getDado().getOrdemInsercao() > alvo.getDado().getOrdemInsercao()) {
                alvo = atual;
            }
            atual = atual.getProximo();
        }
        desligar(alvo);
        return alvo.getDado();
    }

    // Desamarra um Nó da Lista, Ajustando Quem Vem Antes e Depois Dele.
    private void desligar(NoDuplo no) {
        NoDuplo anterior = no.getAnterior();
        NoDuplo proximo = no.getProximo();

        if (anterior == null) {
            inicio = proximo;
        } else {
            anterior.setProximo(proximo);
        }

        if (proximo == null) {
            fim = anterior;
        } else {
            proximo.setAnterior(anterior);
        }

        no.setAnterior(null);
        no.setProximo(null);
        tamanho--;
    }

    // Busca Linear por Id; Devolve o Nó (não só o Registro) Para Permitir Alterar Depois.
    public NoDuplo buscarNoPorId(int id) {
        NoDuplo atual = inicio;
        while (atual != null) {
            if (atual.getDado().getId() == id) {
                return atual;
            }
            atual = atual.getProximo();
        }
        return null;
    }

    public RegistroImagem buscarPorId(int id) {
        NoDuplo no = buscarNoPorId(id);
        return (no == null) ? null : no.getDado();
    }

    public int maiorId() {
        int maior = 0;
        NoDuplo atual = inicio;
        while (atual != null) {
            if (atual.getDado().getId() > maior) {
                maior = atual.getDado().getId();
            }
            atual = atual.getProximo();
        }
        return maior;
    }

    // Devolve os Registros na Ordem do Deque, do Início Para o Fim.
    public List<RegistroImagem> paraLista() {
        List<RegistroImagem> lista = new ArrayList<>();
        NoDuplo atual = inicio;
        while (atual != null) {
            lista.add(atual.getDado());
            atual = atual.getProximo();
        }
        return lista;
    }
}