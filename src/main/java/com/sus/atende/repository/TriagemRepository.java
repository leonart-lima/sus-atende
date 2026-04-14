package com.sus.atende.repository;

import com.sus.atende.model.Triagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TriagemRepository extends JpaRepository<Triagem, Long> {

    List<Triagem> findByPacienteId(Long pacienteId);

}

