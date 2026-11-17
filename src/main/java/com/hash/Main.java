package com.hash;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1) Países e fronteiras
        NetHash<String, List<String>> fronteiras = new NetHash<>(16);
        fronteiras.put("Brasil", new ArrayList<>(List.of("Argentina", "Uruguai", "Paraguai", "Bolívia")));
        fronteiras.put("Argentina", new ArrayList<>(List.of("Brasil", "Chile", "Uruguai")));
        System.out.println("Fronteiras do Brasil: " + fronteiras.get("Brasil"));

        // 2) Cidades em ordem alfabética
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
