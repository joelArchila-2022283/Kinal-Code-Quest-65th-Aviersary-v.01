package com.J.M_CODERS.KinalCodeQuest.repository;

import com.J.M_CODERS.KinalCodeQuest.model.entity.CapituloHistoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapituloHistoriaRepository extends JpaRepository<CapituloHistoria, Integer> {
    // Permite obtener por separado los capítulos principales o los secundarios de un nodo
    List<CapituloHistoria> findByIdCapituloPadre(Integer idCapituloPadre);

    List<CapituloHistoria> findByTipoContenido(String tipoContenido);
}