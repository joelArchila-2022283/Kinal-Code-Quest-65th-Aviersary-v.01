package com.J.M_CODERS.KinalCodeQuest.service.evaluator;

import com.J.M_CODERS.KinalCodeQuest.model.entity.Mision;
import java.util.List;

public interface MisionService {
    List<Mision> listarPorArea(Integer idArea);
    Mision buscarPorId(Integer id);
    List<Mision> listarTodas();
}