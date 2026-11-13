
package purchase.handler;


import org.springframework.test.context.ContextConfiguration;
import purchase.config.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import purchase.config.PurchaseService;
import purchase.domain.DuplicatePurchase;
import purchase.domain.Purchase;
import purchase.domain.PurchaseId;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseController.class)
@Import(PurchaseExceptionHandler.class)
@ContextConfiguration(classes = Application.class)
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PurchaseService service;

    @Test
    void createPurchaseReturns201() throws Exception {

        mockMvc.perform(post("/api/v1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "businessKey": "PR-001",
                                  "title": "Laptop"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.businessKey").value("PR-001"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.title").value("Laptop"));

        verify(service).register(any(Purchase.class));
    }


    @Test
    void missingPurchaseReturns404() throws Exception {

        String id = UUID.randomUUID().toString();

        when(service.getById(any(PurchaseId.class)))
                .thenThrow(
                        new purchase.domain.PurchaseNotFound(
                                new PurchaseId(id)
                        )
                );

        mockMvc.perform(get("/api/v1/purchases/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("purchase not found"));
    }

    @Test
    void blankTitleReturns400() throws Exception {

        mockMvc.perform(post("/api/v1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Correlation-Id", "test-123")
                        .content("""
                                {
                                  "businessKey": "PR-002",
                                  "title": "   "
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION"))
                .andExpect(jsonPath("$.message").value(
                        org.hamcrest.Matchers.containsString("title")
                ))
                .andExpect(jsonPath("$.correlationId").value("test-123"));

        verify(service, never()).register(any(Purchase.class));
    }

    @Test
    void getPurchaseReturns200() throws Exception {

        String id = UUID.randomUUID().toString();

        Purchase purchase = new Purchase(
                new PurchaseId(id),
                "PR-003",
                "Monitor",
                1,
                BigDecimal.ZERO
        );

        when(service.getById(any(PurchaseId.class)))
                .thenReturn(purchase);

        mockMvc.perform(get("/api/v1/purchases/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.businessKey").value("PR-003"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.title").value("Monitor"));
    }

    @Test
    void invalidUuidReturns400() throws Exception {

        mockMvc.perform(get("/api/v1/purchases/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION"))
                .andExpect(jsonPath("$.message").value("id must be a UUID"));
    }

    @Test
    void duplicatePurchaseReturns409() throws Exception {

        doThrow(new DuplicatePurchase("PR-004", null))
                .when(service).register(any(Purchase.class));

        mockMvc.perform(post("/api/v1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "businessKey": "PR-004",
                                  "title": "Keyboard"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DUPLICATE"));
    }
}
