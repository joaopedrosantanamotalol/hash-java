package com.hash;

public class Main {
    public static void main(String[] args) {

        NetHash<String, Integer> tabela = new NetHash<>(4);
        tabela.put("chave 1", 1010);
        System.out.println("valor da chave: " + tabela.get("chave 1"));

    }
}
