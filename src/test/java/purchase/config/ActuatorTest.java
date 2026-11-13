
package purchase.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ActuatorTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    void healthEndpointReturnsUp() throws Exception {

        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
    @Test
    void infoEndpointIsAvailable() throws Exception {

        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk());
    }

    @Test
    void environmentEndpointIsNotExposed() throws Exception {

        mockMvc.perform(get("/actuator/env"))
                .andExpect(status().isNotFound());
    }
    @Test
    void heapdumpEndpointIsNotExposed() throws Exception {

        mockMvc.perform(get("/actuator/heapdump"))
                .andExpect(status().isNotFound());
    }
}
