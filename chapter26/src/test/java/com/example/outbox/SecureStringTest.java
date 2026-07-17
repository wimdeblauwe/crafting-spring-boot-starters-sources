package com.example.outbox;

import tools.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecureStringTest {

    @Test
    void toStringDoesNotRevealTheValue() {
        SecureString secret = SecureString.of("super-secret");

        assertThat(secret.toString()).isEqualTo("<REDACTED>");
        assertThat("secret: " + secret).doesNotContain("super-secret");
    }

    @Test
    void valueAsStringRevealsTheValue() {
        assertThat(SecureString.of("super-secret").valueAsString()).isEqualTo("super-secret");
    }

    @Test
    void equalsComparesContent() {
        assertThat(SecureString.of("super-secret"))
                .isEqualTo(SecureString.of("super-secret"))
                .isNotEqualTo(SecureString.of("other"));
    }

    @Test
    void jacksonSerialisesTheRedactedForm() {
        String json = new JsonMapper().writeValueAsString(SecureString.of("super-secret"));

        assertThat(json).isEqualTo("\"<REDACTED>\"");
    }

    @Test
    void storedValueIsACopy() {
        char[] input = "super-secret".toCharArray();
        SecureString secret = new SecureString(input);
        input[0] = 'X';

        assertThat(secret.valueAsString()).isEqualTo("super-secret");
    }
}
