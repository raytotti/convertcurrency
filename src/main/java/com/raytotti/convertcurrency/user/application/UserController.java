package com.raytotti.convertcurrency.user.application;

import com.raytotti.convertcurrency.commun.application.ResponseCollection;
import com.raytotti.convertcurrency.user.domain.User;
import com.raytotti.convertcurrency.user.domain.UserRepository;
import com.raytotti.convertcurrency.user.exception.UserExistsException;
import com.raytotti.convertcurrency.user.exception.UserNotFoundException;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @ResponseBody
    public ResponseEntity<ResponseCollection<UserResponse>> findAll(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<UserResponse> users = repository.findAll(pageable)
                .map(UserResponse::from);

        return ResponseEntity.ok(ResponseCollection.from(users));

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {

        Optional<User> user = repository.findById(UUID.fromString(id));

        UserResponse userResponse = UserResponse.from(user.orElseThrow(UserNotFoundException::new));

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {

        if (repository.existsByCpf(request.getCpf())) {
            throw new UserExistsException();
        }

        User newUser = User.builder()
                .cpf(request.getCpf())
                .name(request.getName())
                .build();

        repository.save(newUser);

        UserResponse response = UserResponse.from(newUser);

        URI uri = fromCurrentRequest()
                .path("/")
                .path(response.getId().toString())
                .build().toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
