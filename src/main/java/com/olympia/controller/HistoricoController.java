package com.olympia.controller;
import com.olympia.dto.response.ApiResponse;
import com.olympia.entity.HistoricoOperacao;
import com.olympia.repository.HistoricoOperacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/historico") @PreAuthorize("hasRole('ADMIN')")
public class HistoricoController {
    private final HistoricoOperacaoRepository historicoRepository;
    public HistoricoController(HistoricoOperacaoRepository h) { this.historicoRepository = h; }

    @GetMapping public ResponseEntity<ApiResponse<List<HistoricoOperacao>>> listar() { return ResponseEntity.ok(ApiResponse.ok("Histórico", historicoRepository.findAllByOrderByRealizadoEmDesc())); }
    @GetMapping("/pedido/{id}") public ResponseEntity<ApiResponse<List<HistoricoOperacao>>> listarPorPedido(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.ok("Histórico do pedido", historicoRepository.findByRegistroIdAndTabelaAfetadaOrderByRealizadoEmDesc(id, "pedidos"))); }
}
