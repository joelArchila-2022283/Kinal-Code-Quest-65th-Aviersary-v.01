package com.J.M_CODERS.KinalCodeQuest.service.evaluator;

import com.J.M_CODERS.KinalCodeQuest.model.entity.AreaTecnica;
import java.util.List;

public interface AreaTecnicaService {
    List<AreaTecnica> listarTodas();
    AreaTecnica buscarPorId(Integer id);
    AreaTecnica guardar(AreaTecnica area);
}