package com.olympia.repository;

import com.olympia.entity.HistoricoOperacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoricoOperacaoRepository extends JpaRepository<HistoricoOperacao, Long> {
    List<HistoricoOperacao> findAllByOrderByRealizadoEmDesc();
    List<HistoricoOperacao> findByTabelaAfetadaOrderByRealizadoEmDesc(String tabelaAfetada);
    List<HistoricoOperacao> findByRegistroIdAndTabelaAfetadaOrderByRealizadoEmDesc(Long registroId, String tabela);
}
