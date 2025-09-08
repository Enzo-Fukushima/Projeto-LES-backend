package com.enzo.les.les.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;
@Data
public class ClienteUpdateSenhaDTO {
    @NotBlank(message = "Senha atual não pode ser vazia")
    private String senhaAtual;

    @NotBlank(message = "Nova senha não pode ser vazia")
    @Size(min = 8, max = 255, message = "Senha deve ter entre 8 e 255 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "Senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial")
    private String novaSenha;

    @NotBlank(message = "Confirmação da senha não pode ser vazia")
    private String confirmaSenha;


}
