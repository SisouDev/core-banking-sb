package com.banking.core_banking.domain.controller.card;

import com.banking.core_banking.domain.model.dto.card.request.CardCreateRequest;
import com.banking.core_banking.domain.model.dto.card.response.CardResponse;
import com.banking.core_banking.domain.model.dto.product.response.LoanDetailsResponse;
import com.banking.core_banking.domain.model.entities.user.User;
import com.banking.core_banking.domain.service.card.CardService;
import com.banking.core_banking.domain.service.product.LoanService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardResponse> createCard(
            @AuthenticationPrincipal User loggedInUser,
            @Valid @RequestBody CardCreateRequest request
    ) {
        CardResponse response = cardService.createCard(loggedInUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<CardResponse>> getAllCards(
            @AuthenticationPrincipal User loggedInUser,
            Pageable pageable
    ) {
        Page<CardResponse> response = cardService.getAllCardsForUser(loggedInUser, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponse> getCardDetails(
            @AuthenticationPrincipal User loggedInUser,
            @PathVariable Long cardId
    ) {
        CardResponse response = cardService.getCardDetails(loggedInUser, cardId);
        return ResponseEntity.ok(response);
    }
}