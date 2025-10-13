package com.enzo.les.les.service;

import com.enzo.les.les.dtos.CategoriaDTO;
import com.enzo.les.les.model.entities.Categoria;
import com.enzo.les.les.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaDTO obterPorId(Long id) {
        return categoriaRepository.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    private CategoriaDTO mapToDTO(Categoria categoria) {
        return CategoriaDTO.builder()
            .id(categoria.getId())
            .nome(categoria.getNome())
            .build();
    }
}