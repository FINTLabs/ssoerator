package no.fintlabs.operator;

import lombok.Data;
import no.fintlabs.FlaisSpec;

@Data
public class SsoSpec implements FlaisSpec {
    private String basePath = "";
    private String hostname;
    private String loggingLevel = "info";

    public void setBasePath(String basePath) {
        while (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        this.basePath = basePath;
    }

}