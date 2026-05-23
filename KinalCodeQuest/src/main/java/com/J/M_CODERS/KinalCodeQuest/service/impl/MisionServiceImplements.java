package com.J.M_CODERS.KinalCodeQuest.service.impl;

import com.J.M_CODERS.KinalCodeQuest.model.entity.Mision;
import com.J.M_CODERS.KinalCodeQuest.repository.MisionRepository;
import com.J.M_CODERS.KinalCodeQuest.service.evaluator.MisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MisionServiceImplements implements MisionService {

    @Autowired
    private MisionRepository misionRepository;

    @Override
    public List<Mision> listarPorArea(Integer idArea) {
        return misionRepository.findByAreaIdArea(idArea);
    }

    @Override
    public Mision buscarPorId(Integer id) {
        return misionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Mision> listarTodas() {
        return misionRepository.findAll();
    }
}