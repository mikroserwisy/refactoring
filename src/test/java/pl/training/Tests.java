package pl.training;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Tests {

    private static final String TEXT_WITHOUT_EXPRESSIONS = "My name is Jan Kowalski";
    private static final String TEXT_WITH_EXPRESSIONS = "My name is ${firstName} ${lastName}";

    @Test
    void given_a_text_without_expression_when_evaluating_then_returns_the_text() {
        var template = new Template(TEXT_WITHOUT_EXPRESSIONS);
        assertEquals(TEXT_WITHOUT_EXPRESSIONS, template.evaluate(emptyMap()));
    }

    @Test
    void given_a_text_with_expressions_when_evaluating_then_returns_text_with_substituted_values() {
        var data = Map.of("firstName", "Jan", "lastName", "Kowalski");
        var template = new Template(TEXT_WITH_EXPRESSIONS);
        assertEquals("My name is Jan Kowalski", template.evaluate(data));
    }

    @Test
    void given_a_text_with_expressions_when_evaluating_without_providing_all_values_then_throws_an_exception() {
        var template = new Template(TEXT_WITH_EXPRESSIONS);
        assertThrows(IllegalArgumentException.class, () -> template.evaluate(emptyMap()));
    }

    @Test
    void given_a_text_with_expressions_when_evaluating_with_non_alphanumeric_values_then_throws_an_exception() {
        var data = Map.of("firstName", "Jan", "lastName", "@@");
        var template = new Template(TEXT_WITH_EXPRESSIONS);
        assertThrows(IllegalArgumentException.class, () -> template.evaluate(data));
    }

    @Test
    void  given_a_text_with_not_unique_expressions_when_evaluating_then_throws_an_exception() {
        assertThrows(IllegalArgumentException.class, () -> new Template("My name is ${firstName} ${firstName}"));
    }

}
