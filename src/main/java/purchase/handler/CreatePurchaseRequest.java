
package purchase.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data required to create a purchase")
public record CreatePurchaseRequest(
        @NotBlank(message = "must not be blank")
        @Schema(description = "Unique business key", example = "PR-2026-001", requiredMode = Schema.RequiredMode.REQUIRED)
        String businessKey,

        @NotBlank(message = "must not be blank")
        @Size(max = 120)
        @Schema(description = "Purchase title", example = "Laptop", maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
        String title
) {
}
