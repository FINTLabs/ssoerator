package no.fintlabs;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import no.fintlabs.github.GitHubPackageVersionService;
import no.fintlabs.operator.SsoCrd;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class Transformer {

    private final GitHubPackageVersionService gitHubPackageVersionService;

    public Transformer(GitHubPackageVersionService gitHubPackageVersionService) {
        this.gitHubPackageVersionService = gitHubPackageVersionService;
    }


    public InputStream transform(SsoCrd crd, String manifestFile) throws IOException {

        InputStream inputStream = new ClassPathResource(manifestFile).getInputStream();

        Template compile = Mustache.compiler().compile(new BufferedReader(new InputStreamReader(inputStream)));

        StringWriter stringWriter = new StringWriter();
        compile.execute(getContext(crd), stringWriter);

        return new ByteArrayInputStream(stringWriter.toString().getBytes());
    }

    private Map<String, String> getContext(SsoCrd crd) {
        Map<String, String> context = new HashMap<>();

        context.put("name", crd.getMetadata().getName());
        context.put("namespace", crd.getMetadata().getNamespace());
        //context.put("environment", environment);
        context.put("hostname", crd.getSpec().getHostname());
        context.put("basePath", crd.getSpec().getBasePath());
        context.put("image", gitHubPackageVersionService.getLatest());
        //context.put("orgId", crd.getMetadata().getLabels().get("fintlabs.no/org-id").replace(".", "-"));

        return context;
    }
}
