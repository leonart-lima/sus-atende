package com.sus.atende.service;

import com.sus.atende.dto.CriarTriagemRequest;
import com.sus.atende.dto.TriagemResponse;
import com.sus.atende.model.Paciente;
import com.sus.atende.model.Triagem;
import com.sus.atende.model.UnidadeSaude;
import com.sus.atende.repository.PacienteRepository;
import com.sus.atende.repository.TriagemRepository;
import com.sus.atende.repository.UnidadeSaudeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço de Triagem")
class TriagemServiceTest {

    @Mock
    private TriagemRepository triagemRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private UnidadeSaudeRepository unidadeSaudeRepository;

    @InjectMocks
    private TriagemService triagemService;

    private CriarTriagemRequest request;
    private UnidadeSaude unidadeUBS;
    private UnidadeSaude unidadePoliclinica;
    private UnidadeSaude pronto;

    @BeforeEach
    void setUp() {
        // Setup de dados de teste
        request = CriarTriagemRequest.builder()
                .nomePaciente("João Silva")
                .cpf("12345678901")
                .idade(45)
                .telefone("(11) 99999-9999")
                .email("joao@example.com")
                .sintomas("Dor de cabeça leve")
                .temperatura(37.5)
                .diasComSintomas(2)
                .historicoMorbidades("Hipertensão")
                .sexo("MASCULINO")
                .build();

        unidadeUBS = UnidadeSaude.builder()
                .id(1L)
                .nome("UBS Zona Sul")
                .endereco("Rua teste, 123")
                .telefone("(11) 3001-0001")
                .tipo(UnidadeSaude.TipoUnidade.UBS)
                .capacidadeTotalLeitos(20)
                .leitosDisponiveis(18)
                .especialidades(Arrays.asList("Clínica Geral", "Pediatria"))
                .build();

        unidadePoliclinica = UnidadeSaude.builder()
                .id(2L)
                .nome("Policlínica Regional")
                .endereco("Avenida teste, 500")
                .telefone("(11) 3001-0002")
                .tipo(UnidadeSaude.TipoUnidade.POLICLINICA)
                .capacidadeTotalLeitos(50)
                .leitosDisponiveis(35)
                .especialidades(Arrays.asList("Cardiologia", "Dermatologia"))
                .build();

        pronto = UnidadeSaude.builder()
                .id(3L)
                .nome("Pronto-Socorro Central")
                .endereco("Rua emergência, 500")
                .telefone("(11) 3001-0003")
                .tipo(UnidadeSaude.TipoUnidade.PRONTO_SOCORRO)
                .capacidadeTotalLeitos(100)
                .leitosDisponiveis(25)
                .especialidades(Arrays.asList("Emergência", "Traumatologia"))
                .build();
    }

    @Test
    @DisplayName("Deve criar triagem para paciente com urgência baixa")
    void testCriarTriagemUrgenciaLow() {
        // Arrange
        Paciente paciente = Paciente.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .idade(45)
                .telefone("(11) 99999-9999")
                .email("joao@example.com")
                .build();

        Triagem triagem = Triagem.builder()
                .id(1L)
                .paciente(paciente)
                .unidadeSugerida(unidadeUBS)
                .nivelUrgencia(Triagem.NivelUrgencia.BAIXA)
                .scoreTriagem(1)
                .build();

        when(pacienteRepository.findByCpf(request.getCpf())).thenReturn(Optional.of(paciente));
        when(unidadeSaudeRepository.findByTipo(UnidadeSaude.TipoUnidade.UBS)).thenReturn(Arrays.asList(unidadeUBS));
        when(unidadeSaudeRepository.findAll()).thenReturn(Arrays.asList(unidadeUBS, unidadePoliclinica));
        when(triagemRepository.save(any())).thenReturn(triagem);

        // Act
        TriagemResponse response = triagemService.criarTriagem(request);

        // Assert
        assertNotNull(response);
        assertEquals("BAIXA", response.getNivelUrgencia());
        assertEquals(1, response.getScoreTriagem());
        verify(triagemRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve direcionar para pronto-socorro em caso de alta urgência")
    void testCriarTriagemUrgenciaAlta() {
        // Arrange
        CriarTriagemRequest requestAlta = CriarTriagemRequest.builder()
                .nomePaciente("Maria Santos")
                .cpf("98765432109")
                .idade(60)
                .telefone("(11) 98888-8888")
                .email("maria@example.com")
                .sintomas("Falta de ar, dor no peito")
                .temperatura(39.5)
                .diasComSintomas(3)
                .historicoMorbidades("Doença cardíaca")
                .sexo("FEMININO")
                .build();

        Paciente paciente = Paciente.builder()
                .id(2L)
                .nome("Maria Santos")
                .cpf("98765432109")
                .idade(60)
                .build();

        Triagem triagem = Triagem.builder()
                .id(2L)
                .paciente(paciente)
                .unidadeSugerida(pronto)
                .nivelUrgencia(Triagem.NivelUrgencia.ALTA)
                .scoreTriagem(9)
                .build();

        when(pacienteRepository.findByCpf(requestAlta.getCpf())).thenReturn(Optional.of(paciente));
        when(unidadeSaudeRepository.findByTipo(UnidadeSaude.TipoUnidade.PRONTO_SOCORRO)).thenReturn(Arrays.asList(pronto));
        when(unidadeSaudeRepository.findAll()).thenReturn(Arrays.asList(unidadeUBS, unidadePoliclinica, pronto));
        when(triagemRepository.save(any())).thenReturn(triagem);

        // Act
        TriagemResponse response = triagemService.criarTriagem(requestAlta);

        // Assert
        assertNotNull(response);
        assertEquals("ALTA", response.getNivelUrgencia());
        assertTrue(response.getScoreTriagem() > 5);
        verify(triagemRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve retornar triagem existente pelo ID")
    void testObterTriagemExistente() {
        // Arrange
        Paciente paciente = Paciente.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .build();

        Triagem triagem = Triagem.builder()
                .id(1L)
                .paciente(paciente)
                .unidadeSugerida(unidadeUBS)
                .nivelUrgencia(Triagem.NivelUrgencia.BAIXA)
                .scoreTriagem(1)
                .build();

        when(triagemRepository.findById(1L)).thenReturn(Optional.of(triagem));
        when(unidadeSaudeRepository.findAll()).thenReturn(Arrays.asList(unidadeUBS, unidadePoliclinica));

        // Act
        TriagemResponse response = triagemService.obterTriagem(1L);

        // Assert
        assertNotNull(response);
        assertEquals("João Silva", response.getNomePaciente());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar triagem inexistente")
    void testObterTriagemInexistente() {
        // Arrange
        when(triagemRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> triagemService.obterTriagem(999L));
    }
}

