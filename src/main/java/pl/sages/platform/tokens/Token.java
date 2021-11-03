package pl.sages.platform.tokens;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Table(indexes = {
        @Index(name = "token_value_index", columnList = "value", unique = true)
})
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Token {

    @GeneratedValue
    @Id
    private Long id;
    @Column(unique = true)
    @NonNull
    private String value;
    @Column(name = "owner_id", nullable = false)
    @NonNull
    private Long ownerId;

}
