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
import java.util.HashMap;

@Component
public class IngressRouteDependentResource extends FlaisKubernetesDependentResource<IngressRouteCrd, SsoCrd, SsoSpec> {

    private final Transformer transformer;

    public IngressRouteDependentResource(FlaisWorkflow<SsoCrd, SsoSpec> workflow, KubernetesClient kubernetesClient, Transformer transformer) {
        super(IngressRouteCrd.class, workflow, kubernetesClient);
        this.transformer = transformer;
    }

    @Override
    protected IngressRouteCrd desired(SsoCrd primary, Context<SsoCrd> context) {

        try {
            MixedOperation<IngressRouteCrd, KubernetesResourceList<IngressRouteCrd>, Resource<IngressRouteCrd>> client = getKubernetesClient().resources(IngressRouteCrd.class);

            IngressRouteCrd ingressRouteCrd = client.load(transformer.transform(primary, "k8s/ingress-route.yaml")).get();

//            HashMap<String, String> labels = new HashMap<>(primary.getMetadata().getLabels());
//
//            labels.put("app.kubernetes.io/managed-by", "ssoerator");

            ingressRouteCrd.getMetadata().setNamespace(primary.getMetadata().getNamespace());
            ingressRouteCrd.getMetadata().setName(primary.getMetadata().getName());
            LabelFactory.updateRecommendedLabels(ingressRouteCrd, primary);
            //ingressRouteCrd.getMetadata().setLabels(labels);

            return ingressRouteCrd;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
