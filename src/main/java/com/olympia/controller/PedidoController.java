package com.olympia.controller;
import com.olympia.dto.request.PedidoRequest;
import com.olympia.dto.response.ApiResponse;
import com.olympia.dto.response.PedidoResponse;
import com.olympia.enums.StatusPedido;
import com.olympia.service.impl.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;
    public PedidoController(PedidoService pedidoService) { this.pedidoService = pedidoService; }

    @PostMapping
    public ResponseEntity<ApiResponse<PedidoResponse>> criar(@Valid @RequestBody PedidoRequest request, @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Pedido criado", pedidoService.criar(request, u.getUsername())));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<PedidoResponse>>> listar(@AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(ApiResponse.ok("Pedidos encontrados", pedidoService.listarPorUsuario(u.getUsername())));
    }
    @GetMapping("/todos") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PedidoResponse>>> listarTodos() {
        return ResponseEntity.ok(ApiResponse.ok("Todos os pedidos", pedidoService.listarTodos()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoResponse>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Pedido encontrado", pedidoService.buscarPorId(id)));
    }
    @PutMapping("/{id}/status") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PedidoResponse>> atualizarStatus(@PathVariable Long id, @RequestParam StatusPedido status, @AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(ApiResponse.ok("Status atualizado", pedidoService.atualizarStatus(id, status, u.getUsername())));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelar(@PathVariable Long id, @AuthenticationPrincipal UserDetails u) {
        pedidoService.deletar(id, u.getUsername()); return ResponseEntity.ok(ApiResponse.ok("Pedido cancelado", null));
    }
}
