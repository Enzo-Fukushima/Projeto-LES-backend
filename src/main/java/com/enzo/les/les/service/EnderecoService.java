package com.enzo.les.les.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enzo.les.les.enums.TipoResidenciaEnum;
import com.enzo.les.les.enums.TipoLogradouroEnum;
import com.enzo.les.les.enums.EstadoEnum;
import com.enzo.les.les.model.dtos.EnderecoDTO;
import com.enzo.les.les.model.entities.Endereco;
import com.enzo.les.les.repository.EnderecoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    // CREATE
    public EnderecoDTO salvar(EnderecoDTO enderecoDTO) {
        Endereco endereco = enderecoDTO.mapToEntity();
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

        enderecoExistente.setTipoResidencia(TipoResidenciaEnum.valueOf(enderecoDTO.getTipoResidencia()));
        enderecoExistente.setTipoLogradouro(TipoLogradouroEnum.valueOf(enderecoDTO.getTipoLogradouro()));
        enderecoExistente.setLogradouro(enderecoDTO.getLogradouro());
        enderecoExistente.setNumero(enderecoDTO.getNumero());
        enderecoExistente.setBairro(enderecoDTO.getBairro());
        enderecoExistente.setCep(enderecoDTO.getCep());
        enderecoExistente.setCidade(enderecoDTO.getCidade());
        enderecoExistente.setEstado(EstadoEnum.valueOf(String.valueOf(enderecoDTO.getEstado())));
        enderecoExistente.setPais(enderecoDTO.getPais());
        enderecoExistente.setObservacoes(enderecoDTO.getObservacoes());

        Endereco atualizado = enderecoRepository.save(enderecoExistente);
        return atualizado.mapToDTO();
    }

    // DELETE lógico ou físico (aqui deixei físico, se quiser lógico é só usar um boolean "ativo")
    public void deletar(Long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id " + id));
        enderecoRepository.delete(endereco);
    }
}
