package com.hash;

import java.time.LocalDate;
import java.util.Random;

// Gera Dados de Teste (Imagens de Satélite da Amazônia).
// A Semente Fixa Deixa os Mesmos Dados Saindo Sempre, o Que Ajuda a Comparar os Algoritmos.
public class GeradorDados {

    public static final long SEMENTE_PADRAO = 2026L;

    private static final String[] satelites = {
        "Amazonia-1", "CBERS-4A", "Sentinel-2", "Landsat-9", "PlanetScope"
    };

    // Cidades e Países Combinados pela Mesma Posição no Vetor.
    private static final String[] cidades = {
        "Manaus", "Belém", "Porto Velho", "Rio Branco", "Santarém", "Macapá",
        "Boa Vista", "Altamira", "Iquitos", "Puerto Maldonado", "Letícia",
        "Cobija", "Puyo", "Georgetown", "Paramaribo", "Puerto Ayacucho"
    };
    private static final String[] paises = {
        "Brasil", "Brasil", "Brasil", "Brasil", "Brasil", "Brasil",
        "Brasil", "Brasil", "Peru", "Peru", "Colômbia",
        "Bolívia", "Equador", "Guiana", "Suriname", "Venezuela"
    };

    public static DequeRegistros gerar(int quantidade, long semente) {
        Random sorteio = new Random(semente);
        DequeRegistros deque = new DequeRegistros();

        for (int i = 1; i <= quantidade; i++) {
            int posicao = sorteio.nextInt(cidades.length);
            String satelite = satelites[sorteio.nextInt(satelites.length)];
            LocalDate data = LocalDate.of(2026, 1, 1).plusDays(sorteio.nextInt(365));
            double area = Math.round(sorteio.nextDouble() * 50000) / 100.0; // Área Entre 0 e 500 Ha
            int nuvens = sorteio.nextInt(101);
            boolean alerta = area > 100.0;
            String nome_arquivo = String.format("img_%06d_%s.tif", i, satelite.toLowerCase());

            RegistroImagem registro = new RegistroImagem(i, nome_arquivo, satelite, data,
                    cidades[posicao], paises[posicao], area, nuvens, alerta);
            deque.inserirNoFim(registro);
        }
        return deque;
    }

    public static DequeRegistros gerar(int quantidade) {
        return gerar(quantidade, SEMENTE_PADRAO);
    }
}