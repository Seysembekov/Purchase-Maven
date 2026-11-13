package purchase.handler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import purchase.config.PurchaseService;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;
import purchase.domain.status.*;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/purchases")
public class PurchaseController {
    private final PurchaseService service;
    public PurchaseController(PurchaseService service) {
        this.service = service;
    }
    @PostMapping
    @Operation(summary = "Create a purchase")
    @ApiResponse(responseCode = "201", description = "Purchase created")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Duplicate business key")
    public ResponseEntity<PurchaseResponse> create(
            @Valid @RequestBody CreatePurchaseRequest request
    ) {
        Purchase purchase = new Purchase(
                PurchaseId.newId(),
                request.businessKey(),
                request.title(),
                1,
                BigDecimal.ZERO
        );

        service.register(purchase);

        PurchaseResponse response = toResponse(purchase);

        URI location = URI.create(
                "/api/v1/purchases/" + purchase.getId().value()
        );

        return ResponseEntity
                .created(location)
                .body(response);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a purchase by UUID")
    @ApiResponse(responseCode = "200", description = "Purchase found")
    @ApiResponse(responseCode = "400", description = "Invalid UUID")
    @ApiResponse(responseCode = "404", description = "Purchase not found")
    public ResponseEntity<PurchaseResponse> getById(
            @PathVariable String id
    ) {
        PurchaseId purchaseId;

        try {
            purchaseId = new PurchaseId(id);
        } catch (IllegalArgumentException ex) {
            throw new purchase.domain.InvalidPurchaseId();
        }

        Purchase purchase = service.getById(purchaseId);

        return ResponseEntity.ok(toResponse(purchase));
    }


    private PurchaseResponse toResponse(Purchase purchase) {
        return new PurchaseResponse(purchase.getId().value(), purchase.getBusinessKey(), statusToken(purchase.getStatus()), purchase.getProductName());
    }
    private String statusToken(PurchaseStatus status) {
        if (status instanceof Draft) return "DRAFT";
        if (status instanceof Submitted) return "SUBMITTED";
        if (status instanceof Approved) return "APPROVED";
        if (status instanceof Ordered) return "ORDERED";
        if (status instanceof Completed) return "COMPLETED";

        throw new IllegalArgumentException("Unknown purchase status");
    }
}
