package com.raytotti.convertcurrency.transaction.application;

import com.raytotti.convertcurrency.conversion.domain.Conversion;
import com.raytotti.convertcurrency.conversion.domain.IConversionRepository;
import com.raytotti.convertcurrency.transaction.domain.Transaction;
import com.raytotti.convertcurrency.transaction.domain.TransactionRepository;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(path = "/api/v1/transactions")
@AllArgsConstructor
@Slf4j
@Api(tags = "Transaction Currency Operations")
public class TransactionController {

    private final TransactionRepository repository;
    private final IConversionRepository conversionRepository;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid CreateTransactionRequest request) {

        BigDecimal conversionRate = conversionRepository.getRate(new Conversion(request.getOriginCurrency(), request.getDestinationCurrency()));

        Transaction newTransaction = Transaction.builder()
                .userId(request.getUserId())
                .originCurrency(request.getOriginCurrency())
                .originValue(request.getOriginValue())
                .destinationCurrency(request.getDestinationCurrency())
                .conversionRate(conversionRate)
                .build();

        repository.save(newTransaction);

        TransactionResponse response = TransactionResponse.from(newTransaction);

        URI uri = fromCurrentRequest()
                .path("/")
                .path(response.getId().toString())
                .build().toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TransactionResponse> findById(@PathVariable String id) {
        Optional<Transaction> transaction = repository.findById(UUID.fromString(id));
        TransactionResponse transactionResponse = TransactionResponse.from(transaction.orElseThrow());
        return ResponseEntity.ok(transactionResponse);
    }

}

