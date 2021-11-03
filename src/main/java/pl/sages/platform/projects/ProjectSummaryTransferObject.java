package pl.sages.platform.projects;

import lombok.Data;

@Data
public class ProjectSummaryTransferObject {

    private Long id;
    private String name;
    private String description;
    private long importTimestamp;

}
