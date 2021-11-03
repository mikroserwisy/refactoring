package pl.sages.platform.projects;

import lombok.Data;

@Data
public class ProjectFileTransferObject {

    private Long id;
    private String name;
    private String path;
    private boolean editable;
    private String content;

}
