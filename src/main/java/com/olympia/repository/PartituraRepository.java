package com.olympia.repository;

import com.olympia.entity.Partitura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartituraRepository extends JpaRepository<Partitura, Long> {
    List<Partitura> findByDisponivelTrueOrderByNomeMusicaAsc();
    Optional<Partitura> findByNomeMusicaIgnoreCase(String nomeMusica);
    boolean existsByNomeMusicaIgnoreCase(String nomeMusica);
}
