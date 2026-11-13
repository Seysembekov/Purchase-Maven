
package purchase.handler;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API error response")
public record ApiErrorResponse(
        @Schema(example = "VALIDATION")
        String code,
        @Schema(example = "title must not be blank")
        String message,
        @Schema(example = "request-12345")
        String correlationId) {
}
