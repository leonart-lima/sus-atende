package com.sus.atende;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sus.atende.dto.CriarTriagemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Testes de Integração da API")
class SusAtendeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve retornar todas as unidades de saúde")
    void testListarTodasUnidades() throws Exception {
        mockMvc.perform(get("/api/v1/unidades")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].nome", notNullValue()))
                .andExpect(jsonPath("$[0].tipo", notNullValue()));
    }

    @Test
    @DisplayName("Deve obter uma unidade específica")
    void testObterUnidadeEspecifica() throws Exception {
        mockMvc.perform(get("/api/v1/unidades/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("UBS Zona Sul")));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar unidade inexistente")
    void testObterUnidadeInexistente() throws Exception {
        mockMvc.perform(get("/api/v1/unidades/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar unidades por tipo")
    void testListarUnidadesPorTipo() throws Exception {
        mockMvc.perform(get("/api/v1/unidades/tipo/UBS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].tipo", is("UBS")));
    }

    @Test
    @DisplayName("Deve criar triagem com sucesso")
    void testCriarTriagemComSucesso() throws Exception {
        CriarTriagemRequest request = CriarTriagemRequest.builder()
                .nomePaciente("Pedro Oliveira")
                .cpf("11144477788")
                .idade(35)
                .telefone("(11) 97777-7777")
                .email("pedro@example.com")
                .sintomas("Tosse seca")
                .temperatura(37.2)
                .diasComSintomas(1)
                .historicoMorbidades("Asma")
                .sexo("MASCULINO")
                .build();

        mockMvc.perform(post("/api/v1/triagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nomePaciente", is("Pedro Oliveira")))
                .andExpect(jsonPath("$.nivelUrgencia", notNullValue()))
                .andExpect(jsonPath("$.unidadeSugerida", notNullValue()));
    }

    @Test
    @DisplayName("Deve criar triagem com urgência alta para sintomas graves")
    void testCriarTriagemUrgenciaAlta() throws Exception {
        CriarTriagemRequest request = CriarTriagemRequest.builder()
                .nomePaciente("Ana Costa")
                .cpf("22244477788")
                .idade(55)
                .telefone("(11) 96666-6666")
                .email("ana@example.com")
                .sintomas("Falta de ar, dor no peito, palpitação")
                .temperatura(40.0)
                .diasComSintomas(5)
                .historicoMorbidades("Doença cardíaca")
                .sexo("FEMININO")
                .build();

        mockMvc.perform(post("/api/v1/triagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nivelUrgencia", is("ALTA")))
                .andExpect(jsonPath("$.unidadeSugerida.tipo", is("PRONTO_SOCORRO")));
    }

    @Test
    @DisplayName("Deve retornar erro ao criar triagem com dados inválidos")
    void testCriarTriagemDadosInvalidos() throws Exception {
        CriarTriagemRequest request = CriarTriagemRequest.builder()
                .nomePaciente("") // inválido
                .cpf("123") // inválido
                .idade(200) // inválido
                .telefone("(11) 97777-7777")
                .email("email_invalido") // inválido
                .sintomas("Sintoma")
                .temperatura(45.5) // inválido
                .diasComSintomas(1)
                .build();

        mockMvc.perform(post("/api/v1/triagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve obter resultado de triagem realizada")
    void testObterResultadoTriagem() throws Exception {
        // Primeiro, criar uma triagem
        CriarTriagemRequest request = CriarTriagemRequest.builder()
                .nomePaciente("Lucas Ferreira")
                .cpf("33344477788")
                .idade(28)
                .telefone("(11) 95555-5555")
                .email("lucas@example.com")
                .sintomas("Dor de cabeça")
                .temperatura(37.5)
                .diasComSintomas(2)
                .sexo("MASCULINO")
                .build();

        String response = mockMvc.perform(post("/api/v1/triagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extrair ID e buscar
        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/api/v1/triagens/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomePaciente", is("Lucas Ferreira")))
                .andExpect(jsonPath("$.nivelUrgencia", notNullValue()));
    }

    @Test
    @DisplayName("Deve atualizar disponibilidade de unidade")
    void testAtualizarDisponibilidade() throws Exception {
        mockMvc.perform(put("/api/v1/unidades/1/disponibilidade")
                .param("novaDisponibilidade", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leitosDisponiveis", is(10)));
    }

    @Test
    @DisplayName("Deve retornar unidades com capacidade disponível")
    void testListarUnidadesComCapacidade() throws Exception {
        mockMvc.perform(get("/api/v1/unidades/disponibilidade/ativas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
}

