package pl.sages.platform.accounts;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sages.platform.security.SecurityService;
import pl.sages.platform.common.exception.ExceptionResponseBuilder;
import pl.sages.platform.common.exception.ExceptionTransferObject;
import pl.sages.platform.tokens.TokenNotFoundException;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static pl.sages.platform.security.OAuthConfiguration.AuthorizationServer;

@RequestMapping("${platform.api-prefix}/accounts")
@RestController
@RequiredArgsConstructor
public class AccountRestController {

    @NonNull
    private final AccountService accountService;
    @NonNull
    private final SecurityService securityService;
    @NonNull
    private final AccountMapper accountMapper;
    @NonNull
    private final ExceptionResponseBuilder exceptionResponseBuilder;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createAccount(@RequestBody AccountTransferObject accountTransferObject, Locale locale) {
        Account account = accountMapper.toAccount(accountTransferObject);
        account.setLanguage(locale.getLanguage());
        accountService.createAccount(account);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "active", method = RequestMethod.GET)
    public ResponseEntity<AccountTransferObject> getActiveAccount() {
         Account account = accountService.getActiveAccount();
         return ResponseEntity.ok(accountMapper.toAccountTransferObject(account));
    }

    @RequestMapping(value = "active", method = RequestMethod.DELETE)
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String tokenValue = authorizationHeader.replace(AuthorizationServer.TOKEN_TYPE, "").trim();
        securityService.logout(tokenValue);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "blocked", method = RequestMethod.POST)
    public ResponseEntity<Void> resetAccountPassword(@RequestBody AccountTransferObject accountTransferObject) {
        accountService.resetAccountPassword(accountTransferObject.getEmail());
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "{accountId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateAccount(@RequestBody AccountTransferObject accountTransferObject, @RequestParam(name = "token") String tokenValue) {
        if (accountTransferObject.getPassword() != null) {
            accountService.setAccountPassword(tokenValue, accountTransferObject.getPassword());
        } else {
            accountService.activateAccount(tokenValue);
        }
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ExceptionTransferObject> onAccountNotFoundException(AccountNotFoundException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, NOT_FOUND, locale);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ExceptionTransferObject> onEmailAlreadyUsedException(EmailAlreadyUsedException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, BAD_REQUEST, locale);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ExceptionTransferObject> onInvalidEmailException(InvalidEmailException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, BAD_REQUEST, locale);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ExceptionTransferObject> onTokenNotFoundException(TokenNotFoundException exception, Locale locale) {
        return exceptionResponseBuilder.buildResponse(exception, NOT_FOUND, locale);
    }

}
