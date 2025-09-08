package com.enzo.les.les.service;

import java.util.List;
import java.util.stream.Collectors;

import com.enzo.les.les.model.entities.Cliente;
import com.enzo.les.les.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.enzo.les.les.dtos.EnderecoDTO;
import com.enzo.les.les.model.entities.Endereco;
import com.enzo.les.les.repository.EnderecoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // CREATE
    public EnderecoDTO salvar(EnderecoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId()).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id" + dto.getClienteId()));
        Endereco endereco = dto.mapToEntity();
        cliente.addEndereco(endereco);
        clienteRepository.save(cliente);
        return endereco.mapToDTO();
    }

    public List<EnderecoDTO> buscarEnderecosDoUsuario(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return cliente.getEnderecos()
                .stream()
                .map(Endereco::mapToDTO)
                .toList();
    }

    // READ - buscar por id
    public EnderecoDTO buscarPorId(Long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id " + id));
        return endereco.mapToDTO();
    }

    // READ - listar todos
    public List<EnderecoDTO> listarTodos() {
        return enderecoRepository.findAll()
                .stream()
                .map(Endereco::mapToDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public EnderecoDTO atualizar(Long id, EnderecoDTO enderecoDTO) {
        Endereco enderecoExistente = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id " + id));

        enderecoExistente.update(enderecoDTO);
        enderecoRepository.save(enderecoExistente);
        return enderecoExistente.mapToDTO();
    }

    public void deletar(Long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id " + id));
        Cliente cliente = endereco.getCliente();
        if (cliente == null){
            throw new IllegalStateException("Endereço não está associado a nenhum cliente");
        }

        cliente.removeEndereco(endereco);
        clienteRepository.save(cliente);
    }
}
