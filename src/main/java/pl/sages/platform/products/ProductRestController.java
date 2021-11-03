package pl.sages.platform.products;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sages.platform.accounts.Account;
import pl.sages.platform.accounts.AccountService;
import pl.sages.platform.common.ResourceUri;
import pl.sages.platform.common.exception.ExceptionResponseBuilder;
import pl.sages.platform.common.pagination.ResultPage;
import pl.sages.platform.common.pagination.ResultPageTransferObject;
import pl.sages.platform.projects.ProjectNotFoundException;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequestMapping("${platform.api-prefix}/products")
@RestController
@RequiredArgsConstructor
public class ProductRestController {

    private static final String PRODUCTS_PATH = "products";

    @NonNull
    private final ProductMapper productMapper;
    @NonNull
    private final ProductService productService;
    @NonNull
    private final ResourceUri resourceUri;
    private final AccountService accountService;
    @NonNull
    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createProduct(@RequestBody ProductTransferObject productTransferObject) {
        Product product = productMapper.toProduct(productTransferObject);
        productService.addProduct(product);
        URI productUri = resourceUri.from(PRODUCTS_PATH, product.getId());
        return ResponseEntity.created(productUri).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResultPageTransferObject<ProductTransferObject>> getProducts(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultPage<Product> productsPage = productService.getProducts(pageNumber, pageSize);
        ResultPageTransferObject<ProductTransferObject> resultPageTransferObject = productMapper.toProductTransferObjects(productsPage);
        return ResponseEntity.ok(resultPageTransferObject);
    }

    @RequestMapping(value = "{productId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateProduct(@RequestBody ProductTransferObject productTransferObject, @PathVariable("productId") Long productId) {
        Product product = productMapper.toProduct(productTransferObject);
        product.setId(productId);
        productService.updateProduct(product);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "{productId}/invitations", method = RequestMethod.POST)
    public ResponseEntity<Void> invite(@RequestBody InvitationTransferObject invitationTransferObject, @PathVariable("productId") Long productId) {
        productService.assignProduct(invitationTransferObject.getText(), invitationTransferObject.getEmails(), productId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "{productId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "active", method = RequestMethod.GET)
    public ResponseEntity<List<ProductTransferObject>> getUserProducts() {
        Account account = accountService.getActiveAccount();
        Set<Product> products = account.getProducts();
        if (products.isEmpty()) {
            productService.getDefaultProject().ifPresent(products::add);
        }
        List<ProductTransferObject> productTransferObjects = productMapper.toProductTransferObjects(products);
        return ResponseEntity.ok(productTransferObjects);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity onProductNotFoundException(ProductNotFoundException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity onProjectNotFoundException(ProjectNotFoundException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, NOT_FOUND, locale);
    }
}
