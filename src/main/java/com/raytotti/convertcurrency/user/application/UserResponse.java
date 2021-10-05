package com.raytotti.convertcurrency.user.application;

import com.raytotti.convertcurrency.user.domain.User;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserResponse {

    private UUID id;
    private String cpf;
    private String name;

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getCpf(), user.getName());
    }

}
