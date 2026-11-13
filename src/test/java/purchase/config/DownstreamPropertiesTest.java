
package purchase.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("dev")
class DownstreamPropertiesTest {

    @Autowired
    private DownstreamProperties properties;

    @Test
    void devProfileLoadsDownstreamProperties() {

        assertNotNull(properties);

        assertEquals(
                "http://localhost:9090",
                properties.baseUrl().toString()
        );

        assertEquals(
                Duration.ofSeconds(2),
                properties.timeout()
        );
    }
}
