package com.enzo.les.les.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.enzo.les.les.enums.TipoTelefoneEnum;
import com.enzo.les.les.dtos.ClienteDTO;
import com.enzo.les.les.dtos.EnderecoDTO;
import com.enzo.les.les.dtos.CartaoCreditoDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, length = 20)
    private String genero;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(name = "tipo_telefone", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTelefoneEnum tipoTelefone;

    @Column(nullable = false, length = 2)
    private String ddd;

    @Column(name = "numero_telefone", nullable = false, length = 9)
    private String numeroTelefone;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false)
    private Integer ranking = 0;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Endereco> enderecos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartaoCredito> cartoes = new ArrayList<>();

    public ClienteDTO mapToDTO() {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(this.id);
        dto.setNome(this.nome);
        dto.setCpf(this.cpf);
        dto.setGenero(this.genero);
        dto.setDataNascimento(this.dataNascimento);
        dto.setEmail(this.email);
        dto.setSenha(this.senha);
        dto.setTipoTelefone(this.tipoTelefone.name());
        dto.setDdd(this.ddd);
        dto.setNumeroTelefone(this.numeroTelefone);
        dto.setAtivo(this.ativo);
        dto.setRanking(this.ranking);

        if (this.enderecos != null && !this.enderecos.isEmpty()) {
            List<EnderecoDTO> enderecoDTOs = this.enderecos.stream()
                    .map(Endereco::mapToDTO)
                    .toList();
            dto.setEnderecos(enderecoDTOs);
        }

        if (this.cartoes != null && !this.cartoes.isEmpty()) {
            List<CartaoCreditoDTO> cartaoDTOs = this.cartoes.stream()
                    .map(CartaoCredito::mapToDTO)
                    .toList();
            dto.setCartoes(cartaoDTOs);
        }

        return dto;
    }

    public void update(ClienteDTO dto) {
        this.nome = dto.getNome();
        this.cpf = dto.getCpf();
        this.genero = dto.getGenero();
        this.dataNascimento = dto.getDataNascimento();
        this.email = dto.getEmail();
        this.senha = dto.getSenha();
        this.tipoTelefone = TipoTelefoneEnum.valueOf(dto.getTipoTelefone());
        this.ddd = dto.getDdd();
        this.numeroTelefone = dto.getNumeroTelefone();
        this.ativo = dto.isAtivo();
        this.ranking = dto.getRanking();
    }

    public void updateSenha(ClienteDTO dto){
        this.senha = dto.getSenha();
    }
    public void addEndereco(Endereco endereco) {
        if (this.enderecos == null) {
            this.enderecos = new ArrayList<>();
        }
        this.enderecos.add(endereco);
        endereco.setCliente(this);
    }

    public void removeEndereco(Endereco endereco) {
        if (this.enderecos != null) {
            this.enderecos.remove(endereco);
            endereco.setCliente(null);
        }
    }

    public void addCartaoCredito(CartaoCredito cartao) {
        if (this.cartoes == null) {
            this.cartoes = new ArrayList<>();
        }
        this.cartoes.add(cartao);
        cartao.setCliente(this);
    }

    public void removeCartaoCredito(CartaoCredito cartao) {
        if (this.cartoes != null) {
            this.cartoes.remove(cartao);
            cartao.setCliente(null);
        }
    }
}