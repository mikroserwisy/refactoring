package pl.sages.platform.products;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.sages.platform.accounts.Account;
import pl.sages.platform.accounts.AccountService;
import pl.sages.platform.common.UniqueValueGenerator;
import pl.sages.platform.common.configuration.TransactionalService;
import pl.sages.platform.common.pagination.ResultPage;
import pl.sages.platform.projects.ProjectService;

import java.util.Optional;
import java.util.Set;

@TransactionalService
@RequiredArgsConstructor
public class ProductService {

    @NonNull
    private final ProductRepository productRepository;
    @NonNull
    private final AccountService accountService;
    @NonNull
    private final ProjectService projectService;
    @NonNull
    private final UniqueValueGenerator uniqueValueGenerator;
    @Value("${platform.default-language}")
    @Setter
    private String defaultLanguage;

    public Product addProduct(Product product) {
        return productRepository.saveAndFlush(product);
    }

    public ResultPage<Product> getProducts(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Product> productsPage = productRepository.findAll(pageRequest);
        return new ResultPage<>(productsPage.getContent(), pageNumber, productsPage.getTotalPages());
    }

    public void updateProduct(Product product) {
        getProduct(product.getId());
        product.getElements().forEach(this::validateProductElement);
        productRepository.saveAndFlush(product);
    }

    public Product getProduct(Long productId) {
        return productRepository.getById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    private void validateProductElement(ProductElement productElement) {
        if (productElement.getType().equals(ProductElementType.PROJECT)) {
            projectService.getProject(productElement.getElementId());
        }
    }

    public void deleteProduct(Long projectId) {
        Product product = getProduct(projectId);
        if (accountService.accountsWithProductExists(product)) {
            throw new ElementInUseException();
        }
        productRepository.delete(product);
    }

    public void assignProduct(String invitationText, Set<String> emails, Long productId) {
        Product product = productRepository.getById(productId)
            .orElseThrow(ProductNotFoundException::new);
        emails.forEach(email -> assignProduct(email, invitationText, product));
    }

    private void assignProduct(String email, String invitationText, Product product) {
        Optional<Account> account = accountService.getAccount(email);
        if (account.isEmpty()) {
            Account newAccount = createAccount(email, product);
            accountService.sendInvitationEmail(newAccount, invitationText);
        } else {
            replaceProduct(product, account.get());
        }
    }

    private Account createAccount(String email, Product product) {
        Account newAccount = new Account(email, uniqueValueGenerator.nextValue());
        newAccount.setEnabled(true);
        newAccount.setProducts(Set.of(product));
        newAccount.setLanguage(defaultLanguage);
        accountService.createAccount(newAccount);
        return newAccount;
    }

    private void replaceProduct(Product product,Account account) {
        account.getProducts().clear();
        account.getProducts().add(product);
        accountService.update(account);
    }

    public Optional<Product> getDefaultProject() {
        return productRepository.getFirstBy();
    }

}
