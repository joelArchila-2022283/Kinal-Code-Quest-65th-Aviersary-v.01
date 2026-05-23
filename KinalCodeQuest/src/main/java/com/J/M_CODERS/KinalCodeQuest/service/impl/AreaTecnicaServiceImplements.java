package com.J.M_CODERS.KinalCodeQuest.service.impl;

import com.J.M_CODERS.KinalCodeQuest.model.entity.AreaTecnica;
import com.J.M_CODERS.KinalCodeQuest.repository.AreaTecnicaRepository;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.AreaTecnicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AreaTecnicaServiceImplements implements AreaTecnicaService {

    @Autowired
    private AreaTecnicaRepository areaTecnicaRepository;

    @Override
    public List<AreaTecnica> listarTodas() {
        return areaTecnicaRepository.findAll();
    }

    @Override
    public AreaTecnica buscarPorId(Integer id) {
        return areaTecnicaRepository.findById(id).orElse(null);
    }

    @Override
    public AreaTecnica guardar(AreaTecnica area) {
        return areaTecnicaRepository.save(area);
    }
}