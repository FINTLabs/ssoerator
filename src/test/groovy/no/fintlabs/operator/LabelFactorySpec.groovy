package no.fintlabs.operator


import io.fabric8.kubernetes.api.model.ServiceBuilder
import org.apache.groovy.util.Maps
import spock.lang.Specification

class LabelFactorySpec extends Specification {

    def "When updating recommended labels it should be one label more than in the primary resource"() {
        given:
        def crd = new SsoCrd()
        crd.getMetadata().getLabels().putAll(Maps.of("test", "test"))
        def service = new ServiceBuilder().withNewMetadata().endMetadata().build()

        when:
        def labels = LabelFactory.updateRecommendedLabels(service, crd)

        then:
        labels.size() == crd.getMetadata().getLabels().size() + 1
        service.getMetadata().getLabels().size() == crd.getMetadata().getLabels().size() + 1
    }

    def "When updating recommended labels it should contain the managed-by label"() {
        given:
        def crd = new SsoCrd()
        crd.getMetadata().getLabels().putAll(Maps.of("test", "test"))
        def service = new ServiceBuilder().withNewMetadata().endMetadata().build()

        when:
        def labels = LabelFactory.updateRecommendedLabels(service, crd)

        then:
        labels.containsKey("app.kubernetes.io/managed-by")
        service.getMetadata().getLabels().containsKey("app.kubernetes.io/managed-by")
    }
}
