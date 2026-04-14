package com.sus.atende.controller;

import com.sus.atende.dto.CriarTriagemRequest;
import com.sus.atende.dto.TriagemResponse;
import com.sus.atende.service.TriagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/triagens")
@Tag(name = "Triagem", description = "Endpoints para realização de triagem e direcionamento de pacientes")
public class TriagemController {

    private static final Logger log = LoggerFactory.getLogger(TriagemController.class);

    private final TriagemService triagemService;

    public TriagemController(TriagemService triagemService) {
        this.triagemService = triagemService;
    }

    @PostMapping
    @Operation(
            summary = "Realizar triagem de paciente",
            description = "Realiza triagem completa do paciente, calcula nível de urgência e sugere unidade apropriada"
    )
    public ResponseEntity<TriagemResponse> criarTriagem(@Valid @RequestBody CriarTriagemRequest request) {
        log.info("Recebendo requisição de triagem para: {}", request.getNomePaciente());
        try {
            TriagemResponse response = triagemService.criarTriagem(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Erro ao realizar triagem: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obter resultado de triagem",
            description = "Retorna os detalhes completos de uma triagem já realizada"
    )
    public ResponseEntity<TriagemResponse> obterTriagem(@PathVariable Long id) {
        log.info("Buscando triagem com ID: {}", id);
        try {
            TriagemResponse response = triagemService.obterTriagem(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Triagem não encontrada: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
    }
}
