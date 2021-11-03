package pl.sages.platform.common.pagination;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class ResultPage<T> {

    @NonNull
    private List<T> data;
    private int pageNumber;
    private int totalPages;

}
