package com.enzo.les.les.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.enzo.les.les.dtos.*;
import com.enzo.les.les.model.entities.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Cliente cliente = clienteDTO.mapToEntity();

        cliente.setSenha(cliente.getSenha());

        if (clienteDTO.getEnderecos() != null) {
            for (CreateClienteEnderecoDTO endDTO : clienteDTO.getEnderecos()) {
                Endereco endereco = endDTO.mapToEntity();
                cliente.addEndereco(endereco);
            }
        }

        Cliente salvo = clienteRepository.save(cliente);

        return salvo.mapToDTODetalhado();
    }

    // READ - buscar por id
    public ClienteDetalhadoDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id " + id));
        return cliente.mapToDTODetalhado();
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    // READ - listar todos
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(Cliente::mapToDTOSimples)
                .collect(Collectors.toList());
    }

    // UPDATE
    public ClienteUpdateDTO atualizar(Long id, ClienteUpdateDTO dto) {
        Cliente clienteExistente = clienteRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id " + id));

        clienteExistente.update(dto);
        clienteRepository.save(clienteExistente);
        return clienteExistente.mapToUpdateDTO();
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

    public ClienteDetalhadoDTO alterarSenha(Long id, ClienteUpdateSenhaDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));


        if (!dto.getSenhaAtual().equals(cliente.getSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }


        if (!dto.getNovaSenha().equals(dto.getConfirmaSenha())) {
            throw new IllegalArgumentException("Nova senha e confirmação não coincidem");
        }

        // 3️⃣ Atualiza a senha sem hash
        cliente.setSenha(dto.getNovaSenha());

        clienteRepository.save(cliente);
        return cliente.mapToDTODetalhado();
    }


}
