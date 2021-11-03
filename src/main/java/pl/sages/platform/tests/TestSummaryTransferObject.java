package pl.sages.platform.tests;

import lombok.Data;

@Data
public class TestSummaryTransferObject {

    private Long id;
    private String name;
    private String description;
    private long importTimestamp;

}
