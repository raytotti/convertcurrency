package com.raytotti.convertcurrency.transaction.application;

import com.raytotti.convertcurrency.commons.application.ResponseCollection;
import com.raytotti.convertcurrency.conversion.domain.Conversion;
import com.raytotti.convertcurrency.conversion.domain.IConversionService;
import com.raytotti.convertcurrency.transaction.domain.Transaction;
import com.raytotti.convertcurrency.transaction.domain.TransactionRepository;
import com.raytotti.convertcurrency.transaction.exception.TransactionNotFound;
import com.raytotti.convertcurrency.user.domain.UserRepository;
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
import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/transactions")
@Api(tags = "Transaction Currency Operations")
public class TransactionController {

    private final TransactionRepository repository;
    private final IConversionService conversionService;
    private final UserRepository userRepository;

    @PostMapping
    @Transactional
    @ApiOperation(value = "Create new Transaction Currency.", response = TransactionResponse.class, responseContainer = "Transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Operation completed successfully."),
            @ApiResponse(code = 400, message = "Failed to validate! Request Invalid."),
            @ApiResponse(code = 404, message = "Failed to validate! User or Tax Rate not found."),
            @ApiResponse(code = 415, message = "Unsupported Content Type."),
            @ApiResponse(code = 500, message = "Unexpected system failure.")
    })
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid CreateTransactionRequest request) {
        log.info("TransactionController -> create: Solicitado a criação de uma transação de moedas: {}", request);

        if (!userRepository.existsById(request.getUserId())) {
            log.error("TransactionController -> create: O usuário {} não existe.", request.getUserId());
            throw new UserNotFoundException();
        }

        BigDecimal conversionRate = conversionService.getRate(Conversion.of(request.getOriginCurrency(), request.getDestinationCurrency()));
        log.info("TransactionController -> create: Taxa de conversão que será utilizada: {}", conversionRate);

        Transaction newTransaction = repository.save(Transaction.of(request, conversionRate));
        log.info("TransactionController -> create: Transação com id {} criada.", newTransaction.getId());

        TransactionResponse response = TransactionResponse.from(newTransaction);

        log.info("TransactionController -> create: Transação respondida {}", response);
        URI uri = fromCurrentRequest()
                .path("/")
                .path(response.getId().toString())
                .build().toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Retrieve an Transaction Currency by Id.", response = TransactionResponse.class, responseContainer = "Transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully."),
            @ApiResponse(code = 400, message = "Failed to validate! Request Invalid."),
            @ApiResponse(code = 404, message = "Failed to validate! Transaction not found."),
            @ApiResponse(code = 500, message = "Unexpected system failure.")
    })
    public ResponseEntity<TransactionResponse> findById(@PathVariable UUID id) {
        log.info("TransactionController -> findById: Solicitado a busca de uma transação pelo id {}.", id);

        Optional<Transaction> transaction = repository.findById(id);
        TransactionResponse transactionResponse = TransactionResponse.from(transaction.orElseThrow(() -> {
            log.error("TransactionController -> findById: Transação com o id {} não encontrado.", id);
            throw new TransactionNotFound();
        }));

        log.info("TransactionController -> findById: Transação encontrada. {}", transactionResponse);
        return ResponseEntity.ok(transactionResponse);
    }

    @GetMapping(path = "/users/{userId}")
    @ApiOperation(value = "Retrieve all Transaction Currency by User.", response = TransactionResponse.class, responseContainer = "Transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operation completed successfully."),
            @ApiResponse(code = 400, message = "Failed to validate! Request Invalid."),
            @ApiResponse(code = 404, message = "Failed to validate! User not found."),
            @ApiResponse(code = 500, message = "Unexpected system failure.")
    })
    public ResponseEntity<ResponseCollection<TransactionResponse>> findByUserId(@PathVariable UUID userId,
                                                                                @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("TransactionController -> findByUserId: Solicitado a busca de todas as transações de um usuário {} com a paginação {}.", userId, pageable);

        if (!userRepository.existsById(userId)) {
            log.error("TransactionController -> findByUserId: O usuário {} não existe.", userId);
            throw new UserNotFoundException();
        }

        Page<TransactionResponse> transactions = repository.findByUserId(userId, pageable)
                .map(TransactionResponse::from);
        log.info("TransactionController -> findByUserId: Transações encontradas" + transactions.getContent());

        return ResponseEntity.ok(ResponseCollection.from(transactions));
    }

}

