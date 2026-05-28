package com.olympia.controller;

import com.olympia.dto.response.ApiResponse;
import com.olympia.enums.TipoFormacao;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/formacoes")
@Tag(name = "Formações", description = "Formações musicais disponíveis")
public class FormacaoController {

    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listar() {
        List<Map<String, Object>> formacoes = Arrays.stream(TipoFormacao.values())
                .map(f -> Map.<String, Object>of(
                        "nome", f.name(),
                        "descricao", f.getDescricao(),
                        "composicao", f.getComposicao(),
                        "valorBase", f.getValorBase()
                )).toList();
        return ResponseEntity.ok(ApiResponse.ok("Formações disponíveis", formacoes));
    }
}
