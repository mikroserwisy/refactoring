package pl.sages.platform.products;

import lombok.Data;

import java.util.Set;

@Data
public class InvitationTransferObject {

    private String text;
    private Set<String> emails;

}
