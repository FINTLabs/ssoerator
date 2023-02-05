package no.fintlabs.github

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class GitHubPackageVersionServiceSpec extends Specification {

    MockWebServer mockWebServer
    WebClient webClient
    def githubResponse = new ClassPathResource('github-response.json').getFile().text
    GitHubPackageVersionService gitHubPackageVersionService

    void setup() {
        mockWebServer = new MockWebServer()
        mockWebServer.start(8080)

        webClient = WebClient.builder().baseUrl(mockWebServer.hostName + ":" + mockWebServer.port + "/").build()

        gitHubPackageVersionService = new GitHubPackageVersionService(webClient)

    }

    void cleanup() {
        mockWebServer.shutdown()
    }

    def "When getting the latest docker image tag we should get the tag with the newest date."() {
        given:
        mockWebServer.enqueue(new MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(githubResponse))
        gitHubPackageVersionService.init()

        when:
        def latest = gitHubPackageVersionService.getLatest()

        then:
        latest.endsWith("sha-5d41ac0")
    }
}
