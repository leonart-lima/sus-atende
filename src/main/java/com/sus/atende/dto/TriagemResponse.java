package com.sus.atende.dto;

import com.sus.atende.model.Triagem;
import java.time.LocalDateTime;
import java.util.List;

public class TriagemResponse {

    private Long id;
    private String nomePaciente;
    private String cpf;
    private String nivelUrgencia;
    private String descricaoUrgencia;
    private Integer scoreTriagem;
    private UnidadeSaudeDTO unidadeSugerida;
    private List<UnidadeSaudeDTO> unidadesAlternativas;
    private String sintomas;
    private Double temperatura;
    private Integer diasComSintomas;
    private String observacoes;
    private LocalDateTime dataTriagem;

    public TriagemResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNivelUrgencia() { return nivelUrgencia; }
    public void setNivelUrgencia(String nivelUrgencia) { this.nivelUrgencia = nivelUrgencia; }
    public String getDescricaoUrgencia() { return descricaoUrgencia; }
    public void setDescricaoUrgencia(String descricaoUrgencia) { this.descricaoUrgencia = descricaoUrgencia; }
    public Integer getScoreTriagem() { return scoreTriagem; }
    public void setScoreTriagem(Integer scoreTriagem) { this.scoreTriagem = scoreTriagem; }
    public UnidadeSaudeDTO getUnidadeSugerida() { return unidadeSugerida; }
    public void setUnidadeSugerida(UnidadeSaudeDTO unidadeSugerida) { this.unidadeSugerida = unidadeSugerida; }
    public List<UnidadeSaudeDTO> getUnidadesAlternativas() { return unidadesAlternativas; }
    public void setUnidadesAlternativas(List<UnidadeSaudeDTO> unidadesAlternativas) { this.unidadesAlternativas = unidadesAlternativas; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    public Integer getDiasComSintomas() { return diasComSintomas; }
    public void setDiasComSintomas(Integer diasComSintomas) { this.diasComSintomas = diasComSintomas; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getDataTriagem() { return dataTriagem; }
    public void setDataTriagem(LocalDateTime dataTriagem) { this.dataTriagem = dataTriagem; }

    public static TriagemResponse fromTriagem(Triagem triagem, List<UnidadeSaudeDTO> alternativas) {
        TriagemResponse r = new TriagemResponse();
        r.id = triagem.getId();
        r.nomePaciente = triagem.getPaciente().getNome();
        r.cpf = triagem.getPaciente().getCpf();
        r.nivelUrgencia = triagem.getNivelUrgencia().name();
        r.descricaoUrgencia = triagem.getNivelUrgencia().getDescricao();
        r.scoreTriagem = triagem.getScoreTriagem();
        r.unidadeSugerida = UnidadeSaudeDTO.fromUnidade(triagem.getUnidadeSugerida());
        r.unidadesAlternativas = alternativas;
        r.sintomas = triagem.getSintomas();
        r.temperatura = triagem.getTemperatura();
        r.diasComSintomas = triagem.getDiasComSintomas();
        r.observacoes = triagem.getObservacoes();
        r.dataTriagem = triagem.getDataTriagem();
        return r;
    }
}
