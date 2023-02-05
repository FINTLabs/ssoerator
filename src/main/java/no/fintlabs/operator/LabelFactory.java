package no.fintlabs.operator;

import io.fabric8.kubernetes.api.model.HasMetadata;

import java.util.HashMap;
import java.util.Map;

public class LabelFactory {

    public static Map<String, String> updateRecommendedLabels(HasMetadata resource) {
        Map<String, String> recommendedLabels = resource.getMetadata().getLabels();
        recommendedLabels.put("app.kubernetes.io/managed-by", "ssoerator");

        return recommendedLabels;
    }

}
