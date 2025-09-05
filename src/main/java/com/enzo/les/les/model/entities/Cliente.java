package com.enzo.les.les.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.enzo.les.les.enums.TipoTelefoneEnum;
import com.enzo.les.les.model.dtos.ClienteDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RN0026
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private String genero;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTelefoneEnum tipoTelefone;

    @Column(nullable = false, length = 3)
    private String ddd;

    @Column(nullable = false, length = 9)
    private String numeroTelefone;


    private boolean ativo = true;

    private Integer ranking = 0;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartaoCredito> cartoes;

    public ClienteDTO mapToDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(this.getId());
        dto.setNome(this.getNome());
        dto.setCpf(this.getCpf());
        dto.setGenero(this.getGenero());
        dto.setDataNascimento(this.getDataNascimento());
        dto.setEmail(this.getEmail());
        dto.setSenha(this.getSenha());
        dto.setTipoTelefone(String.valueOf(this.getTipoTelefone()));
        dto.setDdd(this.getDdd());
        dto.setNumeroTelefone(this.getNumeroTelefone());
        dto.setAtivo(this.isAtivo());
        dto.setRanking(this.getRanking());
        return dto;
    }


    public void updateFromDTO(ClienteDTO dto) {
        this.setNome(dto.getNome());
        this.setCpf(dto.getCpf());
        this.setGenero(dto.getGenero());
        this.setDataNascimento(dto.getDataNascimento());
        this.setEmail(dto.getEmail());
        this.setSenha(dto.getSenha());
        this.setTipoTelefone(TipoTelefoneEnum.valueOf(dto.getTipoTelefone()));
        this.setDdd(dto.getDdd());
        this.setNumeroTelefone(dto.getNumeroTelefone());
        this.setAtivo(dto.isAtivo());
        this.setRanking(dto.getRanking());

        // atualizar endereços
        if (dto.getEnderecos() != null) {
            dto.getEnderecos().forEach(e -> e.setCliente(this));
            this.setEnderecos(dto.getEnderecos());
        }

        // atualizar cartões
        if (dto.getCartoes() != null) {
            dto.getCartoes().forEach(c -> c.setCliente(this));
            this.setCartoes(dto.getCartoes());
        }
    }
}
