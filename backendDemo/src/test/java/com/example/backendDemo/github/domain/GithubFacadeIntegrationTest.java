package com.example.backendDemo.github.domain;

import com.example.backendDemo.exception.ClientException;
import com.example.backendDemo.github.GithubFacade;
import com.example.backendDemo.github.dto.GithubRepoDto;
import com.example.backendDemo.github.dto.SearchRepoForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDate;
import java.util.List;

import static com.example.backendDemo.github.domain.GithubFacadeTest.FindFavourites.createItem;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(GithubFacade.class)
@Import(GithubFacadeConfiguration.class)
class GithubFacadeIntegrationTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GithubFacade systemUnderTest;

    @Nested
    class FindRepositories {

        @Test
        void shouldFindAllRepos() throws JsonProcessingException {
            //given
            SearchRepoForm searchForm = new SearchRepoForm(null, LocalDate.of(2024, 9, 13), null);

            GithubRepoDto data = new GithubRepoDto(List.of(
                    createItem(1, "name1", "php"),
                    createItem(2, "name2", "kotlin"),
                    createItem(3, "name3", "php")));
            server.expect(requestTo(
                            "https://api.github.com/search/repositories?sort=stars&order=desc&q=created:2024-09-13"))
                    .andRespond(withSuccess(objectMapper.writeValueAsString(data), MediaType.APPLICATION_JSON));

            //when
            GithubRepoDto actual = systemUnderTest.findRepositories(searchForm);

            //then
            assertEquals(3, actual.items().size());
        }

        @Test
        void shouldThrowClientException() {
            //given
            SearchRepoForm searchForm = new SearchRepoForm(null, LocalDate.of(2024, 9, 13), null);

            server.expect(requestTo(
                            "https://api.github.com/search/repositories?sort=stars&order=desc&q=created:2024-09-13"))
                    .andRespond(withBadRequest());

            //when
            ClientException exception = catchThrowableOfType(() -> systemUnderTest.findRepositories(searchForm), ClientException.class);

            //then
            assertThat(exception.getMessage())
                    .isEqualTo("Client exception with status 400. Response body: Bad Request");
        }
    }
}