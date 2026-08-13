package no.fintlabs.operator.traefik.ingress;

import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import no.fintlabs.FlaisKubernetesDependentResource;
import no.fintlabs.FlaisWorkflow;
import no.fintlabs.Transformer;
import no.fintlabs.operator.LabelFactory;
import no.fintlabs.operator.SsoCrd;
import no.fintlabs.operator.SsoSpec;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IngressRouteV3DependentResource extends FlaisKubernetesDependentResource<IngressRouteV3Crd, SsoCrd, SsoSpec> {

    private final Transformer transformer;

    public IngressRouteV3DependentResource(FlaisWorkflow<SsoCrd, SsoSpec> workflow, KubernetesClient kubernetesClient, Transformer transformer) {
        super(IngressRouteV3Crd.class, workflow, kubernetesClient);
        this.transformer = transformer;
    }

    @Override
    protected IngressRouteV3Crd desired(SsoCrd primary, Context<SsoCrd> context) {

        try {
            MixedOperation<IngressRouteV3Crd, KubernetesResourceList<IngressRouteV3Crd>, Resource<IngressRouteV3Crd>> client
                    = getKubernetesClient().resources(IngressRouteV3Crd.class);
            IngressRouteV3Crd ingressRouteCrd = client
                    .load(transformer.transform(primary, "k8s/ingress-route-v3.yaml"))
                    .get();

            ingressRouteCrd.getMetadata().setNamespace(primary.getMetadata().getNamespace());
            ingressRouteCrd.getMetadata().setName(primary.getMetadata().getName());
            LabelFactory.updateRecommendedLabels(ingressRouteCrd, primary);

            return ingressRouteCrd;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
