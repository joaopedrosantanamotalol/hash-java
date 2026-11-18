package com.hash;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

// Teste Manual (sem JUnit) do CRUD; Usa um Arquivo Temporário e Não Toca no entrada.txt Real.
public class TesteBase {

    private static int falhas = 0;

    private static void conferir(String descricao, boolean condicao) {
        System.out.println((condicao ? "[OK]    " : "[FALHA] ") + descricao);
        if (!condicao) {
            falhas++;
        }
    }

    public static void main(String[] args) throws IOException {
        Path temporario = Files.createTempFile("entrada_teste", ".txt");
        ServicoCrud servico = new ServicoCrud(new ArquivoEntrada(temporario.toString()));

        // Create + Read
        servico.create(50);
        List<RegistroImagem> lista = servico.read();
        conferir("Create gera 50 registros e Read lê 50", lista.size() == 50);
        conferir("Primeiro registro tem id 1", lista.get(0).getId() == 1);

        // Ida e Volta de uma Linha (Confere o Registro Não-Homogêneo)
        RegistroImagem original = lista.get(0);
        RegistroImagem copia = RegistroImagem.deLinha(original.paraLinha());
        conferir("paraLinha/deLinha preservam o registro", original.paraLinha().equals(copia.paraLinha()));

        // Update
        boolean alterou = servico.update(10, null, null, null, "Cidade Teste", null, 12.5, null, true);
        conferir("Update devolve true para id existente", alterou);
        RegistroImagem alterado = servico.read().stream().filter(r -> r.getId() == 10).findFirst().get();
        conferir("Update gravou cidade e área", alterado.getCidade().equals("Cidade Teste")
                && alterado.getAreaDesmatadaHa() == 12.5);
        conferir("Update devolve false para id inexistente",
                !servico.update(9999, null, null, null, null, null, null, null, null));

        // Inclusão no Início e no Fim do Deque
        int id_inicio = servico.proximoId();
        servico.incluirNoInicio(new RegistroImagem(id_inicio, "novo_inicio.tif", "Sentinel-2",
                LocalDate.of(2026, 11, 1), "Manaus", "Brasil", 77.0, 10, false));
        int id_fim = servico.proximoId();
        servico.incluirNoFim(new RegistroImagem(id_fim, "novo_fim.tif", "Landsat-9",
                LocalDate.of(2026, 11, 2), "Belém", "Brasil", 88.0, 20, false));
        lista = servico.read();
        conferir("Inclusão no início coloca o registro na primeira linha", lista.get(0).getId() == id_inicio);
        conferir("Inclusão no fim coloca o registro na última linha",
                lista.get(lista.size() - 1).getId() == id_fim);
        conferir("Arquivo passou a ter 52 registros", lista.size() == 52);

        // Delete em Lifo: Sai o Último Incluído (Fim) e Depois o Penúltimo (Início)
        RegistroImagem removido_1 = servico.delete();
        conferir("Delete 1 remove o último incluído (fim)", removido_1.getId() == id_fim);
        RegistroImagem removido_2 = servico.delete();
        conferir("Delete 2 remove o anterior (início)", removido_2.getId() == id_inicio);
        conferir("Arquivo voltou a ter 50 registros", servico.read().size() == 50);

        // Esvazia o Arquivo Para Testar o Caso Vazio
        for (int i = 0; i < 50; i++) {
            servico.delete();
        }
        conferir("Arquivo vazio depois de 50 deletes", servico.read().isEmpty());
        conferir("Delete em arquivo vazio devolve null", servico.delete() == null);

        Files.deleteIfExists(temporario);
        System.out.println(falhas == 0 ? "\nTodos os testes passaram." : "\nFalhas: " + falhas);
        System.exit(falhas == 0 ? 0 : 1);
    }
}