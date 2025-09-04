package com.enzo.les.les.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

import com.enzo.les.les.model.dtos.ClienteDTO;

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
    private String tipoTelefone;

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
        dto.setTipoTelefone(this.getTipoTelefone());
        dto.setDdd(this.getDdd());
        dto.setNumeroTelefone(this.getNumeroTelefone());
        dto.setAtivo(this.isAtivo());
        dto.setRanking(this.getRanking());
        return dto;
    }
}
