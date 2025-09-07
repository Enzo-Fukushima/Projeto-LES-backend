package com.enzo.les.les.service;

import java.util.List;
import java.util.stream.Collectors;

import com.enzo.les.les.dtos.ClienteDTO;
import com.enzo.les.les.dtos.CreateClienteDTO;
import com.enzo.les.les.dtos.CreateClienteEnderecoDTO;
import com.enzo.les.les.model.entities.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enzo.les.les.dtos.ClienteDetalhadoDTO;
import com.enzo.les.les.model.entities.Cliente;
import com.enzo.les.les.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // CREATE
    public ClienteDetalhadoDTO salvar(CreateClienteDTO clienteDTO) {
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        if (clienteRepository.existsByCpf(clienteDTO.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        // Converte DTO para Entity
        Cliente cliente = clienteDTO.mapToEntity();
        if(clienteDTO.getEnderecos() != null){
            for(CreateClienteEnderecoDTO endDTO : clienteDTO.getEnderecos()){
                Endereco endereco = endDTO.mapToEntity();
                cliente.addEndereco(endereco);
            }
        }
        Cliente salvo = clienteRepository.save(cliente);

        // Retorna DTO
        return salvo.mapToDTODetalhado();
    }

    // READ - buscar por id
    public ClienteDetalhadoDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id " + id));
        return cliente.mapToDTODetalhado();
    }

    // READ - listar todos
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(Cliente::mapToDTOSimples)
                .collect(Collectors.toList());
    }

    // UPDATE
    public ClienteDetalhadoDTO atualizar(Long id, ClienteDetalhadoDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(clienteDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id " + id));

        clienteExistente.update(clienteDTO);
        clienteRepository.save(clienteExistente);
        return clienteExistente.mapToDTODetalhado();
    }

    // DELETE lógico (inativar)
    public void inativar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id " + id));
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }

    public void ativarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        cliente.setAtivo(true);
        clienteRepository.save(cliente);
    }

    public ClienteDetalhadoDTO alterarSenha(Long id, ClienteDetalhadoDTO dto){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        cliente.updateSenha(dto);
        return cliente.mapToDTODetalhado();
    }
}
