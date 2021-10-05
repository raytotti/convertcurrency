package com.raytotti.convertcurrency.user.application;

import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateUserRequest {

    @NotBlank(message = "{User.cpf.NotBlank}")
    @Size(max = 14, min = 14, message = "{User.cpf.Size}")
    @CPF(message = "{User.cpf.Pattern}")
    private String cpf;

    @NotBlank(message = "{User.name.NotBlank}")
    @Size(min = 3, max = 256, message = "{User.name.Size}")
    private String name;

}
