package no.fintlabs.operator.oauth;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("fintlabs.no")
@Version("v1alpha1")
@Kind("NamOAuthClientApplicationResource")
public class OAuthClientCrd extends CustomResource<OAuthClientSpec, OAuthClientStatus> implements Namespaced {

    @Override
    protected OAuthClientSpec initSpec() {
        return new OAuthClientSpec();
    }

    @Override
    protected OAuthClientStatus initStatus() {
        return new OAuthClientStatus();
    }
}