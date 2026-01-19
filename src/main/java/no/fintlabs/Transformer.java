package no.fintlabs;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import no.fintlabs.operator.SsoCrd;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class Transformer {


    @Value("${flais.operators.ssoerator.test-environment:false}")
    private boolean testEnvironment;

    @Value("${flais.operators.ssoerator.middleware-image:ghcr.io/fintlabs/flais-auth-forward-service:latest}")
    private String middlewareImage;

    public InputStream transform(SsoCrd crd, String manifestFile) throws IOException {

        InputStream inputStream = new ClassPathResource(manifestFile).getInputStream();

        Template compile = Mustache.compiler().compile(new BufferedReader(new InputStreamReader(inputStream)));

        StringWriter stringWriter = new StringWriter();
        compile.execute(getContext(crd), stringWriter);

        return new ByteArrayInputStream(stringWriter.toString().getBytes());
    }

    public Map<String, String> getContext(SsoCrd crd) {
        Map<String, String> context = new HashMap<>();

        context.put("name", crd.getMetadata().getName());
        context.put("namespace", crd.getMetadata().getNamespace());
        context.put("hostname", crd.getSpec().getHostname());
        context.put("basePath", crd.getSpec().getBasePath());
        context.put("oauthPath", crd.getSpec().getBasePath().endsWith("/") ? "_oauth" : "/_oauth");
        context.put("upnClaim", crd.getSpec().getUpnClaim());
        context.put("image", middlewareImage);
        context.put("loggingLevel", crd.getSpec().getLoggingLevel());
        context.put("issuerUri", testEnvironment
                ? "https://idp.test.felleskomponent.no/nidp/oauth/nam"
                : "https://idp.felleskomponent.no/nidp/oauth/nam");

        return context;
    }
}
