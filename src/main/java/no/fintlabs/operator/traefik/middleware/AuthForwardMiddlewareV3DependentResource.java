package no.fintlabs.operator.traefik.middleware;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.FlaisKubernetesDependentResource;
import no.fintlabs.FlaisWorkflow;
import no.fintlabs.Transformer;
import no.fintlabs.operator.LabelFactory;
import no.fintlabs.operator.SsoCrd;
import no.fintlabs.operator.SsoSpec;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AuthForwardMiddlewareV3DependentResource extends FlaisKubernetesDependentResource<AuthForwardMiddlewareV3Crd, SsoCrd, SsoSpec> {


    private final Transformer transformer;

    public AuthForwardMiddlewareV3DependentResource(FlaisWorkflow<SsoCrd, SsoSpec> workflow, KubernetesClient kubernetesClient, Transformer transformer) {
        super(AuthForwardMiddlewareV3Crd.class, workflow, kubernetesClient);
        this.transformer = transformer;
    }


    @Override
    protected AuthForwardMiddlewareV3Crd desired(SsoCrd primary, Context<SsoCrd> context) {

        try {
            MixedOperation<AuthForwardMiddlewareV3Crd, KubernetesResourceList<AuthForwardMiddlewareV3Crd>, Resource<AuthForwardMiddlewareV3Crd>> resources
                    = getKubernetesClient().resources(AuthForwardMiddlewareV3Crd.class);

            AuthForwardMiddlewareV3Crd middleware = resources
                    .load(transformer.transform(primary, "k8s/middleware-v3.yaml"))
                    .get();

            middleware.getMetadata().setNamespace(primary.getMetadata().getNamespace());
            log.info("Desired Traefik v3 middleware:");
            log.info(middleware.toString());
            LabelFactory.updateRecommendedLabels(middleware, primary);

            return middleware;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
