package com.enzo.les.les.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enzo.les.les.dtos.LivroDTO;
import com.enzo.les.les.model.entities.Livro;
import com.enzo.les.les.repository.LivroRepository;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Transactional(readOnly = true)
    public List<LivroDTO> listarTodos() {
        return livroRepository.findAll()
            .stream()
            .map(Livro::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LivroDTO getLivroById(Long id) {
        return livroRepository.findById(id)
            .map(Livro::mapToDTO)
            .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }
}
