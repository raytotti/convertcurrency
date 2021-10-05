package com.raytotti.convertcurrency.user.application;

import com.raytotti.convertcurrency.commons.application.ResponseCollection;
import com.raytotti.convertcurrency.user.domain.User;
import com.raytotti.convertcurrency.user.domain.UserRepository;
import com.raytotti.convertcurrency.user.exception.UserExistsException;
import com.raytotti.convertcurrency.user.exception.UserNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "User Operations")
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserRepository repository;

    @PostMapping
    @Transactional
    @ApiOperation(value = "Create new User.", response = UserResponse.class, responseContainer = "User")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation completed successfully."),
            @ApiResponse(code = 400, message = "Failed to validate! Request Invalid."),
            @ApiResponse(code = 409, message = "Failed to validate! User already exists."),
            @ApiResponse(code = 415, message = "Unsupported Content Type."),
            @ApiResponse(code = 500, message = "Unexpected system failure.")
    })
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        log.info("UserController -> create: Solicitado a criação de um usuário: {}", request);

        if (repository.existsByCpf(request.getCpf())) {
            log.error("UserController -> create: Já existe um usuário para o CPF: {}", request.getCpf());
            throw new UserExistsException();
        }

        User newUser = User.builder()
                .cpf(request.getCpf())
                .name(request.getName())
                .build();
        repository.save(newUser);
        log.info("UserController -> create: Usuário com id {} criado.", newUser.getId());

        UserResponse response = UserResponse.from(newUser);
        log.info("UserController -> create: Usuário respondido {}", response);

        URI uri = fromCurrentRequest()
                .path("/")
                .path(response.getId().toString())
                .build().toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Retrieve an User by Id.", response = UserResponse.class, responseContainer = "User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully."),
            @ApiResponse(code = 400, message = "Failed to validate! Request Invalid."),
            @ApiResponse(code = 404, message = "Failed to validate! User not found."),
            @ApiResponse(code = 500, message = "Unexpected system failure.")
    })
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        log.info("UserController -> findById: Solicitado a busca de um usuário pelo id {}.", id);
        Optional<User> user = repository.findById(id);

        UserResponse userResponse = UserResponse.from(user.orElseThrow(() -> {
            log.error("UserController -> findById: Usuário com o id {} não encontrado.", id);
            throw new UserNotFoundException();
        }));

        log.info("UserController -> findById: Usuário encontrado. {}", userResponse);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    @ResponseBody
    @ApiOperation(value = "Retrieve all Users.", response = UserResponse.class, responseContainer = "User")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully."),
            @ApiResponse(code = 400, message = "Failed to validate! Request Invalid."),
            @ApiResponse(code = 500, message = "Unexpected system failure.")
    })
    public ResponseEntity<ResponseCollection<UserResponse>> findAll(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("UserController -> findAll: Solicitado a busca de todos usuário com a paginação {}.", pageable);

        Page<UserResponse> users = repository.findAll(pageable)
                .map(UserResponse::from);

        log.info("UserController -> findAll: Usuários encontrados: {}", users.getContent());
        return ResponseEntity.ok(ResponseCollection.from(users));

    }

}
