package com.sus.atende.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "unidades_saude")
public class UnidadeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUnidade tipo;

    @Column(nullable = false)
    private Integer capacidadeTotalLeitos;

    @Column(nullable = false)
    private Integer leitosDisponiveis;

    @ElementCollection(fetch = FetchType.EAGER)
    @jakarta.persistence.CollectionTable(name = "unidades_saude_especialidades",
            joinColumns = @jakarta.persistence.JoinColumn(name = "unidade_saude_id"))
    @jakarta.persistence.Column(name = "especialidades")
    private List<String> especialidades;

    // Construtores
    public UnidadeSaude() {
    }

    public UnidadeSaude(Long id, String nome, String endereco, String telefone, TipoUnidade tipo,
                        Integer capacidadeTotalLeitos, Integer leitosDisponiveis, List<String> especialidades) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.tipo = tipo;
        this.capacidadeTotalLeitos = capacidadeTotalLeitos;
        this.leitosDisponiveis = leitosDisponiveis;
        this.especialidades = especialidades;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public TipoUnidade getTipo() {
        return tipo;
    }

    public void setTipo(TipoUnidade tipo) {
        this.tipo = tipo;
    }

    public Integer getCapacidadeTotalLeitos() {
        return capacidadeTotalLeitos;
    }

    public void setCapacidadeTotalLeitos(Integer capacidadeTotalLeitos) {
        this.capacidadeTotalLeitos = capacidadeTotalLeitos;
    }

    public Integer getLeitosDisponiveis() {
        return leitosDisponiveis;
    }

    public void setLeitosDisponiveis(Integer leitosDisponiveis) {
        this.leitosDisponiveis = leitosDisponiveis;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }

    public Double getTaxaOcupacao() {
        return (double) (capacidadeTotalLeitos - leitosDisponiveis) / capacidadeTotalLeitos * 100;
    }

    public enum TipoUnidade {
        UBS("Unidade Básica de Saúde"),
        POLICLINICA("Policlínica"),
        PRONTO_SOCORRO("Pronto-Socorro"),
        HOSPITAL("Hospital");

        private final String descricao;

        TipoUnidade(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    public static UnidadeSaudeBuilder builder() {
        return new UnidadeSaudeBuilder();
    }

    public static class UnidadeSaudeBuilder {
        private Long id;
        private String nome;
        private String endereco;
        private String telefone;
        private TipoUnidade tipo;
        private Integer capacidadeTotalLeitos;
        private Integer leitosDisponiveis;
        private List<String> especialidades;

        public UnidadeSaudeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UnidadeSaudeBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public UnidadeSaudeBuilder endereco(String endereco) {
            this.endereco = endereco;
            return this;
        }

        public UnidadeSaudeBuilder telefone(String telefone) {
            this.telefone = telefone;
            return this;
        }

        public UnidadeSaudeBuilder tipo(TipoUnidade tipo) {
            this.tipo = tipo;
            return this;
        }

        public UnidadeSaudeBuilder capacidadeTotalLeitos(Integer capacidadeTotalLeitos) {
            this.capacidadeTotalLeitos = capacidadeTotalLeitos;
            return this;
        }

        public UnidadeSaudeBuilder leitosDisponiveis(Integer leitosDisponiveis) {
            this.leitosDisponiveis = leitosDisponiveis;
            return this;
        }

        public UnidadeSaudeBuilder especialidades(List<String> especialidades) {
            this.especialidades = especialidades;
            return this;
        }

        public UnidadeSaude build() {
            return new UnidadeSaude(id, nome, endereco, telefone, tipo, capacidadeTotalLeitos,
                    leitosDisponiveis, especialidades);
        }
    }
}


