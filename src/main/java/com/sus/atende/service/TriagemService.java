package com.sus.atende.service;

import com.sus.atende.dto.CriarTriagemRequest;
import com.sus.atende.dto.TriagemResponse;
import com.sus.atende.dto.UnidadeSaudeDTO;
import com.sus.atende.model.Paciente;
import com.sus.atende.model.Triagem;
import com.sus.atende.model.UnidadeSaude;
import com.sus.atende.repository.PacienteRepository;
import com.sus.atende.repository.TriagemRepository;
import com.sus.atende.repository.UnidadeSaudeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TriagemService {

    private static final Logger log = LoggerFactory.getLogger(TriagemService.class);

    private final TriagemRepository triagemRepository;
    private final PacienteRepository pacienteRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;

    public TriagemService(TriagemRepository triagemRepository,
                          PacienteRepository pacienteRepository,
                          UnidadeSaudeRepository unidadeSaudeRepository) {
        this.triagemRepository = triagemRepository;
        this.pacienteRepository = pacienteRepository;
        this.unidadeSaudeRepository = unidadeSaudeRepository;
    }

    /**
     * Realiza triagem completa: cria paciente, calcula score de urgência,
     * e sugere unidade mais apropriada.
     */
    public TriagemResponse criarTriagem(CriarTriagemRequest request) {
        log.info("Iniciando triagem para paciente: {}", request.getNomePaciente());

        // Buscar ou criar paciente
        Paciente paciente = pacienteRepository.findByCpf(request.getCpf())
                .orElseGet(() -> criarNovoPaciente(request));

        // Calcular score de urgência
        int score = calcularScoreUrgencia(request);
        log.debug("Score de urgência calculado: {}", score);

        // Determinar nível de urgência
        Triagem.NivelUrgencia nivelUrgencia = Triagem.NivelUrgencia.fromScore(score);

        // Sugerir unidade apropriada
        UnidadeSaude unidadeSugerida = sugerirUnidadeApropiada(nivelUrgencia, request.getSintomas());

        // Obter unidades alternativas (mesma categoria ou próximas)
        List<UnidadeSaudeDTO> alternativas = obterUnidadesAlternativas(unidadeSugerida, nivelUrgencia);

        // Criar e salvar triagem
        Triagem triagem = Triagem.builder()
                .paciente(paciente)
                .unidadeSugerida(unidadeSugerida)
                .nivelUrgencia(nivelUrgencia)
                .sintomas(request.getSintomas())
                .temperatura(request.getTemperatura())
                .diasComSintomas(request.getDiasComSintomas())
                .scoreTriagem(score)
                .observacoes(gerarObservacoes(request, score, nivelUrgencia))
                .build();

        triagem = triagemRepository.save(triagem);
        log.info("Triagem realizada com sucesso. ID: {}, Urgência: {}", triagem.getId(), nivelUrgencia);

        return TriagemResponse.fromTriagem(triagem, alternativas);
    }

    /**
     * Algoritmo de cálculo de score de urgência baseado em sintomas e sinais vitais
     */
    private int calcularScoreUrgencia(CriarTriagemRequest request) {
        int score = 0;

        // Febre
        if (request.getTemperatura() >= 39.5) {
            score += 3; // Febre muito alta
        } else if (request.getTemperatura() >= 38.5) {
            score += 2; // Febre moderada
        } else if (request.getTemperatura() >= 38) {
            score += 1; // Febre leve
        }

        // Duração dos sintomas
        if (request.getDiasComSintomas() > 7) {
            score += 1;
        }

        // Análise de sintomas críticos
        String sintomas = request.getSintomas().toLowerCase();
        if (sintomas.contains("falta de ar") || sintomas.contains("dispneia") || sintomas.contains("dificuldade respiratória")) {
            score += 3;
        }
        if (sintomas.contains("dor no peito") || sintomas.contains("palpitação")) {
            score += 3;
        }
        if (sintomas.contains("vômito") || sintomas.contains("diarreia")) {
            score += 1;
        }
        if (sintomas.contains("dor") && !sintomas.contains("leve")) {
            score += 2;
        }
        if (sintomas.contains("tonteira") || sintomas.contains("desmaio") || sintomas.contains("tontura")) {
            score += 2;
        }
        if (sintomas.contains("convulsão") || sintomas.contains("paralisia")) {
            score += 3;
        }
        if (sintomas.contains("ferimento") || sintomas.contains("sangramento")) {
            score += 2;
        }
        if (sintomas.contains("alergia") && sintomas.contains("inchação")) {
            score += 2;
        }

        // Histórico de morbidades agrava o score
        if (request.getHistoricoMorbidades() != null && !request.getHistoricoMorbidades().isEmpty()) {
            String historico = request.getHistoricoMorbidades().toLowerCase();
            if (historico.contains("diabetes") || historico.contains("hipertensão") || historico.contains("coração")) {
                score += 1;
            }
        }

        return Math.min(score, 10); // Score máximo de 10
    }

    /**
     * Sugere a unidade mais apropriada baseada no nível de urgência e leitos disponíveis
     */
    private UnidadeSaude sugerirUnidadeApropiada(Triagem.NivelUrgencia urgencia, String sintomas) {
        List<UnidadeSaude> unidades;

        switch (urgencia) {
            case ALTA:
                // Priorizar Pronto-Socorro para casos graves
                unidades = unidadeSaudeRepository.findByTipo(UnidadeSaude.TipoUnidade.PRONTO_SOCORRO);
                if (unidades.isEmpty()) {
                    unidades = unidadeSaudeRepository.findByTipo(UnidadeSaude.TipoUnidade.HOSPITAL);
                }
                break;
            case MEDIA:
                // Policlínica ou UBS com bom funcionamento
                unidades = unidadeSaudeRepository.findByTipo(UnidadeSaude.TipoUnidade.POLICLINICA);
                if (unidades.isEmpty()) {
                    unidades = unidadeSaudeRepository.findByTipo(UnidadeSaude.TipoUnidade.UBS);
                }
                break;
            case BAIXA:
            default:
                // UBS para casos simples
                unidades = unidadeSaudeRepository.findByTipo(UnidadeSaude.TipoUnidade.UBS);
                if (unidades.isEmpty()) {
                    unidades = unidadeSaudeRepository.findByTipo(UnidadeSaude.TipoUnidade.POLICLINICA);
                }
                break;
        }

        // Retornar unidade com maior disponibilidade (menor taxa de ocupação)
        return unidades.stream()
                .min((u1, u2) -> Double.compare(u1.getTaxaOcupacao(), u2.getTaxaOcupacao()))
                .orElse(unidadeSaudeRepository.findAll().get(0)); // Fallback para primeira unidade
    }

    /**
     * Obtém unidades alternativas para o paciente
     */
    private List<UnidadeSaudeDTO> obterUnidadesAlternativas(UnidadeSaude unidadePrimaria, Triagem.NivelUrgencia urgencia) {
        List<UnidadeSaude> alternativas = unidadeSaudeRepository.findAll().stream()
                .filter(u -> !u.getId().equals(unidadePrimaria.getId()))
                .filter(u -> u.getLeitosDisponiveis() > 0)
                .filter(u -> {
                    // Filtrar por tipo de unidade apropriado
                    if (urgencia == Triagem.NivelUrgencia.ALTA) {
                        return u.getTipo() == UnidadeSaude.TipoUnidade.PRONTO_SOCORRO ||
                               u.getTipo() == UnidadeSaude.TipoUnidade.HOSPITAL;
                    } else if (urgencia == Triagem.NivelUrgencia.MEDIA) {
                        return u.getTipo() == UnidadeSaude.TipoUnidade.POLICLINICA ||
                               u.getTipo() == UnidadeSaude.TipoUnidade.UBS;
                    } else {
                        return u.getTipo() == UnidadeSaude.TipoUnidade.UBS;
                    }
                })
                .sorted((u1, u2) -> Double.compare(u1.getTaxaOcupacao(), u2.getTaxaOcupacao()))
                .limit(3)
                .collect(Collectors.toList());

        return alternativas.stream()
                .map(UnidadeSaudeDTO::fromUnidade)
                .collect(Collectors.toList());
    }

    /**
     * Cria um novo paciente a partir dos dados da triagem
     */
    private Paciente criarNovoPaciente(CriarTriagemRequest request) {
        Paciente paciente = Paciente.builder()
                .nome(request.getNomePaciente())
                .cpf(request.getCpf())
                .idade(request.getIdade())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .historicoMorbidades(request.getHistoricoMorbidades())
                .sexo(request.getSexo() != null ? Paciente.Sexo.valueOf(request.getSexo().toUpperCase()) : Paciente.Sexo.OUTRO)
                .build();

        return pacienteRepository.save(paciente);
    }

    /**
     * Gera observações úteis para o paciente baseado na triagem
     */
    private String gerarObservacoes(CriarTriagemRequest request, int score, Triagem.NivelUrgencia urgencia) {
        StringBuilder obs = new StringBuilder();

        obs.append("Nível de Urgência: ").append(urgencia.getDescricao()).append(". ");

        if (urgencia == Triagem.NivelUrgencia.ALTA) {
            obs.append("PROCURE ATENDIMENTO IMEDIATO. ");
        } else if (urgencia == Triagem.NivelUrgencia.MEDIA) {
            obs.append("Procure atendimento em poucas horas. ");
        } else {
            obs.append("Você pode agendar consulta sem pressa. ");
        }

        if (request.getTemperatura() >= 40) {
            obs.append("Febre muito alta detectada. ");
        }

        return obs.toString();
    }

    /**
     * Busca uma triagem anterior do paciente
     */
    public TriagemResponse obterTriagem(Long id) {
        Triagem triagem = triagemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Triagem não encontrada com ID: " + id));

        List<UnidadeSaudeDTO> alternativas = obterUnidadesAlternativas(triagem.getUnidadeSugerida(), triagem.getNivelUrgencia());
        return TriagemResponse.fromTriagem(triagem, alternativas);
    }
}
