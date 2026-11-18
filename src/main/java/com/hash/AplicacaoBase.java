package com.hash;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

// Menu de Console da Base do Sistema: Reúne as Operações de CRUD e as Inclusões no Deque.
// Dá Para Acrescentar Aqui as Opções de Ordenação, Pesquisa e Tabela Hash Depois.
public class AplicacaoBase {

    private static final Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        ServicoCrud servico = new ServicoCrud(new ArquivoEntrada());
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            opcao = lerInteiro("Opção: ", -1);
            try {
                switch (opcao) {
                    case 1 -> executarCreate(servico);
                    case 2 -> executarRead(servico);
                    case 3 -> executarUpdate(servico);
                    case 4 -> executarDelete(servico);
                    case 5 -> executarInclusao(servico, true);
                    case 6 -> executarInclusao(servico, false);
                    case 0 -> System.out.println("Encerrando.");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IOException | IllegalArgumentException erro) {
                System.out.println("Erro: " + erro.getMessage());
            }
        }
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("=== Monitoramento de Imagens de Satélite ===");
        System.out.println("1 - Create  (gerar entrada.txt)");
        System.out.println("2 - Read    (mostrar registros)");
        System.out.println("3 - Update  (alterar registro por id)");
        System.out.println("4 - Delete  (remover o último incluído - LIFO)");
        System.out.println("5 - Incluir registro no INÍCIO do deque");
        System.out.println("6 - Incluir registro no FIM do deque");
        System.out.println("0 - Sair");
    }

    // Opção 1: Gera o Arquivo entrada.txt Com N Registros Aleatórios.
    private static void executarCreate(ServicoCrud servico) throws IOException {
        int quantidade = lerInteiro("Quantidade de registros a gerar: ", 100);
        servico.create(quantidade);
        System.out.println("Arquivo " + ArquivoEntrada.NOME_PADRAO + " criado com " + quantidade + " registros.");
    }

    // Opção 2: Lê e Mostra Todos os Registros Gravados.
    private static void executarRead(ServicoCrud servico) throws IOException {
        List<RegistroImagem> lista = servico.read();
        System.out.println(RegistroImagem.cabecalho());
        for (RegistroImagem registro : lista) {
            System.out.println(registro);
        }
        System.out.println("Total: " + lista.size() + " registro(s).");
    }

    // Opção 3: Altera um Registro Existente, Campo a Campo (Vazio Mantém o Valor Atual).
    private static void executarUpdate(ServicoCrud servico) throws IOException {
        int id = lerInteiro("Id do registro a alterar: ", -1);
        System.out.println("(deixe em branco para manter o valor atual)");

        String nome_arquivo = lerTextoOpcional("Novo nome do arquivo: ");
        String satelite = lerTextoOpcional("Novo satélite: ");
        LocalDate data_captura = lerDataOpcional("Nova data (aaaa-mm-dd): ");
        String cidade = lerTextoOpcional("Nova cidade: ");
        String pais = lerTextoOpcional("Novo país: ");
        Double area = lerDecimalOpcional("Nova área desmatada (ha): ");
        Integer nuvens = lerInteiroOpcional("Nova cobertura de nuvens (%): ");
        String texto_alerta = lerTextoOpcional("Alerta de crime ambiental (s/n): ");
        Boolean alerta = texto_alerta == null ? null : texto_alerta.equalsIgnoreCase("s");

        boolean alterou = servico.update(id, nome_arquivo, satelite, data_captura,
                cidade, pais, area, nuvens, alerta);
        System.out.println(alterou ? "Registro alterado." : "Registro com id " + id + " não encontrado.");
    }

    // Opção 4: Remove o Registro Mais Recente, Seguindo a Regra Lifo do Deque.
    private static void executarDelete(ServicoCrud servico) throws IOException {
        RegistroImagem removido = servico.delete();
        if (removido == null) {
            System.out.println("Não há registros para remover.");
        } else {
            System.out.println("Registro removido (LIFO):");
            System.out.println(RegistroImagem.cabecalho());
            System.out.println(removido);
        }
    }

    // Opções 5 e 6: Monta um Registro Novo e Inclui no Início ou no Fim do Deque.
    private static void executarInclusao(ServicoCrud servico, boolean no_inicio) throws IOException {
        int id = servico.proximoId();
        System.out.println("Novo registro receberá o id " + id);

        String nome_arquivo = lerTexto("Nome do arquivo: ");
        String satelite = lerTexto("Satélite: ");
        LocalDate data_captura = null;
        while (data_captura == null) {
            data_captura = lerDataOpcional("Data de captura (aaaa-mm-dd): ");
        }
        String cidade = lerTexto("Cidade: ");
        String pais = lerTexto("País: ");
        double area = lerDecimal("Área desmatada (ha): ");
        int nuvens = lerInteiro("Cobertura de nuvens (%): ", 0);
        boolean alerta = lerTexto("Alerta de crime ambiental (s/n): ").equalsIgnoreCase("s");

        RegistroImagem registro = new RegistroImagem(id, nome_arquivo, satelite, data_captura,
                cidade, pais, area, nuvens, alerta);

        if (no_inicio) {
            servico.incluirNoInicio(registro);
        } else {
            servico.incluirNoFim(registro);
        }
        System.out.println("Registro incluído no " + (no_inicio ? "início" : "fim") + " do deque.");
    }

    // ---------- Métodos Auxiliares de Leitura do Teclado ----------

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return teclado.nextLine().trim();
    }

    private static String lerTextoOpcional(String mensagem) {
        String texto = lerTexto(mensagem);
        return texto.isEmpty() ? null : texto;
    }

    private static int lerInteiro(String mensagem, int valor_padrao) {
        String texto = lerTexto(mensagem);
        if (texto.isEmpty()) {
            return valor_padrao;
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException erro) {
            System.out.println("Número inteiro inválido.");
            return valor_padrao;
        }
    }

    private static Integer lerInteiroOpcional(String mensagem) {
        String texto = lerTextoOpcional(mensagem);
        if (texto == null) {
            return null;
        }
        try {
            return Integer.valueOf(texto);
        } catch (NumberFormatException erro) {
            System.out.println("Número inteiro inválido; valor mantido.");
            return null;
        }
    }

    private static double lerDecimal(String mensagem) {
        Double valor = lerDecimalOpcional(mensagem);
        return valor == null ? 0.0 : valor;
    }

    private static Double lerDecimalOpcional(String mensagem) {
        String texto = lerTextoOpcional(mensagem);
        if (texto == null) {
            return null;
        }
        try {
            return Double.valueOf(texto.replace(',', '.'));
        } catch (NumberFormatException erro) {
            System.out.println("Número decimal inválido; valor mantido.");
            return null;
        }
    }

    private static LocalDate lerDataOpcional(String mensagem) {
        String texto = lerTextoOpcional(mensagem);
        if (texto == null) {
            return null;
        }
        try {
            return LocalDate.parse(texto);
        } catch (DateTimeParseException erro) {
            System.out.println("Data inválida (use aaaa-mm-dd).");
            return null;
        }
    }
}