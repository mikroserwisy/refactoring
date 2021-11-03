package pl.sages.platform.accounts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sages.platform.common.TextSource;
import pl.sages.platform.common.configuration.TransactionalService;
import pl.sages.platform.emails.MailMessage;
import pl.sages.platform.emails.MailService;
import pl.sages.platform.products.Product;
import pl.sages.platform.tokens.Token;
import pl.sages.platform.tokens.TokenService;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@TransactionalService
@RequiredArgsConstructor
public class AccountService {

    private static final String ACTIVATION_EMAIL_TEMPLATE = "activation-email";
    private static final String RESET_PASSWORD_EMAIL_TEMPLATE = "reset-password-email";
    private static final String INVITATION_EMAIL_TEMPLATE = "invitation-email";
    private static final String SUBJECT_KEY = "email.subject";
    private static final String TOKEN_KEY = "token";
    private static final String ACCOUNT_ID_KEY = "accountId";
    private static final String INVITATION_TEXT_KEY = "invitation";

    @NonNull
    private final RoleService roleService;
    @NonNull
    private final TokenService tokenService;
    @NonNull
    private final MailService mailService;
    @NonNull
    private final AccountRepository accountRepository;
    @NonNull
    private final PasswordEncoder passwordEncoder;
    @NonNull
    private final TextSource textSource;
    @Value("${platform.admin-email}")
    @Setter
    private String adminEmail;
    @Value("${platform.admin-password}")
    @Setter
    private String adminPassword;

    @PostConstruct
    public void init() {
        if (accountRepository.getByEmail(adminEmail).isEmpty()) {
            Set<Role> roles = Set.of(roleService.getRole(Role.USER), roleService.getRole(Role.ADMIN));
            Account account = new Account();
            account.setRoles(roles);
            account.setEnabled(true);
            account.setEmail(adminEmail);
            account.setPassword(adminPassword);
            encodePassword(account);
            accountRepository.saveAndFlush(account);
        }
    }

    public void createAccount(Account account) {
        assignDefaultRoles(account);
        encodePassword(account);
        try {
            accountRepository.saveAndFlush(account);
            if (!account.isEnabled()) {
                sendConfirmationEmail(account, ACTIVATION_EMAIL_TEMPLATE);
            }
        } catch (ConstraintViolationException exception) {
            throw new InvalidEmailException();
        } catch (DataIntegrityViolationException exception) {
            throw new EmailAlreadyUsedException();
        }
    }

    private void assignDefaultRoles(Account account) {
        Set<Role> roles = Set.of(roleService.getRole(Role.USER));
        account.setRoles(roles);
    }

    private void encodePassword(Account account) {
        String password = account.getPassword();
        account.setPassword(passwordEncoder.encode(password));
    }

    private void sendConfirmationEmail(Account account, String templateName) {
        MailMessage confirmationEmail = prepareConfirmationEmail(account, templateName);
        mailService.send(confirmationEmail);
    }

    private MailMessage prepareConfirmationEmail(Account account, String templateName) {
        Token token = tokenService.createToken(account.getId());
        Map<String, Object> variables = Map.of(TOKEN_KEY, token.getValue(), ACCOUNT_ID_KEY, account.getId());
        String subject = textSource.getText(SUBJECT_KEY, account.getLanguage());
        String text = textSource.getTextFromTemplate(templateName, variables, account.getLanguage());
        return new MailMessage(account.getEmail(), subject, text);
    }

    public Account getAccount(Long id) {
        return accountRepository.getById(id)
                .orElseThrow(AccountNotFoundException::new);
    }

    public Optional<Account> getAccount(String email) {
        return accountRepository.getByEmail(email);
    }

    public Account getActiveAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        return accountRepository.getOne(account.getId());
    }

    public void resetAccountPassword(String email) {
        Account account = getAccount(email)
                .orElseThrow(AccountNotFoundException::new);
        sendConfirmationEmail(account, RESET_PASSWORD_EMAIL_TEMPLATE);
    }

    public void setAccountPassword(String tokenValue, String password) {
        updateAccount(account -> {
            account.setPassword(password);
            encodePassword(account);
        }, tokenValue);
    }

    public void activateAccount(String tokenValue) {
        updateAccount(account -> account.setEnabled(true), tokenValue);
    }

    private void updateAccount(Consumer<Account> updater, String tokenValue) {
        Token token = tokenService.getToken(tokenValue);
        Account account = getAccount(token.getOwnerId());
        updater.accept(account);
        accountRepository.saveAndFlush(account);
        tokenService.deleteToken(token);
    }

    public void sendInvitationEmail(Account account, String invitationText) {
        MailMessage invitationEmail = prepareInvitationEmail(account, INVITATION_EMAIL_TEMPLATE, invitationText);
        mailService.send(invitationEmail);
    }

    private MailMessage prepareInvitationEmail(Account account, String templateName, String invitationText) {
        Token token = tokenService.createToken(account.getId());
        Map<String, Object> variables = Map.of(TOKEN_KEY, token.getValue(), ACCOUNT_ID_KEY, account.getId(), INVITATION_TEXT_KEY, invitationText);
        String subject = textSource.getText(SUBJECT_KEY, account.getLanguage());
        String text = textSource.getTextFromTemplate(templateName, variables, account.getLanguage());
        return new MailMessage(account.getEmail(), subject, text);
    }

    public void update(Account account) {
        accountRepository.saveAndFlush(account);
    }

    public boolean accountsWithProductExists(Product product) {
        return accountRepository.countByProduct(product.getId()) > 0;
    }

}
