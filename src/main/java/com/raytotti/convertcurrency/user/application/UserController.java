package com.raytotti.convertcurrency.user.application;

import com.raytotti.convertcurrency.commun.application.ResponseCollection;
import com.raytotti.convertcurrency.user.domain.User;
import com.raytotti.convertcurrency.user.domain.UserRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(path = "/api/v1/users")
@AllArgsConstructor
@Slf4j
@Api(tags = "User Operations")
public class UserController {

    private final UserRepository repository;

    @GetMapping
    @ResponseBody
    @ApiOperation("Retrieve all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Operation")
    })
    public ResponseEntity<ResponseCollection<UserResponse>> findAll(@PageableDefault(sort = "name", direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pageable) {
        Page<UserResponse> users = repository.findAll(pageable)
                .map(UserResponse::from);
        return ResponseEntity.ok(ResponseCollection.from(users));

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        Optional<User> user = repository.findById(UUID.fromString(id));
        UserResponse userResponse = UserResponse.from(user.orElseThrow());
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {

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
