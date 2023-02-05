package no.fintlabs.operator.traefik.middleware;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class AuthForwardMiddlewareSpec {
    private ForwardAuth forwardAuth;

    @Data
    public static class ForwardAuth {
        private String address;
        private List<String> authResponseHeaders = Collections.singletonList("Authorization");
        private boolean trustForwardHeader = true;
    }
}
