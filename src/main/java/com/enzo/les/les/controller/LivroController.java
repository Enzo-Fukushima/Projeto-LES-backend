package com.enzo.les.les.controller;

import com.enzo.les.les.dtos.LivroDTO;
import com.enzo.les.les.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @Operation(summary = "Todos os livros")
    @GetMapping()
    public List<LivroDTO> GetAll() {
        return livroService.listarTodos();
    }
    @Operation(summary = "livro por ID")
    @GetMapping()
    public LivroDTO GetLivroById(long id) {
        return livroService.getLivroById(id);
    }
}
