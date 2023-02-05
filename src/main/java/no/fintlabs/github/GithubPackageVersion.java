
package no.fintlabs.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GithubPackageVersion {

    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("html_url")
    private String htmlUrl;
    private Long id;
    private Metadata metadata;
    private String name;
    @JsonProperty("package_html_url")
    private String packageHtmlUrl;
    @JsonProperty("updated_at")
    private String updatedAt;
    private String url;
}
