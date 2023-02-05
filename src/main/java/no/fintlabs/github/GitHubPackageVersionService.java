package no.fintlabs.github;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class GitHubPackageVersionService {


    private final static String IMAGE = "ghcr.io/fintlabs/flais-auth-forward-service";
    private final WebClient webClient;

    @Getter
    private String latest;

    public GitHubPackageVersionService(WebClient webClient) {
        this.webClient = webClient;
    }


    @PostConstruct
    public void init() {
        refresh();
    }


    private void refresh() {
        List<GithubPackageVersion> versions = List.of(Objects.requireNonNull(webClient.get()
                .retrieve()
                .bodyToMono(GithubPackageVersion[].class)
                .block()));

        log.debug("{} versions of {}", versions.size(), IMAGE);

        latest = String.format("%s:%s",
                IMAGE,
                getLatestVersion(versions));

        log.debug("Latest version is {}", latest);
    }

    private String getLatestVersion(List<GithubPackageVersion> versions) {
        return versions
                .stream()
                .max(Comparator.comparing(GithubPackageVersion::getCreatedAt))
                .orElseThrow()
                .getMetadata()
                .getContainer()
                .getTags().get(0);
    }
}
