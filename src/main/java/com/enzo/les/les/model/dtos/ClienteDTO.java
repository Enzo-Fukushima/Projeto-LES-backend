package com.enzo.les.les.model.dtos;

import java.time.LocalDate;
import java.util.List;

import com.enzo.les.les.enums.TipoTelefoneEnum;
import com.enzo.les.les.model.entities.CartaoCredito;
import com.enzo.les.les.model.entities.Cliente;
import com.enzo.les.les.model.entities.Endereco;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;

    @NotBlank(message = "Nome não pode ser vazio")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Nome deve conter apenas letras e espaços")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF não pode ser vazio")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
    private String cpf;

    @NotBlank(message = "Gênero não pode ser vazio")
    private String genero;

    @NotNull(message = "Data de nascimento não pode ser nula")
    private LocalDate dataNascimento;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha não pode ser vazia")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial")
    private String senha;

    @NotBlank(message = "Tipo de telefone não pode ser vazio")
    private String tipoTelefone; // Idealmente ENUM

    @NotBlank(message = "DDD não pode ser vazio")
    @Pattern(regexp = "\\d{2,3}", message = "DDD deve ter 2 ou 3 dígitos numéricos")
    private String ddd;

    @NotBlank(message = "Número de telefone não pode ser vazio")
    @Pattern(regexp = "\\d{8,9}", message = "Número de telefone deve ter entre 8 e 9 dígitos")
    private String numeroTelefone;

    private boolean ativo = true;
    private Integer ranking = 0;

    // 🔹 Relacionamentos
    private List<Endereco> enderecos;
    private List<CartaoCredito> cartoes;

    // 🔹 Conversão para entidade Cliente
    public Cliente mapToEntity() {
        Cliente cliente = new Cliente();
        cliente.setId(this.id);
        cliente.setNome(this.nome);
        cliente.setCpf(this.cpf);
        cliente.setGenero(this.genero);
        cliente.setDataNascimento(this.dataNascimento);
        cliente.setEmail(this.email);
        cliente.setSenha(this.senha);
        cliente.setTipoTelefone(TipoTelefoneEnum.valueOf(this.tipoTelefone));
        cliente.setDdd(this.ddd);
        cliente.setNumeroTelefone(this.numeroTelefone);
        cliente.setAtivo(this.ativo);
        cliente.setRanking(this.ranking);

        // associar endereços ao cliente
        if (this.enderecos != null) {
            this.enderecos.forEach(e -> e.setCliente(cliente));
            cliente.setEnderecos(this.enderecos);
        }

        // associar cartões ao cliente
        if (this.cartoes != null) {
            this.cartoes.forEach(c -> c.setCliente(cliente));
            cliente.setCartoes(this.cartoes);
        }

        return cliente;
    }
}
