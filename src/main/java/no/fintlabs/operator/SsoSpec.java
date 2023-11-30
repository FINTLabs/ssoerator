package no.fintlabs.operator;

import lombok.Data;
import no.fintlabs.FlaisSpec;

import java.util.Optional;

@Data
public class SsoSpec implements FlaisSpec {
    private String basePath = "";
    private String hostname;
    private String loggingLevel = "info";
    private String upnClaim = "email";

    public void setBasePath(String basePath) {
        while (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        this.basePath = basePath;
    }

}