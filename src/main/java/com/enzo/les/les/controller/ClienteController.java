package com.enzo.les.les.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.enzo.les.les.model.entities.Cliente;

@Controller
public class ClienteController {

    // Lista mockada para testar a tela de listagem
    private final List<Cliente> clientes = new ArrayList<>();

    // Tela de cadastro
    @GetMapping("/api/clientes/cadastro")
    public String cadastroForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente-cadastro";
    }

    // Salvar cliente (na lista mockada)
    @PostMapping("/api/clientes/salvar")
    public String salvar(@ModelAttribute Cliente cliente) {
        // Simula ID automático
        cliente.setId((long) (clientes.size() + 1));
        cliente.setAtivo(true); // cliente sempre começa ativo
        clientes.add(cliente);
        return "redirect:/clientes/lista";
    }

    // Listar clientes
    @GetMapping("/api/clientes/lista")
    public String listar(Model model) {
        model.addAttribute("clientes", clientes);
        return "cliente-lista";
    }

    // Editar cliente (simulação: apenas retorna o primeiro cliente encontrado)
    @GetMapping("/api/clientes/editar/{id}")
    public String editar(Model model) {
        // Para protótipo, retorna o primeiro cliente da lista
        if (!clientes.isEmpty()) {
            model.addAttribute("cliente", clientes.get(0));
        } else {
            model.addAttribute("cliente", new Cliente());
        }
        return "cliente-cadastro";
    }

    // Inativar/Ativar cliente
    @GetMapping("/api/clientes/inativar/{id}")
    public String inativar(Long id) {
        // Para protótipo: inverte status do primeiro cliente da lista
        if (!clientes.isEmpty()) {
            Cliente c = clientes.get(0);
            c.setAtivo(!c.isAtivo());
        }
        return "redirect:/clientes/lista";
    }

    // Excluir cliente
    @GetMapping("/api/clientes/delete/{id}")
    public String delete(Long id) {
        // Para protótipo: remove o primeiro cliente da lista
        if (!clientes.isEmpty()) {
            clientes.remove(0);
        }
        return "redirect:/clientes/lista";
    }
}
