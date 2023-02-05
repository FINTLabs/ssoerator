package no.fintlabs.operator;

import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Deleter;
import io.javaoperatorsdk.operator.api.reconciler.dependent.DependentResource;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.FlaisReconiler;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ControllerConfiguration(
        generationAwareEventProcessing = false
)
public class SsoReconiler extends FlaisReconiler<SsoCrd, SsoSpec> {

    public SsoReconiler(SsoWorkflow workflow,
                        List<? extends DependentResource<?, SsoCrd>> eventSourceProviders,
                        List<? extends Deleter<SsoCrd>> deleters) {
        super(workflow, eventSourceProviders, deleters);
    }
}