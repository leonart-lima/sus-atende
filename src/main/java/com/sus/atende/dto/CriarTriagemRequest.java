package com.sus.atende.dto;

import jakarta.validation.constraints.*;

public class CriarTriagemRequest {

    @NotBlank(message = "Nome do paciente é obrigatório")
    private String nomePaciente;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotNull(message = "Idade é obrigatória")
    @Min(0) @Max(150)
    private Integer idade;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Sintomas são obrigatórios")
    private String sintomas;

    @NotNull(message = "Temperatura é obrigatória")
    @Min(value = 35, message = "Temperatura mínima: 35°C")
    @Max(value = 45, message = "Temperatura máxima: 45°C")
    private Double temperatura;

    @NotNull(message = "Dias com sintomas é obrigatório")
    @Min(0) @Max(365)
    private Integer diasComSintomas;

    private String historicoMorbidades;
    private String sexo;

    public CriarTriagemRequest() {}

    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    public Integer getDiasComSintomas() { return diasComSintomas; }
    public void setDiasComSintomas(Integer diasComSintomas) { this.diasComSintomas = diasComSintomas; }
    public String getHistoricoMorbidades() { return historicoMorbidades; }
    public void setHistoricoMorbidades(String historicoMorbidades) { this.historicoMorbidades = historicoMorbidades; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final CriarTriagemRequest r = new CriarTriagemRequest();
        public Builder nomePaciente(String v) { r.nomePaciente = v; return this; }
        public Builder cpf(String v) { r.cpf = v; return this; }
        public Builder idade(Integer v) { r.idade = v; return this; }
        public Builder telefone(String v) { r.telefone = v; return this; }
        public Builder email(String v) { r.email = v; return this; }
        public Builder sintomas(String v) { r.sintomas = v; return this; }
        public Builder temperatura(Double v) { r.temperatura = v; return this; }
        public Builder diasComSintomas(Integer v) { r.diasComSintomas = v; return this; }
        public Builder historicoMorbidades(String v) { r.historicoMorbidades = v; return this; }
        public Builder sexo(String v) { r.sexo = v; return this; }
        public CriarTriagemRequest build() { return r; }
    }
}
