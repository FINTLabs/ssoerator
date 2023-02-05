package no.fintlabs.operator.deployment;

import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.FlaisKubernetesDependentResource;
import no.fintlabs.FlaisWorkflow;
import no.fintlabs.Transformer;
import no.fintlabs.operator.LabelFactory;
import no.fintlabs.operator.MetadataFactory;
import no.fintlabs.operator.SsoCrd;
import no.fintlabs.operator.SsoSpec;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class DeploymentDependentResource extends FlaisKubernetesDependentResource<Deployment, SsoCrd, SsoSpec> {

    private final MetadataFactory metadataFactory;

    private final Transformer transformer;


    public DeploymentDependentResource(FlaisWorkflow<SsoCrd, SsoSpec> workflow, KubernetesClient kubernetesClient, MetadataFactory metadataFactory, Transformer transformer) {
        super(Deployment.class, workflow, kubernetesClient);
        this.metadataFactory = metadataFactory;
        this.transformer = transformer;
    }

    @Override
    protected Deployment desired(SsoCrd resource, Context<SsoCrd> context) {
        try {
            Deployment deployment = getKubernetesClient()
                    .apps()
                    .deployments()
                    .load(transformer.transform(resource, "k8s/deployment.yaml"))
                    .get();

            Map<String, String> labels = LabelFactory.updateRecommendedLabels(deployment, resource);
            deployment.getSpec().setSelector(new LabelSelectorBuilder().withMatchLabels(labels).build());
            deployment.getSpec().getTemplate().setMetadata(new ObjectMetaBuilder().withLabels(labels).build());

            return deployment;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
