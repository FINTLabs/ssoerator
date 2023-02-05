package no.fintlabs.operator.traefik.middleware;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("traefik.containo.us")
@Version("v1alpha1")
@Kind("Middleware")
public class AuthForwardMiddlewareCrd extends CustomResource<AuthForwardMiddlewareSpec, Void> implements Namespaced {
    @Override
    protected AuthForwardMiddlewareSpec initSpec() {
        return new AuthForwardMiddlewareSpec();
    }
}
