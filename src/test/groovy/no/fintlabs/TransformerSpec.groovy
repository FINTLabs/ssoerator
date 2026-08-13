package no.fintlabs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import io.fabric8.kubernetes.api.model.apps.Deployment
import no.fintlabs.operator.SsoCrd
import no.fintlabs.operator.SsoSpec
import spock.lang.Specification

class TransformerSpec extends Specification {

    Transformer transformer

    void setup() {
        transformer = new Transformer()
        transformer.@middlewareImage = "test-image"
    }

    def "When creating context form crd the values in the context should be equal to the corresponding values in the crd"() {
        given:
        def crd = new SsoCrd()
        def spec = new SsoSpec()
        crd.getMetadata().setName("test")
        crd.getMetadata().setNamespace("test")
        spec.setBasePath("basePath")
        spec.setHostname("hostname")
        crd.setSpec(spec)

        when:
        def context = transformer.getContext(crd)

        then:
        context.containsKey("name")
        context.get("name") == crd.getMetadata().getName()
        context.containsKey("namespace")
        context.get("namespace") == crd.getMetadata().getNamespace()
        context.containsKey("hostname")
        context.get("hostname") == crd.getSpec().getHostname()
        context.containsKey("basePath")
        context.get("basePath") == crd.getSpec().getBasePath()
        context.containsKey("image")
        context.get("image").startsWith("test-image");
    }

    def "When transforming a manifest file it should correspond with the values from the crd"() {
        given:
        def crd = new SsoCrd()
        def spec = new SsoSpec()
        crd.getMetadata().setName("test")
        crd.getMetadata().setNamespace("test")
        spec.setBasePath("basePath")
        spec.setHostname("hostname")
        crd.setSpec(spec)

        def mapper = new ObjectMapper(new YAMLFactory())


        when:
        def deployment = mapper.readValue(transformer.transform(crd, "deployment.yaml").readAllBytes(), Deployment.class)

        then:
        deployment.getMetadata().getName() == crd.getMetadata().getName()
        deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getImage().startsWith("test-image");

    }

    def "When transforming Traefik manifests they should use v2 and v3 api groups"() {
        given:
        def crd = new SsoCrd()
        def spec = new SsoSpec()
        crd.getMetadata().setName("test")
        crd.getMetadata().setNamespace("test")
        spec.setBasePath("basePath")
        spec.setHostname("hostname")
        crd.setSpec(spec)

        when:
        def ingressRouteV2 = transformer.transform(crd, "k8s/ingress-route.yaml").text
        def middlewareV2 = transformer.transform(crd, "k8s/middleware.yaml").text
        def ingressRouteV3 = transformer.transform(crd, "k8s/ingress-route-v3.yaml").text
        def middlewareV3 = transformer.transform(crd, "k8s/middleware-v3.yaml").text

        then:
        ingressRouteV2.contains("apiVersion: traefik.containo.us/v1alpha1")
        middlewareV2.contains("apiVersion: traefik.containo.us/v1alpha1")
        ingressRouteV3.contains("apiVersion: traefik.io/v1alpha1")
        middlewareV3.contains("apiVersion: traefik.io/v1alpha1")
    }

}
