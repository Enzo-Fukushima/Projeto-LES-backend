package com.enzo.les.les.dtos;

import java.time.LocalDate;
import java.util.List;

import com.enzo.les.les.enums.TipoTelefoneEnum;
import com.enzo.les.les.model.entities.Cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class CreateClienteDTO {
    private Long id;

    @NotBlank(message = "Nome não pode ser vazio")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Nome deve conter apenas letras e espaços")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF não pode ser vazio")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
    private String cpf;

    @NotBlank(message = "Gênero não pode ser vazio")
    @Pattern(regexp = "^(MASCULINO|FEMININO|OUTRO)$", message = "Gênero deve ser MASCULINO, FEMININO ou OUTRO")
    private String genero;

    @NotNull(message = "Data de nascimento não pode ser nula")
    private LocalDate dataNascimento;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email deve ser válido")
    @Size(max = 255, message = "Email deve ter no máximo 255 caracteres")
    private String email;

    @NotBlank(message = "Senha não pode ser vazia")
    @Size(min = 8, max = 255, message = "Senha deve ter entre 8 e 255 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial")
    private String senha;

    @NotBlank(message = "Tipo de telefone não pode ser vazio")
    @Pattern(regexp = "^(RESIDENCIAL|CELULAR|COMERCIAL)$", message = "Tipo de telefone deve ser RESIDENCIAL, CELULAR ou COMERCIAL")
    private String tipoTelefone;

    @NotBlank(message = "DDD não pode ser vazio")
    @Pattern(regexp = "\\d{2}", message = "DDD deve ter exatamente 2 dígitos numéricos")
    private String ddd;

    @NotBlank(message = "Número de telefone não pode ser vazio")
    @Pattern(regexp = "\\d{8,9}", message = "Número de telefone deve ter entre 8 e 9 dígitos")
    private String numeroTelefone;

    private boolean ativo = true;
    private Integer ranking = 0;

    @Valid
    private List<CreateClienteEnderecoDTO> enderecos;


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

        return cliente;
    }
}