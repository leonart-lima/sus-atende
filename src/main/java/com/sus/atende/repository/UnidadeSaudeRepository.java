package com.sus.atende.repository;

import com.sus.atende.model.UnidadeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, Long> {

    Optional<UnidadeSaude> findByNome(String nome);

    List<UnidadeSaude> findByTipo(UnidadeSaude.TipoUnidade tipo);

    @Query("SELECT u FROM UnidadeSaude u WHERE u.leitosDisponiveis > 0 ORDER BY u.leitosDisponiveis DESC")
    List<UnidadeSaude> findUnidadesComCapacidade();

}

