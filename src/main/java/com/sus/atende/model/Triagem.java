package com.sus.atende.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "triagens")
public class Triagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unidade_id")
    private UnidadeSaude unidadeSugerida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelUrgencia nivelUrgencia;

    @Column(columnDefinition = "TEXT")
    private String sintomas;

    private Double temperatura;
    private Integer diasComSintomas;
    private Integer scoreTriagem;

    @Column(name = "data_triagem")
    private LocalDateTime dataTriagem;

    private String observacoes;

    public Triagem() {}

    public Triagem(Long id, Paciente paciente, UnidadeSaude unidadeSugerida, NivelUrgencia nivelUrgencia,
                   String sintomas, Double temperatura, Integer diasComSintomas, Integer scoreTriagem,
                   LocalDateTime dataTriagem, String observacoes) {
        this.id = id; this.paciente = paciente; this.unidadeSugerida = unidadeSugerida;
        this.nivelUrgencia = nivelUrgencia; this.sintomas = sintomas; this.temperatura = temperatura;
        this.diasComSintomas = diasComSintomas; this.scoreTriagem = scoreTriagem;
        this.dataTriagem = dataTriagem; this.observacoes = observacoes;
    }

    @PrePersist
    protected void onCreate() { dataTriagem = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public UnidadeSaude getUnidadeSugerida() { return unidadeSugerida; }
    public void setUnidadeSugerida(UnidadeSaude u) { this.unidadeSugerida = u; }
    public NivelUrgencia getNivelUrgencia() { return nivelUrgencia; }
    public void setNivelUrgencia(NivelUrgencia n) { this.nivelUrgencia = n; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    public Integer getDiasComSintomas() { return diasComSintomas; }
    public void setDiasComSintomas(Integer d) { this.diasComSintomas = d; }
    public Integer getScoreTriagem() { return scoreTriagem; }
    public void setScoreTriagem(Integer s) { this.scoreTriagem = s; }
    public LocalDateTime getDataTriagem() { return dataTriagem; }
    public void setDataTriagem(LocalDateTime d) { this.dataTriagem = d; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public static TriagemBuilder builder() { return new TriagemBuilder(); }

    public static class TriagemBuilder {
        private Long id; private Paciente paciente; private UnidadeSaude unidadeSugerida;
        private NivelUrgencia nivelUrgencia; private String sintomas; private Double temperatura;
        private Integer diasComSintomas; private Integer scoreTriagem;
        private LocalDateTime dataTriagem; private String observacoes;

        public TriagemBuilder id(Long id) { this.id = id; return this; }
        public TriagemBuilder paciente(Paciente p) { this.paciente = p; return this; }
        public TriagemBuilder unidadeSugerida(UnidadeSaude u) { this.unidadeSugerida = u; return this; }
        public TriagemBuilder nivelUrgencia(NivelUrgencia n) { this.nivelUrgencia = n; return this; }
        public TriagemBuilder sintomas(String s) { this.sintomas = s; return this; }
        public TriagemBuilder temperatura(Double t) { this.temperatura = t; return this; }
        public TriagemBuilder diasComSintomas(Integer d) { this.diasComSintomas = d; return this; }
        public TriagemBuilder scoreTriagem(Integer s) { this.scoreTriagem = s; return this; }
        public TriagemBuilder dataTriagem(LocalDateTime d) { this.dataTriagem = d; return this; }
        public TriagemBuilder observacoes(String o) { this.observacoes = o; return this; }
        public Triagem build() {
            return new Triagem(id, paciente, unidadeSugerida, nivelUrgencia, sintomas,
                    temperatura, diasComSintomas, scoreTriagem, dataTriagem, observacoes);
        }
    }

    public enum NivelUrgencia {
        BAIXA("Baixa Urgência - Pode aguardar sem risco", 0, 2),
        MEDIA("Média Urgência - Atendimento em poucas horas", 3, 5),
        ALTA("Alta Urgência - Atendimento imediato", 6, Integer.MAX_VALUE);

        private final String descricao;
        private final int scoreMinimo;
        private final int scoreMaximo;

        NivelUrgencia(String descricao, int scoreMinimo, int scoreMaximo) {
            this.descricao = descricao; this.scoreMinimo = scoreMinimo; this.scoreMaximo = scoreMaximo;
        }
        public String getDescricao() { return descricao; }
        public static NivelUrgencia fromScore(int score) {
            if (score <= 2) return BAIXA;
            if (score <= 5) return MEDIA;
            return ALTA;
        }
    }
}
