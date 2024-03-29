package no.fintlabs.operator.service;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.KubernetesClient;
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
public class ServiceDependentResource extends FlaisKubernetesDependentResource<Service, SsoCrd, SsoSpec> {

    private final Transformer transformer;

    public ServiceDependentResource(FlaisWorkflow<SsoCrd, SsoSpec> workflow, KubernetesClient kubernetesClient, Transformer transformer) {
        super(Service.class, workflow, kubernetesClient);
        this.transformer = transformer;
    }

    @Override
    protected Service desired(SsoCrd primary, Context<SsoCrd> context) {
        try {
            Service service = getKubernetesClient()
                    .services()
                    .load(transformer.transform(primary, "k8s/service.yaml"))
                    .get();

            service.getSpec().setSelector(LabelFactory.matchingLabels(primary));

            return service;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
