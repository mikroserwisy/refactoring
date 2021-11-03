package pl.sages.platform.emails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessage {

    @NonNull
    private String recipient;
    @NonNull
    private String subject;
    @NonNull
    private String text;

}
