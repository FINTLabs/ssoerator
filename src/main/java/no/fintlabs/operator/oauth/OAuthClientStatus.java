package no.fintlabs.operator.oauth;

import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthClientStatus extends ObservedGenerationAwareStatus {
    private String clientUri;
    private String errorMessage;
}
