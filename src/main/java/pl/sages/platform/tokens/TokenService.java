package pl.sages.platform.tokens;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sages.platform.common.UniqueValueGenerator;
import pl.sages.platform.common.configuration.TransactionalService;

@TransactionalService
@RequiredArgsConstructor
public class TokenService {

    @NonNull
    private final TokenRepository tokenRepository;
    @NonNull
    private final UniqueValueGenerator uniqueValueGenerator;

    public Token createToken(Long ownerId) {
        String tokenValue = uniqueValueGenerator.nextValue();
        Token token = new Token(tokenValue, ownerId);
        return tokenRepository.saveAndFlush(token);
    }

    public Token getToken(String tokenValue) {
        return tokenRepository.getByValue(tokenValue)
                .orElseThrow(TokenNotFoundException::new);
    }

    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }

}
