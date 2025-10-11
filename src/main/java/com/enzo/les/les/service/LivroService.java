package com.enzo.les.les.service;

import java.util.List;

import com.enzo.les.les.dtos.LivroDTO;
import com.enzo.les.les.exceptions.ResourceNotFoundException;
import com.enzo.les.les.model.entities.Livro;
import com.enzo.les.les.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    @Autowired
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<LivroDTO> listarTodos(){
        return livroRepository.findAll().stream().map(Livro::mapToDTO).toList();
    }

    public LivroDTO getLivroById(long id){
        Livro livro = livroRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cartão não encontrado: " + id));
        return livro.mapToDTO();
    }



}
