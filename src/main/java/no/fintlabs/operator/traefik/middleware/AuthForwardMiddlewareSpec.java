package no.fintlabs.operator.traefik.middleware;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthForwardMiddlewareSpec {
    private ForwardAuth forwardAuth;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForwardAuth {
        private String address;
        private List<String> authResponseHeaders = Collections.singletonList("Authorization");
        private boolean trustForwardHeader = true;
    }
}
