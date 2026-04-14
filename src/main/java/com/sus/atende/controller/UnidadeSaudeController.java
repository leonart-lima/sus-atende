package com.sus.atende.controller;

import com.sus.atende.dto.UnidadeSaudeDTO;
import com.sus.atende.service.UnidadeSaudeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/unidades")
@Tag(name = "Unidades de Saúde", description = "Endpoints para gerenciamento de unidades de saúde do SUS")
public class UnidadeSaudeController {

    private static final Logger log = LoggerFactory.getLogger(UnidadeSaudeController.class);

    private final UnidadeSaudeService unidadeSaudeService;

    public UnidadeSaudeController(UnidadeSaudeService unidadeSaudeService) {
        this.unidadeSaudeService = unidadeSaudeService;
    }

    @GetMapping
    @Operation(
            summary = "Listar todas as unidades",
            description = "Retorna lista completa de todas as unidades de saúde cadastradas"
    )
    public ResponseEntity<List<UnidadeSaudeDTO>> listarTodasUnidades() {
        log.info("Listando todas as unidades de saúde");
        List<UnidadeSaudeDTO> unidades = unidadeSaudeService.listarTodasUnidades();
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obter detalhes de unidade",
            description = "Retorna os detalhes completos de uma unidade específica"
    )
    public ResponseEntity<UnidadeSaudeDTO> obterUnidade(@PathVariable Long id) {
        log.info("Buscando unidade com ID: {}", id);
        try {
            UnidadeSaudeDTO unidade = unidadeSaudeService.obterUnidade(id);
            return ResponseEntity.ok(unidade);
        } catch (IllegalArgumentException e) {
            log.warn("Unidade não encontrada: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(
            summary = "Listar unidades por tipo",
            description = "Retorna unidades filtradas por tipo (UBS, POLICLINICA, PRONTO_SOCORRO, HOSPITAL)"
    )
    public ResponseEntity<List<UnidadeSaudeDTO>> listarPorTipo(@PathVariable String tipo) {
        log.info("Listando unidades do tipo: {}", tipo);
        List<UnidadeSaudeDTO> unidades = unidadeSaudeService.listarPorTipo(tipo);
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("/disponibilidade/ativas")
    @Operation(
            summary = "Listar unidades com capacidade disponível",
            description = "Retorna apenas unidades que possuem leitos/atendimentos disponíveis"
    )
    public ResponseEntity<List<UnidadeSaudeDTO>> listarComCapacidade() {
        log.info("Listando unidades com capacidade disponível");
        List<UnidadeSaudeDTO> unidades = unidadeSaudeService.listarComCapacidade();
        return ResponseEntity.ok(unidades);
    }

    @PutMapping("/{id}/disponibilidade")
    @Operation(
            summary = "Atualizar disponibilidade de leitos",
            description = "Atualiza o número de leitos/atendimentos disponíveis em uma unidade"
    )
    public ResponseEntity<UnidadeSaudeDTO> atualizarDisponibilidade(
            @PathVariable Long id,
            @RequestParam Integer novaDisponibilidade) {
        log.info("Atualizando disponibilidade da unidade {}: {}", id, novaDisponibilidade);
        try {
            UnidadeSaudeDTO unidade = unidadeSaudeService.atualizarDisponibilidade(id, novaDisponibilidade);
            return ResponseEntity.ok(unidade);
        } catch (IllegalArgumentException e) {
            log.warn("Erro ao atualizar disponibilidade: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    }
}
