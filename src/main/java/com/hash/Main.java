package com.hash;

import java.util.ArrayList;
import java.util.List;

// Demonstração Rápida das Duas Tabelas Hash do Projeto.
public class Main {
    public static void main(String[] args) {

        // 1) NetHash Genérica: País Como Chave, Lista de Fronteiras Como Valor
        NetHash<String, List<String>> fronteiras = new NetHash<>(16);
        fronteiras.put("Brasil", new ArrayList<>(List.of("Argentina", "Uruguai", "Paraguai", "Bolívia")));
        fronteiras.put("Argentina", new ArrayList<>(List.of("Brasil", "Chile", "Uruguai")));
        System.out.println("Fronteiras do Brasil: " + fronteiras.get("Brasil"));

        // 2) HashCidades: Insere Fora de Ordem e Confere Que Sai Alfabético
        HashCidades cidades = new HashCidades();
        cidades.inserir("São Paulo");
        cidades.inserir("Santos");
        cidades.inserir("Campinas");
        cidades.inserir("Águas de Lindóia");
        cidades.inserir("Belém");

        System.out.println(cidades.listarEmOrdem());
        // [Águas de Lindóia, Belém, Campinas, Santos, São Paulo]
        System.out.println("Existe Santos? " + cidades.buscar("santos"));
    }
}