package com.sus.atende.dto;

import com.sus.atende.model.UnidadeSaude;
import java.util.List;

public class UnidadeSaudeDTO {

    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String tipo;
    private Integer capacidadeTotalLeitos;
    private Integer leitosDisponiveis;
    private Double taxaOcupacao;
    private List<String> especialidades;

    public UnidadeSaudeDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getCapacidadeTotalLeitos() { return capacidadeTotalLeitos; }
    public void setCapacidadeTotalLeitos(Integer c) { this.capacidadeTotalLeitos = c; }
    public Integer getLeitosDisponiveis() { return leitosDisponiveis; }
    public void setLeitosDisponiveis(Integer l) { this.leitosDisponiveis = l; }
    public Double getTaxaOcupacao() { return taxaOcupacao; }
    public void setTaxaOcupacao(Double t) { this.taxaOcupacao = t; }
    public List<String> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<String> especialidades) { this.especialidades = especialidades; }

    public static UnidadeSaudeDTOBuilder builder() { return new UnidadeSaudeDTOBuilder(); }

    public static class UnidadeSaudeDTOBuilder {
        private Long id; private String nome; private String endereco; private String telefone;
        private String tipo; private Integer capacidadeTotalLeitos; private Integer leitosDisponiveis;
        private Double taxaOcupacao; private List<String> especialidades;

        public UnidadeSaudeDTOBuilder id(Long id) { this.id = id; return this; }
        public UnidadeSaudeDTOBuilder nome(String nome) { this.nome = nome; return this; }
        public UnidadeSaudeDTOBuilder endereco(String e) { this.endereco = e; return this; }
        public UnidadeSaudeDTOBuilder telefone(String t) { this.telefone = t; return this; }
        public UnidadeSaudeDTOBuilder tipo(String tipo) { this.tipo = tipo; return this; }
        public UnidadeSaudeDTOBuilder capacidadeTotalLeitos(Integer c) { this.capacidadeTotalLeitos = c; return this; }
        public UnidadeSaudeDTOBuilder leitosDisponiveis(Integer l) { this.leitosDisponiveis = l; return this; }
        public UnidadeSaudeDTOBuilder taxaOcupacao(Double t) { this.taxaOcupacao = t; return this; }
        public UnidadeSaudeDTOBuilder especialidades(List<String> e) { this.especialidades = e; return this; }
        public UnidadeSaudeDTO build() {
            UnidadeSaudeDTO dto = new UnidadeSaudeDTO();
            dto.id = id; dto.nome = nome; dto.endereco = endereco; dto.telefone = telefone;
            dto.tipo = tipo; dto.capacidadeTotalLeitos = capacidadeTotalLeitos;
            dto.leitosDisponiveis = leitosDisponiveis; dto.taxaOcupacao = taxaOcupacao;
            dto.especialidades = especialidades;
            return dto;
        }
    }

    public static UnidadeSaudeDTO fromUnidade(UnidadeSaude unidade) {
        if (unidade == null) return null;
        return UnidadeSaudeDTO.builder()
                .id(unidade.getId())
                .nome(unidade.getNome())
                .endereco(unidade.getEndereco())
                .telefone(unidade.getTelefone())
                .tipo(unidade.getTipo().name())
                .capacidadeTotalLeitos(unidade.getCapacidadeTotalLeitos())
                .leitosDisponiveis(unidade.getLeitosDisponiveis())
                .taxaOcupacao(unidade.getTaxaOcupacao())
                .especialidades(unidade.getEspecialidades())
                .build();
    }
}
