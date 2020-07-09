import lombok.Data;

@Data
public class Config {
    private String url;
    private String name;
    private String password;

    private String loginUrl;
    private String testUrl;
    private String myUrl;
    private String productUrl;
    private String projectUrl;
}
