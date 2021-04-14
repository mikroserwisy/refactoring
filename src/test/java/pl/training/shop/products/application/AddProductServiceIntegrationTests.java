package pl.training.shop.products.application;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

import static pl.training.shop.commons.Tags.INTEGRATION;

@Tag(INTEGRATION)
@ExtendWith(ArquillianExtension.class)
public class AddProductServiceIntegrationTests {
}
