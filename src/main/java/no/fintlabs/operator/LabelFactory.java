package no.fintlabs.operator;

import io.fabric8.kubernetes.api.model.HasMetadata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabelFactory {

    public static List<String> MANDATORY_LABELS = Arrays.asList(
            "app.kubernetes.io/name",
            "app.kubernetes.io/instance",
            "app.kubernetes.io/version",
            "app.kubernetes.io/component",
            "app.kubernetes.io/part-of",
            "fintlabs.no/team",
            "fintlabs.no/org-id");

    public static Map<String, String> updateRecommendedLabels(HasMetadata resource, HasMetadata primary) {
        Map<String, String> recommendedLabels = resource.getMetadata().getLabels();
        recommendedLabels.put("app.kubernetes.io/managed-by", "ssoerator");
        recommendedLabels.putAll(primary.getMetadata().getLabels());

        return recommendedLabels;
    }
    public static Map<String, String> matchingLabels(HasMetadata resource) {
        Map<String, String> currentLabels = resource.getMetadata().getLabels();
        Map<String, String> matchingLabels = new HashMap<>();
        currentLabels.put("app.kubernetes.io/managed-by", "flaiserator");

        MANDATORY_LABELS.forEach(s -> matchingLabels.put(s, currentLabels.get(s)));

        return matchingLabels;
    }

}
