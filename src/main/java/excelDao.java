import lombok.Data;

@Data
public class excelDao {
    private String id;
    private String exampleId;
    private String bugTitle;
    private String content;
    private String bugImgUrl;
    private String version;
    private String designee;
    private String bugType;
    private String deadline;
    private String severity;
    private String priority;
    private Boolean upload;

    public String getBugImgUrl() {
        return bugImgUrl;
    }

    public void setBugImgUrl(String bugImgUrl) {
        this.bugImgUrl = bugImgUrl;
    }
}

