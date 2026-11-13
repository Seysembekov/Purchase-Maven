
package purchase.handler;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Purchase information returned by the API")
public record PurchaseResponse(
        @Schema(description = "Purchase UUID")
        String id,
        @Schema(description = "Unique business key")
        String businessKey,
        @Schema(description = "Current purchase status", example = "DRAFT")
        String status,

        @Schema(description = "Purchase title")
        String title
) {
}
