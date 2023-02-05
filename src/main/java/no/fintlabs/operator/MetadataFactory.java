package no.fintlabs.operator;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MetadataFactory {

//    public ObjectMeta createObjectMetadata(HasMetadata resource) {
//        return new ObjectMetaBuilder()
//                .withName(resource.getMetadata().getName())
//                .withNamespace(resource.getMetadata().getNamespace())
//                .withLabels(LabelFactory.updateRecommendedLabels(resource))
//                .build();
//    }
//
//    public ObjectMeta createObjectMetadataWithPrometheus(SsoCrd crd) {
//        ObjectMeta objectMetadata = createObjectMetadata(crd);
//
//        Map<String, String> prometheusAnnotations = new HashMap<>() {
//            {
//                put("prometheus.io/scrape", "true");
//                put("prometheus.io/port", "8080");
//                put("prometheus.io/path", "/actuator/prometheus");
//            }
//        };
//        objectMetadata.getAnnotations().putAll(prometheusAnnotations);
//
//        return objectMetadata;
//    }

}
