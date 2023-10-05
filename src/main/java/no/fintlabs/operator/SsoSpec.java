package no.fintlabs.operator;

import lombok.Data;
import no.fintlabs.FlaisSpec;

@Data
public class SsoSpec implements FlaisSpec {
    private String basePath = "";
    private String hostname;
    private String loggingLevel = "info";
}