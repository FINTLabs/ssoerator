package no.fintlabs.operator.oauth;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import no.fintlabs.FlaisKubernetesDependentResource;
import no.fintlabs.FlaisWorkflow;
import no.fintlabs.Transformer;
import no.fintlabs.operator.SsoCrd;
import no.fintlabs.operator.SsoSpec;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;

import static no.fintlabs.operator.LabelFactory.updateRecommendedLabels;

@Component
public class OAuthClientDependentResource extends FlaisKubernetesDependentResource<OAuthClientCrd, SsoCrd, SsoSpec> {

    private final Transformer transformer;

    public OAuthClientDependentResource(FlaisWorkflow<SsoCrd, SsoSpec> workflow, KubernetesClient kubernetesClient, Transformer transformer) {
        super(OAuthClientCrd.class, workflow, kubernetesClient);
        this.transformer = transformer;
    }

    @Override
    protected OAuthClientCrd desired(SsoCrd primary, Context<SsoCrd> context) {
        try {
            MixedOperation<OAuthClientCrd, KubernetesResourceList<OAuthClientCrd>, Resource<OAuthClientCrd>> client = getKubernetesClient().resources(OAuthClientCrd.class);

            OAuthClientCrd oAuthClientCrd = client.load(transformer.transform(primary, "k8s/oauthclient.yaml")).get();

            oAuthClientCrd.getMetadata().setNamespace(primary.getMetadata().getNamespace());
            oAuthClientCrd.getMetadata().setName(primary.getMetadata().getName());
            updateRecommendedLabels(oAuthClientCrd, primary);


            UriComponents build = UriComponentsBuilder.newInstance()
                    .host(primary.getSpec().getHostname()).path(primary.getSpec().getBasePath()).build();


            oAuthClientCrd.getSpec().setRedirectUris(
                    Collections.singletonList(
                            UriComponentsBuilder
                                    .newInstance()
                                    .scheme("https")
                                    .host(primary.getSpec().getHostname())
                                    .pathSegment(primary.getSpec().getBasePath(), "_oauth")
                                    .build()
                                    .toUriString()
                    )
            );

            return oAuthClientCrd;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
