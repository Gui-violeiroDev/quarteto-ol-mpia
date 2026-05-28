package com.olympia.controller;
import com.olympia.dto.request.PartituraRequest;
import com.olympia.dto.response.ApiResponse;
import com.olympia.dto.response.PartituraResponse;
import com.olympia.service.impl.PartituraService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/partituras")
public class PartituraController {
    private final PartituraService partituraService;
    public PartituraController(PartituraService partituraService) { this.partituraService = partituraService; }

    @GetMapping public ResponseEntity<ApiResponse<List<PartituraResponse>>> listar() { return ResponseEntity.ok(ApiResponse.ok("Partituras", partituraService.listarTodas())); }
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<PartituraResponse>> buscar(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.ok("Partitura", partituraService.buscarPorId(id))); }
    @PostMapping public ResponseEntity<ApiResponse<PartituraResponse>> criar(@Valid @RequestBody PartituraRequest req) { return ResponseEntity.ok(ApiResponse.ok("Partitura cadastrada", partituraService.criar(req))); }
    @PutMapping("/{id}") public ResponseEntity<ApiResponse<PartituraResponse>> atualizar(@PathVariable Long id, @Valid @RequestBody PartituraRequest req) { return ResponseEntity.ok(ApiResponse.ok("Atualizada", partituraService.atualizar(id, req))); }
    @DeleteMapping("/{id}") public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) { partituraService.deletar(id); return ResponseEntity.ok(ApiResponse.ok("Removida", null)); }
}
