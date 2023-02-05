package no.fintlabs.operator.configmap;

import io.fabric8.kubernetes.api.model.ConfigMap;
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
import java.util.HashMap;

import static no.fintlabs.operator.LabelFactory.updateRecommendedLabels;

@Component
public class ConfigMapDependentResource extends FlaisKubernetesDependentResource<ConfigMap, SsoCrd, SsoSpec> {

    private final Transformer transformer;

    public ConfigMapDependentResource(FlaisWorkflow<SsoCrd, SsoSpec> workflow, KubernetesClient kubernetesClient, Transformer transformer) {
        super(ConfigMap.class, workflow, kubernetesClient);
        this.transformer = transformer;
    }

    @Override
    protected ConfigMap desired(SsoCrd primary, Context<SsoCrd> context) {
        try {
            ConfigMap configMap = getKubernetesClient()
                    .configMaps()
                    .load(transformer.transform(primary, "k8s/config-map.yaml"))
                    .get();
            updateRecommendedLabels(configMap, primary);

            return configMap;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
