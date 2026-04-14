package com.sus.atende.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private Integer idade;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private String historicoMorbidades;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    public Paciente() {}

    public Paciente(Long id, String nome, String cpf, Integer idade, String telefone, String email,
                    Sexo sexo, String historicoMorbidades, LocalDateTime dataCriacao) {
        this.id = id; this.nome = nome; this.cpf = cpf; this.idade = idade;
        this.telefone = telefone; this.email = email; this.sexo = sexo;
        this.historicoMorbidades = historicoMorbidades; this.dataCriacao = dataCriacao;
    }

    @PrePersist
    protected void onCreate() { dataCriacao = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public String getHistoricoMorbidades() { return historicoMorbidades; }
    public void setHistoricoMorbidades(String h) { this.historicoMorbidades = h; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime d) { this.dataCriacao = d; }

    public static PacienteBuilder builder() { return new PacienteBuilder(); }

    public static class PacienteBuilder {
        private Long id; private String nome; private String cpf; private Integer idade;
        private String telefone; private String email; private Sexo sexo; private String historicoMorbidades;

        public PacienteBuilder id(Long id) { this.id = id; return this; }
        public PacienteBuilder nome(String nome) { this.nome = nome; return this; }
        public PacienteBuilder cpf(String cpf) { this.cpf = cpf; return this; }
        public PacienteBuilder idade(Integer idade) { this.idade = idade; return this; }
        public PacienteBuilder telefone(String t) { this.telefone = t; return this; }
        public PacienteBuilder email(String email) { this.email = email; return this; }
        public PacienteBuilder sexo(Sexo sexo) { this.sexo = sexo; return this; }
        public PacienteBuilder historicoMorbidades(String h) { this.historicoMorbidades = h; return this; }
        public Paciente build() {
            return new Paciente(id, nome, cpf, idade, telefone, email, sexo, historicoMorbidades, null);
        }
    }

    public enum Sexo { MASCULINO, FEMININO, OUTRO }
}
