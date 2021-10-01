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

    @Valid
    @NotBlank(message = "{User.name.notBlank}")
    @Size(min = 3, max = 256, message = "{User.name.size}")
    private String name;

    @Valid
    @NonNull
    @Size(max = 14, min = 14, message = "{User.cpf.Size}")
    @CPF(message = "{User.cpf.Pattern}}")
    private String cpf;

}
