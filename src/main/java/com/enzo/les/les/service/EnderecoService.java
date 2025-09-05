package com.enzo.les.les.service;

import java.util.List;
import java.util.stream.Collectors;

import com.enzo.les.les.model.entities.Cliente;
import com.enzo.les.les.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.enzo.les.les.model.dtos.EnderecoDTO;
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
    public EnderecoDTO salvar(EnderecoDTO enderecoDTO) {
        Endereco endereco = enderecoDTO.mapToEntity();
        Cliente cliente = clienteRepository.findById(enderecoDTO.getClienteId()).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id" + enderecoDTO.getClienteId()));
        endereco.setCliente(cliente);
        Endereco salvo = enderecoRepository.save(endereco);
        return salvo.mapToDTO();
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

    // DELETE lógico ou físico (aqui deixei físico, se quiser lógico é só usar um boolean "ativo")
    public void deletar(Long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id " + id));
        enderecoRepository.delete(endereco);
    }
}
