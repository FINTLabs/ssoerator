package no.fintlabs.operator;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;
import no.fintlabs.FlaisCrd;

@Group("fintlabs.no")
@Version("v1alpha1")
@Kind("FLAISSso")
public class SsoCrd extends FlaisCrd<SsoSpec> implements Namespaced {

    @Override
    protected SsoSpec initSpec() {
        return new SsoSpec();
    }
}