package com.enzo.les.les.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enzo.les.les.model.dtos.ClienteDTO;
import com.enzo.les.les.model.entities.Cliente;
import com.enzo.les.les.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // CREATE
    public ClienteDTO salvar(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        if (clienteRepository.existsByCpf(clienteDTO.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        // Converte DTO para Entity
        Cliente cliente = clienteDTO.mapToEntity();
        Cliente salvo = clienteRepository.save(cliente);

        // Retorna DTO
        return salvo.mapToDTO();
    }

    // READ - buscar por id
    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id " + id));
        return cliente.mapToDTO();
    }

    // READ - listar todos
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(Cliente::mapToDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public ClienteDTO atualizar(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id " + id));

        // Atualiza campos
        clienteExistente.setNome(clienteDTO.getNome());
        clienteExistente.setCpf(clienteDTO.getCpf());
        clienteExistente.setGenero(clienteDTO.getGenero());
        clienteExistente.setDataNascimento(clienteDTO.getDataNascimento());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setSenha(clienteDTO.getSenha());
        clienteExistente.setTipoTelefone(clienteDTO.getTipoTelefone());
        clienteExistente.setDdd(clienteDTO.getDdd());
        clienteExistente.setNumeroTelefone(clienteDTO.getNumeroTelefone());
        clienteExistente.setAtivo(clienteDTO.isAtivo());
        clienteExistente.setRanking(clienteDTO.getRanking());

        Cliente atualizado = clienteRepository.save(clienteExistente);
        return atualizado.mapToDTO();
    }

    // DELETE lógico (inativar)
    public void inativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id " + id));
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
}
