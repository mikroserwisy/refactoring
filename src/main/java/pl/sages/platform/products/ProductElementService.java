package pl.sages.platform.products;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sages.platform.common.configuration.TransactionalService;

@TransactionalService
@RequiredArgsConstructor
public class ProductElementService {

    @NonNull
    private final ProductElementRepository productElementRepository;

    public void isElementSaveToDelete(Long elementId) {
        if (productElementRepository.findByElementId(elementId).isPresent()) {
            throw new ElementInUseException();
        }
    }

    public void updateElementId(Long oldId, Long newId) {
        productElementRepository.updateElementId(oldId, newId);
    }

}
