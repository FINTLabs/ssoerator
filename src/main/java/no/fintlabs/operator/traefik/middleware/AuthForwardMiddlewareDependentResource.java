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
public class AuthForwardMiddlewareDependentResource extends FlaisKubernetesDependentResource<AuthForwardMiddlewareCrd, SsoCrd, SsoSpec> {


    private final Transformer transformer;

    public AuthForwardMiddlewareDependentResource(FlaisWorkflow<SsoCrd, SsoSpec> workflow, KubernetesClient kubernetesClient, Transformer transformer) {
        super(AuthForwardMiddlewareCrd.class, workflow, kubernetesClient);
        this.transformer = transformer;
    }


    @Override
    protected AuthForwardMiddlewareCrd desired(SsoCrd primary, Context<SsoCrd> context) {

        try {
            MixedOperation<AuthForwardMiddlewareCrd, KubernetesResourceList<AuthForwardMiddlewareCrd>, Resource<AuthForwardMiddlewareCrd>> resources
                    = getKubernetesClient().resources(AuthForwardMiddlewareCrd.class);

            AuthForwardMiddlewareCrd middleware = resources
                    .load(transformer.transform(primary, "k8s/middleware.yaml"))
                    .get();

            middleware.getMetadata().setNamespace(primary.getMetadata().getNamespace());
            log.info("Desired middleware:");
            log.info(middleware.toString());
            LabelFactory.updateRecommendedLabels(middleware, primary);

            return middleware;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
