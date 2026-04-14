package com.sus.atende.service;

import com.sus.atende.dto.UnidadeSaudeDTO;
import com.sus.atende.model.UnidadeSaude;
import com.sus.atende.repository.UnidadeSaudeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UnidadeSaudeService {

    private static final Logger log = LoggerFactory.getLogger(UnidadeSaudeService.class);

    private final UnidadeSaudeRepository unidadeSaudeRepository;

    public UnidadeSaudeService(UnidadeSaudeRepository unidadeSaudeRepository) {
        this.unidadeSaudeRepository = unidadeSaudeRepository;
    }

    /**
     * Lista todas as unidades de saúde
     */
    public List<UnidadeSaudeDTO> listarTodasUnidades() {
        return unidadeSaudeRepository.findAll().stream()
                .map(UnidadeSaudeDTO::fromUnidade)
                .collect(Collectors.toList());
    }

    /**
     * Obtém uma unidade específica pelo ID
     */
    public UnidadeSaudeDTO obterUnidade(Long id) {
        UnidadeSaude unidade = unidadeSaudeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada com ID: " + id));
        return UnidadeSaudeDTO.fromUnidade(unidade);
    }

    /**
     * Lista unidades por tipo
     */
    public List<UnidadeSaudeDTO> listarPorTipo(String tipo) {
        UnidadeSaude.TipoUnidade tipoUnidade = UnidadeSaude.TipoUnidade.valueOf(tipo.toUpperCase());
        return unidadeSaudeRepository.findByTipo(tipoUnidade).stream()
                .map(UnidadeSaudeDTO::fromUnidade)
                .collect(Collectors.toList());
    }

    /**
     * Lista unidades com capacidade disponível
     */
    public List<UnidadeSaudeDTO> listarComCapacidade() {
        return unidadeSaudeRepository.findUnidadesComCapacidade().stream()
                .map(UnidadeSaudeDTO::fromUnidade)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza disponibilidade de leitos de uma unidade
     */
    public UnidadeSaudeDTO atualizarDisponibilidade(Long id, Integer novaDisponibilidade) {
        UnidadeSaude unidade = unidadeSaudeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada com ID: " + id));

        if (novaDisponibilidade < 0 || novaDisponibilidade > unidade.getCapacidadeTotalLeitos()) {
            throw new IllegalArgumentException("Disponibilidade inválida");
        }

        unidade.setLeitosDisponiveis(novaDisponibilidade);
        unidade = unidadeSaudeRepository.save(unidade);
        log.info("Disponibilidade atualizada para unidade: {}", unidade.getNome());

        return UnidadeSaudeDTO.fromUnidade(unidade);
    }
}
