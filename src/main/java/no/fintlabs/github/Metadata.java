
package no.fintlabs.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Generated;

@Data
public class Metadata {
    private Container container;
    @JsonProperty("package_type")
    private String packageType;
}
