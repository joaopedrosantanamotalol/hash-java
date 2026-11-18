package com.hash;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Operações de CRUD Sobre o entrada.txt; Sempre Carrega o Deque, Mexe Nele e Regrava.
// Cada Operação Loga o Tempo em Nanossegundos Para Alimentar o Relatório de Desempenho.
public class ServicoCrud {

    private static final Logger logger = LogManager.getLogger(ServicoCrud.class);

    private final ArquivoEntrada arquivo;

    public ServicoCrud(ArquivoEntrada arquivo) {
        this.arquivo = arquivo;
    }

    // Create: (Re)Cria o Arquivo do Zero Com a Quantidade Pedida de Registros.
    public void create(int quantidade) throws IOException {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de registros deve ser maior que zero");
        }
        long inicio = System.nanoTime();

        DequeRegistros deque = GeradorDados.gerar(quantidade);
        arquivo.gravar(deque);

        logger.info("CREATE: arquivo {} criado com {} registros", arquivo.getCaminho(), quantidade);
        logger.info("Tempo de CREATE ({} registros): {} ns", quantidade, System.nanoTime() - inicio);
    }

    // Read: Devolve os Registros na Mesma Ordem em Que Estão Gravados.
    public List<RegistroImagem> read() throws IOException {
        long inicio = System.nanoTime();

        List<RegistroImagem> lista = arquivo.carregar().paraLista();

        logger.info("READ: {} registros lidos de {}", lista.size(), arquivo.getCaminho());
        logger.info("Tempo de READ ({} registros): {} ns", lista.size(), System.nanoTime() - inicio);
        return lista;
    }

    // Update: Acha o Registro pelo Id e Troca Só os Campos Que Vieram Preenchidos (null Mantém).
    public boolean update(int id, String nome_arquivo, String satelite, LocalDate data_captura,
                          String cidade, String pais, Double area_desmatada_ha,
                          Integer cobertura_nuvens_pct, Boolean alerta_crime) throws IOException {
        long inicio = System.nanoTime();

        DequeRegistros deque = arquivo.carregar();
        NoDuplo no = deque.buscarNoPorId(id);

        if (no == null) {
            logger.warn("UPDATE: registro com id {} não encontrado", id);
            logger.info("Tempo de UPDATE (id {}): {} ns", id, System.nanoTime() - inicio);
            return false;
        }

        RegistroImagem registro = no.getDado();
        if (nome_arquivo != null) registro.setNomeArquivo(nome_arquivo);
        if (satelite != null) registro.setSatelite(satelite);
        if (data_captura != null) registro.setDataCaptura(data_captura);
        if (cidade != null) registro.setCidade(cidade);
        if (pais != null) registro.setPais(pais);
        if (area_desmatada_ha != null) registro.setAreaDesmatadaHa(area_desmatada_ha);
        if (cobertura_nuvens_pct != null) registro.setCoberturaNuvensPct(cobertura_nuvens_pct);
        if (alerta_crime != null) registro.setAlertaCrime(alerta_crime);

        arquivo.gravar(deque);

        logger.info("UPDATE: registro com id {} alterado", id);
        logger.info("Tempo de UPDATE (id {}): {} ns", id, System.nanoTime() - inicio);
        return true;
    }

    // Delete (Regra Lifo): Tira um Registro por Execução, Sempre o Mais Recente.
    public RegistroImagem delete() throws IOException {
        long inicio = System.nanoTime();

        DequeRegistros deque = arquivo.carregar();
        RegistroImagem removido = deque.removerLifo();

        if (removido == null) {
            logger.warn("DELETE: o arquivo {} não possui registros", arquivo.getCaminho());
        } else {
            arquivo.gravar(deque);
            logger.info("DELETE: registro com id {} removido (ordem_insercao={})",
                    removido.getId(), removido.getOrdemInsercao());
        }
        logger.info("Tempo de DELETE: {} ns", System.nanoTime() - inicio);
        return removido;
    }

    // Próximo Id Livre; útil Para Quem For Incluir um Registro Novo.
    public int proximoId() throws IOException {
        return arquivo.carregar().maiorId() + 1;
    }

    public void incluirNoInicio(RegistroImagem registro) throws IOException {
        incluir(registro, true);
    }

    public void incluirNoFim(RegistroImagem registro) throws IOException {
        incluir(registro, false);
    }

    // Inclusão: Bloqueia Id Repetido e Depois Grava na Posição Pedida (Início ou Fim).
    private void incluir(RegistroImagem registro, boolean no_inicio) throws IOException {
        long inicio = System.nanoTime();

        DequeRegistros deque = arquivo.carregar();
        if (deque.buscarPorId(registro.getId()) != null) {
            throw new IllegalArgumentException("Já existe um registro com id " + registro.getId());
        }

        if (no_inicio) {
            deque.inserirNoInicio(registro);
        } else {
            deque.inserirNoFim(registro);
        }
        arquivo.gravar(deque);

        String posicao = no_inicio ? "início" : "fim";
        logger.info("INCLUSAO: registro com id {} incluído no {} do deque", registro.getId(), posicao);
        logger.info("Tempo de INCLUSAO no {}: {} ns", posicao, System.nanoTime() - inicio);
    }
}