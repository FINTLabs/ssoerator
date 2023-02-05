package no.fintlabs.operator.traefik.ingress;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngressRouteSpec {
    private List<String> entryPoints = new ArrayList<>();
    private List<Route> routes = new ArrayList<>();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        private String match;
        private String kind;
        private List<Service> services;
        private List<Middleware> middlewares;


    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Service {
        private String name;
        private long port;
        private String kind;
    }

    @Data
    public static class Middleware {
        private String name;

    }
}
