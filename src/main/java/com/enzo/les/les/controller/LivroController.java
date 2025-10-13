package com.enzo.les.les.controller;

import com.enzo.les.les.dtos.LivroDTO;
import com.enzo.les.les.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@Tag(name = "Livros", description = "API de gerenciamento de livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @Operation(summary = "Listar todos os livros")
    @GetMapping
    public ResponseEntity<List<LivroDTO>> listarTodos() {
        return ResponseEntity.ok(livroService.listarTodos());
    }

    @Operation(summary = "Obter livro por ID")
    @GetMapping("/{id}")
    public ResponseEntity<LivroDTO> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.getLivroById(id));
    }
}