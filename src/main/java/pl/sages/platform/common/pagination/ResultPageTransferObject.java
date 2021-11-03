package pl.sages.platform.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultPageTransferObject<T> {

    private List<T> data;
    private int pageNumber;
    private int totalPages;

}
