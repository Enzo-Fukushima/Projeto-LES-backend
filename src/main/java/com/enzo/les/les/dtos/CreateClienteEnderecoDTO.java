package com.enzo.les.les.dtos;
import com.enzo.les.les.enums.EstadoEnum;
import com.enzo.les.les.model.entities.Endereco;
import com.enzo.les.les.enums.TipoLogradouroEnum;
import com.enzo.les.les.enums.TipoResidenciaEnum;
import com.enzo.les.les.enums.TipoEnderecoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateClienteEnderecoDTO {
    private Long id;

    @NotBlank(message = "Apelido não pode ser vazio")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Apelido deve conter apenas letras e espaços")
    private String apelido;

    @NotBlank(message = "Tipo de residência não pode ser vazio")
    private TipoResidenciaEnum tipoResidencia;

    @NotBlank(message = "Tipo de logradouro não pode ser vazio")
    private TipoLogradouroEnum tipoLogradouro;

    private TipoEnderecoEnum tipoEndereco;

    @NotBlank(message = "Logradouro não pode ser vazio")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Logradouro deve conter apenas letras e espaços")
    private String logradouro;

    @NotNull(message = "Número não pode ser vazio")
    private Integer numero;

    @NotBlank(message = "Bairro não pode ser vazio")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Bairro deve conter apenas letras e espaços")
    private String bairro;

    @NotBlank(message = "CEP não pode ser vazio")
    private String cep;

    @NotBlank(message = "Cidade não pode ser vazio")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Cidade deve conter apenas letras e espaços")
    private String cidade;

    private EstadoEnum estado;

    @NotBlank(message = "País não pode ser vazio")
    private String pais;

    @NotBlank(message = "Observações não pode ser vazio")
    private String observacoes;


    // 🔹 Conversão para entidade Endereco
    public Endereco mapToEntity() {
        Endereco endereco = new Endereco();
        endereco.setId(this.id);
        endereco.setApelido(this.apelido);
        endereco.setTipoResidencia(this.tipoResidencia);
        endereco.setTipoLogradouro(this.tipoLogradouro);
        endereco.setTipoEndereco(this.tipoEndereco);
        endereco.setLogradouro(this.logradouro);
        endereco.setNumero(this.numero);
        endereco.setBairro(this.bairro);
        endereco.setCep(this.cep);
        endereco.setCidade(this.cidade);
        endereco.setEstado(this.estado);
        endereco.setPais(this.pais);

        return endereco;
    }
}
