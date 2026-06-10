package com.olympia.repository;

import com.olympia.entity.Musico;
import com.olympia.enums.TipoInstrumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MusicoRepository extends JpaRepository<Musico, Long> {
    List<Musico> findByInstrumentoAndAtivoTrue(TipoInstrumento instrumento);
    List<Musico> findByAtivoTrue();
}
