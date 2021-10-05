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
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid CreateTransactionRequest request) {

        if (!userRepository.existsById(request.getUserId())) {
            throw new UserNotFoundException();
        }

        BigDecimal conversionRate = conversionService.getRate(Conversion.of(request.getOriginCurrency(), request.getDestinationCurrency()));

        Transaction newTransaction = repository.save(Transaction.of(request, conversionRate));

        TransactionResponse response = TransactionResponse.from(newTransaction);

        URI uri = fromCurrentRequest()
                .path("/")
                .path(response.getId().toString())
                .build().toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TransactionResponse> findById(@PathVariable UUID id) {

        Optional<Transaction> transaction = repository.findById(id);

        TransactionResponse transactionResponse = TransactionResponse.from(transaction.orElseThrow(TransactionNotFound::new));

        return ResponseEntity.ok(transactionResponse);
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<ResponseCollection<TransactionResponse>> findByUserId(@PathVariable UUID userId,
                                                                                @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        Page<TransactionResponse> transactions = repository.findByUserId(userId, pageable)
                .map(TransactionResponse::from);

        return ResponseEntity.ok(ResponseCollection.from(transactions));
    }

}

