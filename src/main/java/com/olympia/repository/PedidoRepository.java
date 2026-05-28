package com.olympia.repository;

import com.olympia.entity.Pedido;
import com.olympia.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEmailClienteOrderByCriadoEmDesc(String emailCliente);
    List<Pedido> findByStatusOrderByCriadoEmDesc(StatusPedido status);
    List<Pedido> findAllByOrderByCriadoEmDesc();

    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId ORDER BY p.criadoEm DESC")
    List<Pedido> findByUsuarioId(Long usuarioId);
}
