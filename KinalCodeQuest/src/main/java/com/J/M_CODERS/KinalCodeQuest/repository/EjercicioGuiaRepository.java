package com.J.M_CODERS.KinalCodeQuest.repository;

import com.J.M_CODERS.KinalCodeQuest.model.entity.EjercicioGuia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjercicioGuiaRepository extends JpaRepository<EjercicioGuia, Integer> {
}

