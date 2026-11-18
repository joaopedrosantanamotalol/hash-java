package com.hash;

import java.time.LocalDate;
import java.util.Locale;

// Registro Não-Homogêneo do Cenário: uma Imagem de Satélite Usada no Monitoramento Ambiental.
// Os Campos Têm Tipos Diferentes (int, String, LocalDate, double, boolean, long) de Propósito.
// Linha do Arquivo (Separador ';'): id;nome_arquivo;satelite;data_captura;cidade;pais;
// area_desmatada_ha;cobertura_nuvens_pct;alerta_crime;ordem_insercao
public class RegistroImagem implements Comparable<RegistroImagem> {

    public static final String SEPARADOR = ";";
    public static final int TOTAL_CAMPOS = 10;

    private int id;
    private String nome_arquivo;
    private String satelite;
    private LocalDate data_captura;
    private String cidade;
    private String pais;
    private double area_desmatada_ha;
    private int cobertura_nuvens_pct;
    private boolean alerta_crime;
    private long ordem_insercao; // Controla a Regra Lifo: Quanto Maior, Mais Recente

    public RegistroImagem(int id, String nome_arquivo, String satelite,
                          LocalDate data_captura, String cidade, String pais,
                          double area_desmatada_ha, int cobertura_nuvens_pct,
                          boolean alerta_crime) {
        this.id = id;
        this.nome_arquivo = limpar(nome_arquivo);
        this.satelite = limpar(satelite);
        this.data_captura = data_captura;
        this.cidade = limpar(cidade);
        this.pais = limpar(pais);
        this.area_desmatada_ha = area_desmatada_ha;
        this.cobertura_nuvens_pct = cobertura_nuvens_pct;
        this.alerta_crime = alerta_crime;
        this.ordem_insercao = 0;
    }

    // Tira o Separador e as Quebras de Linha Para Não Bagunçar o Arquivo.
    private static String limpar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace(SEPARADOR, ",").replace("\n", " ").replace("\r", " ").trim();
    }

    // Converte o Registro em uma Linha do Arquivo.
    public String paraLinha() {
        return String.format(Locale.US, "%d;%s;%s;%s;%s;%s;%.2f;%d;%s;%d",
                id, nome_arquivo, satelite, data_captura, cidade, pais,
                area_desmatada_ha, cobertura_nuvens_pct, alerta_crime, ordem_insercao);
    }

    // Caminho Inverso: de uma Linha do Arquivo de Volta Para o Registro.
    public static RegistroImagem deLinha(String linha) {
        String[] partes = linha.split(SEPARADOR, -1);
        if (partes.length != TOTAL_CAMPOS) {
            throw new IllegalArgumentException(
                    "Linha com " + partes.length + " campos (esperado " + TOTAL_CAMPOS + "): " + linha);
        }
        try {
            RegistroImagem registro = new RegistroImagem(
                    Integer.parseInt(partes[0].trim()),
                    partes[1],
                    partes[2],
                    LocalDate.parse(partes[3].trim()),
                    partes[4],
                    partes[5],
                    Double.parseDouble(partes[6].trim()),
                    Integer.parseInt(partes[7].trim()),
                    Boolean.parseBoolean(partes[8].trim()));
            registro.ordem_insercao = Long.parseLong(partes[9].trim());
            return registro;
        } catch (RuntimeException erro) {
            throw new IllegalArgumentException("Linha inválida: " + linha, erro);
        }
    }

    public static String cabecalho() {
        return String.format("%-5s %-24s %-14s %-10s %-18s %-10s %10s %7s %-6s %6s",
                "ID", "ARQUIVO", "SATELITE", "DATA", "CIDADE", "PAIS",
                "AREA(ha)", "NUVENS", "ALERTA", "ORDEM");
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%-5d %-24s %-14s %-10s %-18s %-10s %10.2f %6d%% %-6s %6d",
                id, nome_arquivo, satelite, data_captura, cidade, pais,
                area_desmatada_ha, cobertura_nuvens_pct, alerta_crime ? "SIM" : "NAO",
                ordem_insercao);
    }

    // Ordenação Natural por Id (os Algoritmos de Ordenação Podem Usar Outro Critério).
    @Override
    public int compareTo(RegistroImagem outro) {
        return Integer.compare(this.id, outro.id);
    }

    public int getId() {
        return id;
    }

    public String getNomeArquivo() {
        return nome_arquivo;
    }

    public void setNomeArquivo(String nome_arquivo) {
        this.nome_arquivo = limpar(nome_arquivo);
    }

    public String getSatelite() {
        return satelite;
    }

    public void setSatelite(String satelite) {
        this.satelite = limpar(satelite);
    }

    public LocalDate getDataCaptura() {
        return data_captura;
    }

    public void setDataCaptura(LocalDate data_captura) {
        this.data_captura = data_captura;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = limpar(cidade);
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = limpar(pais);
    }

    public double getAreaDesmatadaHa() {
        return area_desmatada_ha;
    }

    public void setAreaDesmatadaHa(double area_desmatada_ha) {
        this.area_desmatada_ha = area_desmatada_ha;
    }

    public int getCoberturaNuvensPct() {
        return cobertura_nuvens_pct;
    }

    public void setCoberturaNuvensPct(int cobertura_nuvens_pct) {
        this.cobertura_nuvens_pct = cobertura_nuvens_pct;
    }

    public boolean isAlertaCrime() {
        return alerta_crime;
    }

    public void setAlertaCrime(boolean alerta_crime) {
        this.alerta_crime = alerta_crime;
    }

    public long getOrdemInsercao() {
        return ordem_insercao;
    }

    public void setOrdemInsercao(long ordem_insercao) {
        this.ordem_insercao = ordem_insercao;
    }
}