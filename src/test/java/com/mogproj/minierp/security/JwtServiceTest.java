package com.mogproj.minierp.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    @Test
    void validateConfiguration_whenSecretMissing_shouldFailFast() {
        JwtService service = new JwtService();
        ReflectionTestUtils.setField(service, "secret", "");

        assertThatThrownBy(service::validateConfiguration)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("app.security.jwt.secret");
    }

    @Test
    void validateConfiguration_whenSecretTooShort_shouldFailFast() {
        JwtService service = new JwtService();
        ReflectionTestUtils.setField(service, "secret", "dG9vLXNob3J0");

        assertThatThrownBy(service::validateConfiguration)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("256 bits");
    }

    @Test
    void validateConfiguration_whenSecretIsValid_shouldPass() {
        JwtService service = new JwtService();
        ReflectionTestUtils.setField(service, "secret",
                "dGVzdC1vbmx5LXNlY3JldC1rZXktZm9yLXVuaXQtdGVzdHMtbWluaW1hbC0yNTZiaXQ=");

        assertThatNoException().isThrownBy(service::validateConfiguration);
    }
}
